import {
  ApolloClient,
  createHttpLink,
  InMemoryCache,
  split,
  from
} from '@apollo/client/core';
import { setContext } from '@apollo/client/link/context';
import { GraphQLWsLink } from "@apollo/client/link/subscriptions";
import { getMainDefinition } from "@apollo/client/utilities";
import { createClient } from "graphql-ws";
import { onError } from '@apollo/client/link/error';
import { RetryLink } from '@apollo/client/link/retry';

// HTTP Link 설정
const httpLink = createHttpLink({
  uri: 'http://localhost:9000/graphql',
  credentials: 'include', // 필요한 경우 쿠키 전송을 위해
});

// WebSocket Link 설정
const wsLink = new GraphQLWsLink(
    createClient({
      url: "ws://localhost:9000/graphql",
      connectionParams: () => {
        const token = localStorage.getItem('jwt');
        const userId = localStorage.getItem('userId');
        return {
          authorization: token ? `Bearer ${token}` : '',
          'X-USER-Id': userId || ''
        };
      },
      options: {
        reconnect: true,
        connectionTimeout: 30000,
        lazy: true,
      },
    })
);

// 인증 Link 설정
const authLink = setContext((_, { headers }) => {
  const token = localStorage.getItem('jwt');
  const userId = localStorage.getItem('userId');
  return {
    headers: {
      ...headers,
      authorization: token ? `Bearer ${token}` : '',
      'X-USER-Id': userId || ''
    },
  };
});

// 에러 처리 Link 설정
const errorLink = onError(({ graphQLErrors, networkError, operation, forward }) => {
  if (graphQLErrors) {
    graphQLErrors.forEach(({ message, locations, path }) => {
      console.error(
          `[GraphQL error]: Message: ${message}, Location: ${locations}, Path: ${path}`
      );
    });
  }
  if (networkError) {
    console.error(`[Network error]: ${networkError}`);
  }

  // 토큰 만료 등의 인증 에러 처리
  if (graphQLErrors?.some(error => error.extensions?.code === 'UNAUTHENTICATED')) {
    // 토큰 갱신 로직 또는 로그아웃 처리
    localStorage.removeItem('jwt');
    localStorage.removeItem('userId');
    window.location.href = '/login';
  }

  return forward(operation);
});

// 재시도 Link 설정
const retryLink = new RetryLink({
  delay: {
    initial: 300,
    max: 3000,
    jitter: true
  },
  attempts: {
    max: 3,
    retryIf: (error) => {
      return !!error && error.statusCode !== 401;
    }
  }
});

// Split Link 설정
const splitLink = split(
    ({ query }) => {
      const definition = getMainDefinition(query);
      return (
          definition.kind === 'OperationDefinition' &&
          definition.operation === 'subscription'
      );
    },
    wsLink,
    from([retryLink, authLink, httpLink])
);

// Apollo Client 설정
const apolloClient = new ApolloClient({
  link: from([errorLink, splitLink]),
  cache: new InMemoryCache({
    typePolicies: {
      Query: {
        fields: {
          // 캐시 정책 설정이 필요한 경우 여기에 추가
        }
      }
    }
  }),
  defaultOptions: {
    watchQuery: {
      fetchPolicy: 'cache-and-network',
      errorPolicy: 'all',
    },
    query: {
      fetchPolicy: 'network-only',
      errorPolicy: 'all',
    },
    mutate: {
      errorPolicy: 'all',
    },
  },
});

export default apolloClient;

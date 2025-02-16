package com.www.netplix.client;

import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient {

  private final RestTemplate restTemplate;

  public HttpClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String request(String uri, HttpMethod httpMethod, HttpHeaders headers, Map<String, Object> params) {
    return restTemplate.exchange(
        uri,
        httpMethod,
        new HttpEntity<>(headers),
        new ParameterizedTypeReference<String>() {
        },
        params
    ).getBody();
  }
}

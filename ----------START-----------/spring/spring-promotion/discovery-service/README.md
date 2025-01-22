# Eureka discovery-service 생성

## 1. 개요
이번 실습에서는 Spring Cloud Netflix Eureka를 사용하여 서비스 디스커버리를 구축합니다. 서비스 디스커버리는 마이크로서비스 아키텍처에서 각 서비스의 위치를 동적으로 관리하고 발견하는 핵심 컴포넌트입니다.

## 2. 사전 준비사항
- JDK 17 설치
- Docker Desktop 설치
- IDE (IntelliJ IDEA 권장)
- Git 설치

## 3. 실습 내용

### 3.1 Discovery Service 구성

1. 의존성 추가 (build.gradle)
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

2. Eureka Server 활성화 (DiscoveryServiceApplication.java)
```java
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}
```

3. 설정 파일 작성 (application.yml)
```yaml
server:
  port: 8761

spring:
  application:
    name: discovery-service

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    wait-time-in-ms-when-sync-empty: 0
```

### 4.2 클라이언트 서비스 설정

1. 의존성 추가 (각 서비스의 build.gradle)
```gradle
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
}
```

2. Eureka Client 활성화 (각 서비스의 Application.java)
```java
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
```

3. 클라이언트 설정 (application.yml)
```yaml
spring:
  application:
    name: service-name

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
  instance:
    prefer-ip-address: true
```

## 5. 실행 및 테스트

### 5.1 서비스 실행
1. Discovery Service 실행
```bash
cd discovery-service
./gradlew bootRun
```

2. 클라이언트 서비스 실행
```bash
cd [service-name]
./gradlew bootRun
```

### 5.2 동작 확인
1. Eureka Dashboard 접속
    - http://localhost:8761
    - 등록된 서비스 목록 확인
    - 서비스 상태 모니터링

2. 서비스 상태 확인
    - Instances currently registered with Eureka 섹션 확인
    - 각 서비스의 상태 및 메타데이터 확인

## 6. 주요 기능 설명

### 6.1 서비스 등록
- 서비스 시작 시 자동 등록
- 서비스 메타데이터 관리
- 상태 정보 주기적 업데이트

### 6.2 서비스 발견
- 서비스 이름으로 검색
- 로드밸런싱 자동 적용
- 서비스 목록 캐싱

### 6.3 상태 모니터링
- 헬스체크
- 서비스 가용성 확인
- 문제 있는 서비스 자동 제거

## 7. 문제 해결

### 7.1 일반적인 문제
1. 서비스가 등록되지 않는 경우
    - defaultZone URL 확인
    - 네트워크 연결 확인
    - 방화벽 설정 확인

2. 서비스가 자주 제거되는 경우
    - heartbeat 간격 조정
    - 타임아웃 설정 확인

### 7.2 설정 최적화 (Optional)
```yaml
eureka:
  instance:
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90
  server:
    enable-self-preservation: false
```
#  user-service 서비스 구현

## 1. 개요
User 서비스는 사용자 관리, 인증, 그리고 권한 관리를 담당하는 마이크로서비스입니다.

### 1.1 주요 기능
- 사용자 등록 및 관리
- 로그인 및 인증
- JWT 토큰 관리
- 사용자 프로필 관리
- 로그인 이력 관리

### 1.2 기술 스택
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT (JSON Web Token)
- H2 Database
- Lombok

## 2. 핵심 컴포넌트 설명
### 인증 관련 엔드포인트
- POST /api/v1/users/login: 로그인
- POST /api/v1/users/validate-token: 토큰 검증
- POST /api/v1/users/refresh-token: 토큰 갱신

### 사용자 관리 엔드포인트
- POST /api/v1/users/signup: 회원가입
- GET /api/v1/users/me: 프로필 조회
- PUT /api/v1/users/me: 프로필 수정
- POST /api/v1/users/me/password: 비밀번호 변경
- GET /api/v1/users/me/login-history: 로그인 이력 조회

## 3. 데이터베이스 스키마

### 3.1 Users 테이블
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

### 3.2 User Login History 테이블
```sql
CREATE TABLE user_login_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    login_at TIMESTAMP,
    ip_address VARCHAR(45),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```
### 3.3 Dependency 의존성 추가
1. 의존성 추가 (build.gradle)
```gradle
dependencies {
 implementation 'org.springframework.boot:spring-boot-starter-actuator'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	implementation 'io.jsonwebtoken:jjwt:0.12.5' # JWT 추가
	implementation 'jakarta.validation:jakarta.validation-api:3.0.2' # Validation 추가
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher' 
}
```

## 4. 마이크로서비스 통합

### 4.1 서비스 디스커버리
- Eureka Client 등록
- 서비스 이름: USER-SERVICE
- 로드밸런싱 지원

### 4.2 API Gateway 연동
- 라우팅 설정
- 인증 필터 적용
- 속도 제한 설정
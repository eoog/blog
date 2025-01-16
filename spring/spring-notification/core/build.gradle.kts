dependencies {
    // 몽고 DB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    // 도커 컨테이너 테스트 컨테이너 !!
    // 통합테스트에서 테스트를 실해하면 도커컨테이너 몽고디비 활용해서 테스트를 실해하는 작업
    // 로컬에서 테스트컨테이너가 >> 도커컨테이너를 실행
    implementation("org.testcontainers:testcontainers:1.20.1")
}


dependencies {
    // core 모듈 적용
    implementation(project(mapOf("path" to ":core")))

    // Application 의존성 추가
    implementation("org.springframework.boot:spring-boot-starter-web")

    // kafka
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")

    implementation("org.springdoc:springdoc-openapi-starter-common:2.2.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
}


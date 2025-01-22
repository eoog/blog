dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

//    실행할때 서버와..
    runtimeOnly(project(":netplix-core:core-service"))

    implementation(project(":netplix-core:core-usecase"))
}

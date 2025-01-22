dependencies {
    implementation("org.springframework:spring-context")

    //    실행할때 서버와..
    runtimeOnly(project(":netplix-adapters:adapter-http"))

    implementation(project(":netplix-core:core-usecase"))
    implementation(project(":netplix-core:core-port"))
    implementation(project(":netplix-core:core-domain"))

}

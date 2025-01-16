plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

ext["springCloudVersion"] = "2023.0.0"    // https://spring.io/projects/spring-cloud-stream

// root 프로젝트 최상위 포함
allprojects {
    group = "com.www"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

// subProject 제외 포함
subprojects {
    apply {
        plugin("java")
        plugin("org.springframework.boot") // 스프링부트
        plugin("io.spring.dependency-management") // 의존성 관리
        plugin("java-library")
        // junit 기본 의존성
    }

    // 서브 프로젝트에 전파가 안될수도 있기에 추가
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {
        // lombok
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")

        // junit 기본 의존성
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")

        // Spring boot 테스트 어노테이션
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }
    }


    tasks.test {
        useJUnitPlatform()
    }
}

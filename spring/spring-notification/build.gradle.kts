plugins {
    java
    id("org.springframework.boot") version "3.4.1"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

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
        // junit 기본 의존성
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
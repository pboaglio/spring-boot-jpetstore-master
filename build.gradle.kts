plugins {
    id("java")
    id("com.diffplug.eclipse.apt") version "3.29.1"
    id("com.diffplug.spotless") version "5.0.0"
    id("org.springframework.boot") version "2.5.0"
    id("org.seasar.doma.compile") version "1.0.0"
}

apply(plugin = "io.spring.dependency-management")

version = "2.36.0-beta-1"
val dependentVersion = version
val domaSpringBootVersion = "1.4.0"

spotless {
    java {
        googleJavaFormat("1.7")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

springBoot {
    mainClass.set("sample.Application")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://repo.spring.io/milestone")
}

dependencies {
    val domaVersion: String by project
    val domaSpringBootVersion: String by project
    annotationProcessor("org.seasar.doma:doma-processor:${dependentVersion}")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.3.0.RC1")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    //implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.seasar.doma:doma-core:${dependentVersion}")
    implementation("org.seasar.doma.boot:doma-spring-boot-starter:${domaSpringBootVersion}")

    implementation("com.h2database:h2:1.4.200")
    implementation("org.webjars:jquery:3.6.0")
    compileOnly("org.springframework.boot:spring-boot-devtools")

    //implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:2.5.3")
    //developmentOnly("org.springframework.boot:spring-boot-devtools")
    //testImplementation("org.springframework.boot:spring-boot-starter-test")

    //{
    //    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    //}
    
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.0.RC1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.mockito:mockito-junit-jupiter:3.3.3")

    //Agregado esto para poder usar JUnit3-4 y probar con el framework de JUnit5
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testCompileOnly("junit:junit:4.13")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")

}


tasks.test {
    useJUnitPlatform()
}


eclipse {
    classpath {
        file {
            whenMerged {
                val classpath = this as org.gradle.plugins.ide.eclipse.model.Classpath
                classpath.entries.removeAll {
                    when (it) {
                        is org.gradle.plugins.ide.eclipse.model.Output -> it.path == ".apt_generated"
                        else -> false
                    }
                }
            }
            withXml {
                val node = asNode()
                node.appendNode("classpathentry", mapOf("kind" to "src", "output" to "bin/main", "path" to ".apt_generated"))
            }
        }
    }
    jdt {
        javaRuntimeName = "JavaSE-11"
    }
}

//tasks {
//    test {
//        useJUnitPlatform()
//    }
//}
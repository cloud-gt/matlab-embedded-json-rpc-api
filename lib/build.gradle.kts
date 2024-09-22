plugins {
    `java-library`
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.bundles.testlibs)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(8)
    }
}

tasks {
    build {
        dependsOn(
            jacocoTestReport,
            jacocoTestCoverageVerification,
        )
    }

    test {
        useJUnitPlatform()
    }

    jacocoTestReport {
        mustRunAfter(test)
        reports {
            xml.required = false
            csv.required = false
            html.required = true
        }
    }

    jacocoTestCoverageVerification {
        mustRunAfter(test)
        violationRules {
            rule {
                element = "SOURCEFILE"
                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "1".toBigDecimal()
                }
            }
            rule {
                element = "SOURCEFILE"
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "1".toBigDecimal()
                }
            }
        }
    }
}


plugins {
    `java-library`
    jacoco
    alias(libs.plugins.pitest)
}

tasks {
    jar {
        enabled = false
    }
}

subprojects {
    pluginManager.apply("java-library")
    pluginManager.apply("jacoco")

    group = "dev.cloudgt"

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(rootProject.libs.bundles.testlibs)
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

}
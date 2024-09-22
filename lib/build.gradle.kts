plugins {
    `java-library`
    jacoco
    alias(libs.plugins.pitest)
}

group = "dev.cloudgt"

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

pitest {
    pitestVersion = "1.17.0"
    junit5PluginVersion = "1.2.1"
    outputFormats = listOf("HTML")
    threads = 4
}

tasks {
    build {
        dependsOn(
            jacocoTestReport,
            jacocoTestCoverageVerification,
            pitest,
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

    this.pitest {
        mustRunAfter(test)
    }
}


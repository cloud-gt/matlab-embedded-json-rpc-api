plugins {
    alias(libs.plugins.pitest)
    alias(libs.plugins.jvm)
    distribution
}

val matlabroot = providers.gradleProperty("matlabroot").get()

kotlin {
    javaToolchains {
        jvmToolchain(8)
    }
}

dependencies {
    implementation(project(":matlab-embedded"))
    compileOnly(files("$matlabroot/extern/engines/java/jar/engine.jar"))

    implementation(platform("org.http4k:http4k-bom:5.27.0.0"))
    implementation("org.http4k:http4k-format-jackson")
    implementation("org.http4k:http4k-jsonrpc")
    implementation("org.http4k:http4k-server-websocket")
    implementation("org.http4k:http4k-client-websocket")
    implementation("org.http4k:http4k-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")
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
//            pitest,
        )
    }

    jar {
        archiveBaseName = "json-rpc-api-lib"
    }

    this.pitest {
        mustRunAfter(test)
    }
}

distributions {
    main {
        distributionBaseName = "json-rpc-api-lib"
        contents {
            from(tasks.jar)
            from(configurations.runtimeClasspath)
        }
    }
}


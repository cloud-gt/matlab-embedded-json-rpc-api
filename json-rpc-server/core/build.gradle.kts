plugins {
    alias(libs.plugins.pitest)
    alias(libs.plugins.jvm)
}

val matlabroot = providers.gradleProperty("matlabroot").get()

kotlin {
    javaToolchains {
        jvmToolchain(8)
    }
}

dependencies {
    compileOnly(files("$matlabroot/extern/engines/java/jar/engine.jar"))
    implementation(project(":json-rpc-server:api"))

    implementation(libs.matlabcontrol)

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
        archiveBaseName = "matlab-engine-json-rpc-server-core"
    }

    this.pitest {
        mustRunAfter(test)
    }
}
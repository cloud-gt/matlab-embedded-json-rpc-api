plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "matlab-embedded-json-rpc-api"

include("matlab-embedded")
include("json-rpc-server:api")
include("json-rpc-server:core")

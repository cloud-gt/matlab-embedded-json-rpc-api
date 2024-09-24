val matlabroot = providers.gradleProperty("matlabroot").get()

dependencies {
    compileOnly(files("$matlabroot/extern/engines/java/jar/engine.jar"))
}

tasks.jar {
    archiveBaseName = "matlab-engine-json-rpc-server-api"
}
plugins {
    distribution
}

val matlabroot = providers.gradleProperty("matlabroot").get()

val matlabServerCore by configurations.creating
val matlabServerApi by configurations.creating

configurations.implementation {
    extendsFrom(matlabServerApi)
}

dependencies {
    compileOnly(files("$matlabroot/extern/engines/java/jar/engine.jar"))
    matlabServerApi(project(":json-rpc-server:api"))
    matlabServerCore(project(":json-rpc-server:core"))
}

tasks {
    jar {
        archiveBaseName = "matlab-rpc-server-loader"
    }
}

distributions {
    main {
        distributionBaseName = "matlab-engine-json-rpc"
        contents {
            from(layout.projectDirectory.dir("src/conf"))
            into("rpc-server") {
                from(matlabServerCore)
            }
            into("matlab/path") {
                from(files("src/main/matlab"))
            }
            into("matlab/javaclasspath") {
                from(tasks.jar)
                from(matlabServerApi)
            }
        }
    }
}

val startDir = layout.buildDirectory.dir("matlab/start").get().asFile

tasks.register<Exec>("matlabDev") {
    dependsOn(tasks.installDist)
    doFirst {
        startDir.mkdirs()
        val startupCommands = startDir.resolve("startup.m")
        startupCommands.writeText(
            """
            restoredefaultpath;
            addpath(genpath('${layout.buildDirectory.dir("install").get().asFile.path}'));
            clear all;
            cd('${projectDir.path}');
            dev.cloudgt.matlab.loadRpcEngine();
            dev.cloudgt.matlab.startRpcEngine(9000);
        """.trimIndent()
        )
    }
    commandLine(
        "$matlabroot/bin/matlab",
        "-sd",
        startDir.path,
        "-desktop",
    )
}

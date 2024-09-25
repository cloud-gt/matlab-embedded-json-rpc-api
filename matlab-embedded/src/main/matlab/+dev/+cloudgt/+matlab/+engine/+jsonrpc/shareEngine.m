function shareEngine(port)
[installRootDir, ~, ~] = fileparts(which('matlab-engine-json-rpc.anchor'));

jpath = javaclasspath('-all');

matlabJars = dir(fullfile(installRootDir, 'matlab', 'javaclasspath'));
for i=1:length(matlabJars)
    jar = matlabJars(i);
    if ~jar.isdir
        jarPath = fullfile(jar.folder, jar.name);

        if ~any(strcmp(jpath, jarPath))
            javaaddpath(fullfile(jar.folder, jar.name));
        end
    end
end

serverJarsDir = fullfile(installRootDir, 'rpc-server');
dev.cloudgt.matlab.MatlabRpcServerLoader.load(serverJarsDir, version('-release'));

dev.cloudgt.matlab.MatlabEmbeddedRpcServer.start(port);
end
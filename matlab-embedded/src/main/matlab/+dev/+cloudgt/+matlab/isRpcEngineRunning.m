function b = isRpcEngineRunning()
    b = dev.cloudgt.matlab.MatlabEmbeddedRpcServer.isRunning();
end
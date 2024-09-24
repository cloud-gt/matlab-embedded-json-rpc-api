package dev.cloudgt.matlab;

import com.mathworks.engine.MatlabEngine;

import java.util.function.Supplier;

public interface MatlabRpcServerFactory {

    MatlabRpcServer create(Supplier<MatlabEngine> engineMaker);

}

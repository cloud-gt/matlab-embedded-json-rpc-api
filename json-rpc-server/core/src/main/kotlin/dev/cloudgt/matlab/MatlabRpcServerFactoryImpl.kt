package dev.cloudgt.matlab

import com.mathworks.engine.MatlabEngine
import java.util.function.Supplier

class MatlabRpcServerFactoryImpl : MatlabRpcServerFactory {
    override fun create(engineMaker: Supplier<MatlabEngine>) = MatlabRpcServerImpl(engineMaker)
}
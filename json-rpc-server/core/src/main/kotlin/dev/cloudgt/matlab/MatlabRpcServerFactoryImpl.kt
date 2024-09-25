package dev.cloudgt.matlab

import com.mathworks.engine.MatlabEngine
import org.n52.matlab.control.MatlabProxy
import org.n52.matlab.control.MatlabProxyFactory
import org.n52.matlab.control.MatlabProxyFactoryOptions
import java.io.Writer

class MatlabRpcServerFactoryImpl : MatlabRpcServerFactory {
    override fun create(matlabVersion: String): MatlabRpcServer {
        val version = parseVersion(matlabVersion)
        if (version.year <= 2020 || (version.year == 2021 && version.revision == MatlabRevision.A)) {
            return MatlabRpcServerImpl { MatlabControlProxy() }
        }
        return MatlabRpcServerImpl { MatlabEngineProxy() }
    }
}

class MatlabEngineProxy(private val engine: MatlabEngine = MatlabEngine.getCurrentMatlab()) : MatlabEngineApi {
    override fun eval(command: String, stdout: Writer, stderr: Writer) {
        engine.eval(command, stdout, stderr)
    }

    override fun feval(
        nlhs: Int,
        func: String,
        stdout: Writer,
        stderr: Writer,
        vararg args: Any
    ): Any {
        return engine.feval<Any>(nlhs, func, stdout, stderr) ?: Unit
    }

    override fun getVariable(name: String): Any {
        return engine.getVariable(name) ?: Unit
    }

    override fun putVariable(name: String, value: Any) {
        return engine.putVariable(name, value)
    }

    override fun disconnect() {
        engine.disconnect()
    }
}

class MatlabControlProxy : MatlabEngineApi {

    private val proxy: MatlabProxy

    init {
        val options = MatlabProxyFactoryOptions.Builder().build()
        proxy = MatlabProxyFactory(options).proxy
    }


    override fun eval(command: String, stdout: Writer, stderr: Writer) {
        proxy.eval(command)
    }

    override fun feval(
        nlhs: Int,
        func: String,
        stdout: Writer,
        stderr: Writer,
        vararg args: Any
    ): Any {
        return proxy.returningFeval(func, nlhs, args) ?: Unit
    }

    override fun getVariable(name: String): Any {
        return proxy.getVariable(name) ?: Unit
    }

    override fun putVariable(name: String, value: Any) {
        proxy.setVariable(name, value)
    }

    override fun disconnect() {
        proxy.disconnect()
    }
}

private fun parseVersion(version: String): MatlabVersion {
    val matchResult = Regex("(?<name>\\d{4})(?<revision>[ab])").matchEntire(version)
        ?: throw MatlabEngineApi.EngineException("Invalid MATLAB version: $version")

    return MatlabVersion(
        year = matchResult.groups["name"]!!.value.toInt(),
        revision = if (matchResult.groups["revision"]!!.value == "a") MatlabRevision.A else MatlabRevision.B
    )
}

data class MatlabVersion(
    val year: Int,
    val revision: MatlabRevision
)

enum class MatlabRevision {
    A, B
}
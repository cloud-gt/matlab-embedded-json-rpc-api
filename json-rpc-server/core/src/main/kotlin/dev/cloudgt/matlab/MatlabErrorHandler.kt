package dev.cloudgt.matlab

import org.http4k.format.Json
import org.http4k.jsonrpc.ErrorHandler
import org.http4k.jsonrpc.ErrorMessage
import java.lang.IllegalStateException
import java.util.concurrent.CancellationException

object MatlabErrorHandler : ErrorHandler {
    override fun invoke(error: Throwable): ErrorMessage? = when (error) {
        is MatlabEngineApi.EngineException -> MatlabExceptionMessage(
            1,
            "Failure by MATLAB® to start, connect, terminate, or disconnect",
            error
        )

        is MatlabEngineApi.UnsupportedTypeException -> MatlabExceptionMessage(
            2,
            "Unsupported data type in input or output of MATLAB function",
            error
        )

        is MatlabEngineApi.MatlabExecutionException -> MatlabExceptionMessage(3, "Runtime error in MATLAB code", error)
        is MatlabEngineApi.MatlabSyntaxException -> MatlabExceptionMessage(
            4,
            "Syntax error in MATLAB expression",
            error
        )

        is CancellationException -> MatlabExceptionMessage(5, "Evaluation of a MATLAB function was canceled", error)
        is InterruptedException -> MatlabExceptionMessage(6, "Evaluation of a MATLAB function was interrupted", error)
        is IllegalStateException -> MatlabExceptionMessage(7, "The MATLAB session is not available.", error)

        else -> null
    }

    private class MatlabExceptionMessage(
        code: Int,
        message: String,
        private val throwable: Throwable
    ) : ErrorMessage(code, message) {
        override fun <NODE> data(json: Json<NODE>): NODE =
            json.string(throwable.message ?: "Something went wrong in MATLAB.")

    }

}
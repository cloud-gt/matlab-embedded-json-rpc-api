package dev.cloudgt.matlab

import java.io.Writer

interface MatlabEngineApi {
    fun eval(string: String, stdout: Writer, stderr: Writer)
    fun <T> feval(nlhs: Int, func: String, stdout: Writer, stderr: Writer, vararg args: Any): T?
    fun <T> getVariable(name: String): T?
    fun putVariable(name: String, value: Any)
    fun disconnect()
}
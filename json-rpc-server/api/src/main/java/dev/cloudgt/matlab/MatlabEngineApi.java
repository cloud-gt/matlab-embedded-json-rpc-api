package dev.cloudgt.matlab;

import java.io.Writer;

public interface MatlabEngineApi {
    void eval(String command, Writer stdout, Writer stderr) throws
            InterruptedException,
            IllegalStateException,
            MatlabExecutionException,
            MatlabSyntaxException;

    Object feval(int nlhs, String func, Writer stdout, Writer stderr, Object... args) throws
            InterruptedException,
            IllegalStateException,
            MatlabExecutionException,
            UnsupportedTypeException,
            MatlabSyntaxException;

    Object getVariable(String name) throws
            InterruptedException,
            IllegalStateException;

    void putVariable(String name, Object value) throws
            InterruptedException,
            IllegalStateException;

    void disconnect();

    class EngineException extends Exception {
        public EngineException(String message) {
            super(message);
        }
    }

    class MatlabExecutionException extends Exception {}

    class MatlabSyntaxException extends Exception {}

    class UnsupportedTypeException extends Exception {}
}

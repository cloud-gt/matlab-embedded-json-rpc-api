package dev.cloudgt.matlab;

public class MatlabEmbeddedRpcServer {

    private static MatlabRpcServer INSTANCE;
    private static boolean IS_RUNNING = false;

    public static void start(int port) {
        if (null == INSTANCE) {
            INSTANCE = MatlabRpcServerLoader.factory.create(MatlabRpcServerLoader.matlabVersion);
        } else if (IS_RUNNING) {
            throw new RuntimeException("MATLAB RPC ENGINE IS ALREADY STARTED");
        }

        INSTANCE.start(port);
        IS_RUNNING = true;
    }

    public static void stop() {
        INSTANCE.stop();
        IS_RUNNING = false;
    }

    public static int port() {
        return INSTANCE.port();
    }

    public static boolean isRunning() {
        return IS_RUNNING;
    }

}

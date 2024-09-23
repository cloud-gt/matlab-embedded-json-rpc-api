package dev.cloudgt.matlab;

public interface JsonRpcApi {

    static JsonRpcApi create() {
        return new JsonRpcApiLoader();
    }

    void start(int port);

    void stop();

}

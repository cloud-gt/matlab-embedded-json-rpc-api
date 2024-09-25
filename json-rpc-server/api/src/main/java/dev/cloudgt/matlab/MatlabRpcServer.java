package dev.cloudgt.matlab;

public interface MatlabRpcServer {
    void start(int port);

    void stop();

    int port();
}

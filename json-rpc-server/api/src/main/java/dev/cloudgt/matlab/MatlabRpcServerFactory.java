package dev.cloudgt.matlab;

public interface MatlabRpcServerFactory {

    MatlabRpcServer create(String matlabVersion);

}

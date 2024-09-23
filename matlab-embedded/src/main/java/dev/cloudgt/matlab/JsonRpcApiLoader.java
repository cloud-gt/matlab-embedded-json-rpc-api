package dev.cloudgt.matlab;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ServiceLoader;

public class JsonRpcApiLoader implements JsonRpcApi {

    private final JsonRpcApi api;

    JsonRpcApiLoader() {
        try {
            URL[] urls = Arrays.stream(Paths.get("/Users/guillaumetaffin/dev/projetcs/matlab-embedded-json-rpc-api/lib/build/install/json-rpc-api-lib").toFile().listFiles())
                    .map(it -> {
                        try {
                            return it.toURI().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

            URLClassLoader apiLoader = new URLClassLoader(
                    urls,
                    JsonRpcApiLoader.class.getClassLoader()
            );
            Class<?> clazz = Class.forName("");
            ServiceLoader<JsonRpcApi> loader = ServiceLoader.load(JsonRpcApi.class, apiLoader);
            loader.reload();
            api = loader.iterator().next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(int port) {
        api.start(port);
    }

    @Override
    public void stop() {
        api.stop();
    }
}

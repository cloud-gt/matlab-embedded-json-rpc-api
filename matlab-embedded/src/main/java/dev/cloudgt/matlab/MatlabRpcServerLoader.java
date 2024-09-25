package dev.cloudgt.matlab;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ServiceLoader;

public class MatlabRpcServerLoader {

    static MatlabRpcServerFactory factory;
    static String matlabVersion;

    public static void load(String folder, String matlabVersion) {
        MatlabRpcServerLoader.matlabVersion = matlabVersion;
        try {
            File[] files = Paths.get(folder).toFile().listFiles();
            if (files == null) {
                files = new File[0];
            }
            URL[] urls = Arrays.stream(files)
                    .map(it -> {
                        try {
                            return it.toURI().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

            URLClassLoader serverClassLoader = new URLClassLoader(
                    urls,
                    MatlabRpcServerLoader.class.getClassLoader()
            );

            ServiceLoader<MatlabRpcServerFactory> loader = ServiceLoader.load(MatlabRpcServerFactory.class, serverClassLoader);
            loader.reload();
            factory = loader.iterator().next();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

package org.linkki.core.binding.dispatcher.reflection.accessor;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Stream;

class SimulatedRestartClassLoader extends URLClassLoader {

    private final ClassLoader delegate;
    private final Class<?>[] classes;

    public SimulatedRestartClassLoader(Class<?>... classes) {
        super(Stream.of(classes)
                .map(c -> c.getProtectionDomain().getCodeSource().getLocation())
                .toArray(URL[]::new), null);
        this.classes = classes;
        this.delegate = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (Arrays.stream(classes).map(Class::getName).anyMatch(name::equals)) {
            return findClass(name);
        }
        return delegate.loadClass(name);
    }
}

package scan;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Scan {

    private static List<File> scan(File dirOrF) {
        List<File> files = new ArrayList<>();
        Optional.ofNullable(dirOrF.listFiles()).ifPresent(s -> Arrays.stream(s)
                .collect(Collectors.collectingAndThen(Collectors.partitioningBy(x -> x.isDirectory()), e -> {
                    e.get(true).forEach(x -> scan(x).forEach(files::add));
                    e.get(false).forEach(files::add);
                    return null;
                })));
        return files;
    }

    private static Object instantiate(Class<?> c) {
        if (Modifier.isAbstract(c.getModifiers())) {
            return null;
        }
        Object[] test = new Object[1];
        Arrays.stream(c.getConstructors()).forEach(cons -> {
            Parameter[] param = cons.getParameters();
            if (param.length == 0) {
                try {
                    test[0] = cons.newInstance();
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return test[0];
    }

    public static <T extends Annotation> List<Object> search(Class<T> annotation) throws IOException {
        Iterator<URL> resources;
        resources = ClassLoader.getSystemClassLoader().getResources("").asIterator();
        List<File> files = new ArrayList<File>();
        String dirName = null;
        while (resources.hasNext()) {
            var name = resources.next().getFile();
            if (name.startsWith("file:")) {
                continue;
            }
            dirName = name;
            var file = new File(dirName);
            files = scan(file);
        }
        final var dir = dirName;

        var classLoader = new URLClassLoader(new URL[] {
                new URL(dir.startsWith("file") ? dir : "file:" + dir)
        });

        List<Object> annotatedClasses = new ArrayList<>();

        files.forEach(file -> {
            try {
                var fqn = file.getAbsolutePath()
                        .replace(dir, "")
                        .replace(".class", "")
                        .replace("/", ".");

                Class<?> c = classLoader.loadClass(fqn);
                Object instance = instantiate(c);
                if (instance != null && instance.getClass().isAnnotationPresent(annotation)) {
                    annotatedClasses.add(instance);
                }
            } catch (ClassNotFoundException e) {

            }
        });
        classLoader.close();
        return annotatedClasses;
    }

}

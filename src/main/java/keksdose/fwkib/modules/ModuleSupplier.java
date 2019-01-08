package keksdose.fwkib.modules;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class ModuleSupplier {
    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static final String path = "keksdose.fwkib.modules.commands";

    private Map<String, Class<?>> classMap;

    public ModuleSupplier() {
        classMap = new HashMap<>();
        ClassPath classpath;
        try {
            classpath = ClassPath.from(loader);
            ImmutableList<ClassInfo> classes = classpath.getTopLevelClasses(path).asList();
            classes.forEach(v -> classMap.put(v.getSimpleName(), v.load()));
            // classes.forEach(v -> System.out.println(v.getSimpleName()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Command getCommand(String className) {
        System.out.println(className);
        System.out.println(classMap.keySet().toString());
        try {
            Set<String> set = classMap.keySet().stream().filter(s -> Pattern.compile(className).matcher(s).find())
                    .collect(Collectors.toSet());
            return (Command) classMap.get(set.stream().findAny().get()).getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

}

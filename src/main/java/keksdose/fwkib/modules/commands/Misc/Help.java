package keksdose.fwkib.modules.commands.Misc;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import keksdose.fwkib.modules.Command;
import keksdose.fwkib.modules.ModuleSupplier;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import org.apache.commons.lang3.StringUtils;

public class Help implements Command {

    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static final String path = "keksdose.fwkib.modules.commands";

    @Override
    public String apply(String message) {
        message = message.trim();
        if (message.isEmpty()) {
            return "Vorhandene Befehlsgruppen: " + Arrays.stream(Package.getPackages())
                    .filter(v -> v.getName().startsWith(path))
                    .map(v -> StringUtils.substringAfterLast(v.getName(), "."))
                    .collect(Collectors.joining(", "));
        } else {
            try {
                ClassPath classpath = ClassPath.from(loader);
                String pathWithSubPackage = path + "." + StringUtils.capitalize(message);
                ImmutableList<ClassInfo> classes =
                        classpath.getTopLevelClasses(pathWithSubPackage).asList();

                if (!classes.isEmpty()) {
                    String classNames = classes.stream()
                            .map(v -> v.getSimpleName())
                            .sorted()
                            .collect(Collectors.joining(", "));
                    return "Nutzbare Module: " + classNames + " Mehr Infos: #help $modulname ";
                }
                return new ModuleSupplier().supplyCommand(message).help("");
            } catch (IOException e) {
                return "Beim Nachschauen ging etwas schief. Frag mal nach.";
            }
        }
    }

}

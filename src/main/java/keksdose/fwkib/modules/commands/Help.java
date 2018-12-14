package keksdose.fwkib.modules.commands;

import java.io.IOException;
import java.util.List;
import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class Help implements Command {

    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    private static final String path = "keksdose.fwkib.modules.commands";

    @Override
    public String apply(String message) {
        List<String> parameterList = Splitter.on("#help").omitEmptyStrings().trimResults().splitToList(message);
        if (parameterList.size() != 1) {
            try {
                ClassPath classpath = ClassPath.from(loader);
                ImmutableList<ClassInfo> classes = classpath.getTopLevelClasses(path).asList();
                String var = classes.toString().replaceAll(path + ".", "");
                var = var.substring(1, var.length() - 1);
                return "Nutzbare Module: " + var + " Mehr Infos: #help $modulname ";

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return new MongoDB().getHelp(parameterList.get(0));
        }

        return "";
    }

}

package keksdose.fwkib.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import keksdose.fwkib.modules.commands.Util.EmptyCommand;

public class CommandController {
    private ModuleSupplier supplier = new ModuleSupplier();

    public String executeInput(String message) {
        Pattern p = Pattern.compile("#[^\\s]*");
        List<String> matches = new ArrayList<>();
        Matcher m = p.matcher(message);
        while (m.find()) {
            matches.add(m.group());
        }
        for (String var : matches) {
            message = message.replace(var, "").trim();
            System.out.println(message);
            // entfernt die #.* aus der Nachricht
        }
        if (matches.size() == 0) {
            return "";
        }
        if (matches.size() == 1) {
            return supplier.supplyCommand(matches.get(0)).apply(message);
        } else {
            Function<String, String> c = new EmptyCommand();
            for (String cmd : matches) {
                c = c.andThen(supplier.supplyCommand(cmd.trim()));

            }
            message = message.replaceAll("#.*\\s", "").trim();
            return c.apply(message);
        }
    }
}

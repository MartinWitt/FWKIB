package keksdose.fwkib.modules.commands;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import keksdose.fwkib.modules.Command;

public class Version implements Command {

    @Override
    public String apply(String message) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("version"))
                    .replaceAll("\n", " ")
                    .trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ":(";
    }

    @Override
    public String help(String message) {
        return "Version und so";
    }
}

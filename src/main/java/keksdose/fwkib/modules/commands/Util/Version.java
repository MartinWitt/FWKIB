package keksdose.fwkib.modules.commands.Util;

import java.io.IOException;
import java.time.LocalDateTime;
import org.apache.commons.io.IOUtils;
import edu.stanford.nlp.util.Pair;
import keksdose.fwkib.modules.Command;

public class Version implements Command {
    private static final LocalDateTime date = LocalDateTime.now();

    @Override
    public String apply(String message) {
        try {
            return (IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("version"))
                    .replaceAll("\n", " ") + " " + "runs since:" + date.toString()).trim();
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

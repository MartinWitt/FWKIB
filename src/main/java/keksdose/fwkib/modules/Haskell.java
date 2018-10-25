package keksdose.fwkib.modules;

import java.io.SequenceInputStream;
import java.util.Scanner;

import org.pircbotx.hooks.events.MessageEvent;

public class Haskell implements Command {

    @Override
    public String apply(MessageEvent event) {
        try {
            String haskellString = event.getMessage().length() > 8 ? event.getMessage().substring(8) : "";
            if (haskellString.isEmpty()) {
                return "";
            }

            String[] args = { "mueval", "-E", "-e", haskellString };
            Process p = Runtime.getRuntime().exec(args);
            SequenceInputStream test = new SequenceInputStream(p.getErrorStream(), p.getInputStream());
            p.waitFor();
            Scanner s = new Scanner(test);
            String output = s.hasNext() ? s.next() : "";

            event.getChannel().send().message(output);
            s.close();
        } catch (Exception e) {
        }
        return "";

    }

}
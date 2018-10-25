package keksdose.fwkib.modules;

import java.util.Arrays;

import org.pircbotx.hooks.events.MessageEvent;

public class Help implements Command {
    private static final String[] commands = { "brati", "hash", "haskell", "haskell-url", "help", "home", "stats",
            "pwgen", "rsagen-pri", "rsagen-pub", "security", "uuid" };

    @Override
    public String apply(MessageEvent event) {
        return Arrays.toString(commands);

    }

}
package keksdose.fwkib.modules;



import java.util.UUID;

import org.pircbotx.hooks.events.MessageEvent;

public class Home implements Command {
    private static String gitHubhome = "https://github.com/MartinWitt/FWKIB";
    private static String quote = "Any fool can write code that a computer can understand:  ";

    @Override
    public String apply(String message) {
        return quote + gitHubhome;

    }

}
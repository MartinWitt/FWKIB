package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;

public class Home implements Command {
    private static String gitHubhome = "https://github.com/MartinWitt/FWKIB";
    private static String quote = "Any fool can write code that a computer can understand:  ";

    @Override
    public String apply(String message) {
        return quote + gitHubhome;
    }

    @Override
    public String help(String message) {
        return "eine traurige Bautstelle bis *DU* die neuschreibst. (Schau es lieber nicht an)";
    }
}

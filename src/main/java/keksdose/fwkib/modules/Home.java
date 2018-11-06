package keksdose.fwkib.modules;

public class Home implements Command {
    private static String gitHubhome = "https://github.com/MartinWitt/FWKIB";
    private static String quote = "Any fool can write code that a computer can understand:  ";

    @Override
    public String apply(String message) {
        return quote + gitHubhome;

    }

}
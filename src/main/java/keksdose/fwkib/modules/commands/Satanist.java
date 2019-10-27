package keksdose.fwkib.modules.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import keksdose.fwkib.modules.Command;

public class Satanist implements Command {
    private static final List<String> words = new ArrayList<>() {
        private static final long serialVersionUID = 1L;

        {
            add("nach deiner geistigen Gesundheit");
            add("ob du ihm ein bier bringst");
            add("ob du behindert bist");
        }
    };

    @Override
    public String apply(String message) {
        return ("sat" + "\uFEFF" + "anist fragt ") + words.stream()
                .filter(v -> v.toLowerCase().contains(message.toLowerCase()))
                .findAny()
                .orElse(words.get(new Random().ints(0, words.size()).findAny().getAsInt()));

    }

    @Override
    public String help(String message) {
        return "Fragen welche dir helfen können dein Problem zu lösen (präsentiert von sat"
                + "\uFEFF" + "anist)";
    }


}

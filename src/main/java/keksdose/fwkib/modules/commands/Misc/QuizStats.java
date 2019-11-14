package keksdose.fwkib.modules.commands.Misc;

import keksdose.fwkib.modules.Command;

import keksdose.fwkib.mongo.MongoDB;

public class QuizStats implements Command {

    @Override
    public String apply(String message) {
        if (message.isEmpty()) {
            return new MongoDB().getStats();
        } else {
            return new MongoDB().getStats(message);

        }
    }

    @Override
    public String help(String message) {
        return "Zeigt dir wie klug Leute sind. Nutzung: #quizstats oder #quizstats $username";
    }

}

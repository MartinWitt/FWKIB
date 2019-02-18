package keksdose.fwkib.modules.commands;

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
}
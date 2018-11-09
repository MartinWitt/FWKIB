package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.quiz.DB.MongoDB;

public class Brati implements Command {

    @Override
    public String apply(String message) {
        new MongoDB();
        return String.valueOf(new MongoDB().getBrati());

    }

}
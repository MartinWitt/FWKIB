package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class Sleepdose implements Command {

    @Override
    public String apply(String message) {
        if (message.isBlank()) {
            return String.valueOf(new MongoDB().getKeksdose());
        } else {
            return String.valueOf(new MongoDB().getkeksdose(message));
        }
    }
}

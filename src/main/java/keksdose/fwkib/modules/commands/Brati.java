package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class Brati implements Command {

    @Override
    public String apply(String message) {
        if (message.trim().isEmpty()) {
            return String.valueOf(new MongoDB().getBrati());
        } else {
            return String.valueOf(new MongoDB().getBrati(message));
        }

    }

    @Override
    public String help(String message) {
        return "irgendwelche komischen Sätze von Brati. Nutzung #brati $regex";
    }

}

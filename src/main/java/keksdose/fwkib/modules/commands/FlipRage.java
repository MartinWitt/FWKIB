package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class FlipRage implements Command {

    @Override
    public String apply(String message) {
        return new MongoDB().flipRage(message);

    }

}
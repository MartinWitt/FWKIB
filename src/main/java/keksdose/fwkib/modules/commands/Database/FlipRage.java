package keksdose.fwkib.modules.commands.Database;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class FlipRage implements Command {

    @Override
    public String apply(String message) {
        return MongoDB.MongoDB.flipRage(message);
    }

    @Override
    public String help(String message) {
        return "macht aus einem #brati ein #rage und umgekehrt. Nutzung: #fliprage $regex";
    }

}

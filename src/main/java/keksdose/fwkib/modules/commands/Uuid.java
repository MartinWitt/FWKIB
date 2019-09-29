package keksdose.fwkib.modules.commands;

import java.util.UUID;
import keksdose.fwkib.modules.Command;

public class Uuid implements Command {

    @Override
    public String apply(String message) {
        return UUID.randomUUID().toString();
    }

    @Override
    public String help(String message) {
        return "einzigartig und so";
    }
}

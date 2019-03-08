package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;

public class Revert implements Command {

    @Override
    public String apply(String message) {
        return new StringBuilder().append(message).reverse().toString();
    }
}
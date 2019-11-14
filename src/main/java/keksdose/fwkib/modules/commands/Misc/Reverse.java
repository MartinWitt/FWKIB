package keksdose.fwkib.modules.commands.Misc;

import keksdose.fwkib.modules.Command;

public class Reverse implements Command {

    @Override
    public String apply(String message) {
        return new StringBuilder().append(message).reverse().toString();
    }

    @Override
    public String help(String message) {
        return "puhh was das macht ist keinem klar. Meist wird der String umgedreht";
    }

}

package keksdose.fwkib.modules.commands.Util;

import keksdose.fwkib.modules.Command;

public class EmptyCommand implements Command {

    @Override
    public String apply(String message) {
        return "";
    }

}

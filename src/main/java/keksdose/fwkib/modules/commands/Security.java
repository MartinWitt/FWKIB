package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;

public class Security implements Command {

    private String securityFeatures = "use:uuid rsagen-pub rsagen-pri pwgen and hash";

    @Override
    public String apply(String message) {
        return securityFeatures;
    }
}
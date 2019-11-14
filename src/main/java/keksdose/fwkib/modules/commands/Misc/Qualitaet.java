package keksdose.fwkib.modules.commands.Misc;

import keksdose.fwkib.modules.Command;

public class Qualitaet implements Command {

    private String linkToDiagramm = "http://prntscr.com/n7twun";

    @Override
    public String apply(String message) {
        return linkToDiagramm;
    }

    @Override
    public String help(String message) {
        return "Äh ja vllt morgen oder so";
    }

}

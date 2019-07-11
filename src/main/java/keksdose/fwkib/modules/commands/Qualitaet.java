package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;

public class Qualitaet implements Command {

    private String linkToDiagramm = "http://prntscr.com/n7twun";

    @Override
    public String apply(String message) {
        return linkToDiagramm;
    }
}
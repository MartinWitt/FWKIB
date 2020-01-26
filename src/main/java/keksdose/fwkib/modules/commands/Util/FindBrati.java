package keksdose.fwkib.modules.commands.util;

import keksdose.fwkib.modules.Command;

public class FindBrati implements Command {

    public static String nick = "brati";


    @Override
    public String

            apply(String message) {
        return nick;
    }



    @Override
    public String

            help(String message) {
        return "manchmal probiert der alte verwirrte Mann zu fliehen. Fwkib findet ihn (selten)";
    }
}

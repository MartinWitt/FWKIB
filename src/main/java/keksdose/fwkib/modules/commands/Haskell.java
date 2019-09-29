package keksdose.fwkib.modules.commands;

import java.util.Scanner;
import keksdose.fwkib.modules.Command;

public class Haskell implements Command {

    @Override
    public String apply(String message) {
        return "";
        /*
         * try { String haskellString = message.length() > 8 ? message.substring(8) : ""; if
         * (haskellString.isEmpty()) { return ""; }
         * 
         * String[] args = { "mueval", "-E", "-e", haskellString }; Process p =
         * Runtime.getRuntime().exec(args); // SequenceInputStream test = new
         * SequenceInputStream(p.getErrorStream(), // p.getInputStream()); Scanner s = new
         * Scanner(p.getInputStream()).useDelimiter("\\A"); String output = s.hasNext() ? s.next() :
         * ""; s.close();
         * 
         * return (output); } catch (Exception e) { } return "";
         */
    }

    @Override
    public String help(String message) {
        return "sudo dnf kill fedora";
    }

}

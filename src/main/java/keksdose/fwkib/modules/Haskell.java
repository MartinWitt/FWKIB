package keksdose.fwkib.modules;

import java.io.SequenceInputStream;
import java.util.Scanner;

public class Haskell implements Command {

    @Override
    public String apply(String message) {
        try {
            String haskellString = message.length() > 8 ? message.substring(8) : "";
            if (haskellString.isEmpty()) {
                return "";
            }

            String[] args = { "mueval", "-E", "-e", haskellString };
            Process p = Runtime.getRuntime().exec(args);
            // SequenceInputStream test = new SequenceInputStream(p.getErrorStream(),
            // p.getInputStream());
            Scanner s = new Scanner(p.getInputStream()).useDelimiter("\\A");
            String output = s.hasNext() ? s.next() : "";
            s.close();

            return (output);
        } catch (Exception e) {
        }
        return "";

    }

}
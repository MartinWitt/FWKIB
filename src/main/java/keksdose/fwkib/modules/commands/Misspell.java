package keksdose.fwkib.modules.commands;

import java.util.List;

import keksdose.fwkib.modules.Command;

import com.google.common.base.Splitter;
import keksdose.fwkib.mongo.MongoDB;

public class Misspell implements Command {

    @Override
    public String apply(String message) {

        List<String> splitter = Splitter.on(" ").omitEmptyStrings().limit(3).splitToList(message);
        String wordWrong = splitter.size() == 3 ? splitter.get(0).trim() : "";
        String wordCorrect = splitter.size() == 3 ? splitter.get(1).trim() : "";
        String wordRemember = splitter.size() == 3 ? splitter.get(2).trim() : "";

        if (splitter.size() == 1) {

            String var = new MongoDB().getMistake(splitter.get(0));

            if (!var.equalsIgnoreCase("")) {
                return var;
            }
        }
        System.out.println(message);
        if (wordWrong.isEmpty() || wordCorrect.isEmpty() || wordRemember.isEmpty()) {
            return "furchtbar du monster";
        }

        String var = "\"" + wordWrong + "\"" + " schreibt sich eigentlich " + "\"" + wordCorrect + "\""
                + ", du kannst es dir merken mit " + "\"" + wordCorrect + "\"" + " wie " + "\"" + wordRemember + "\".";

        new MongoDB().insertMistake(wordWrong, wordCorrect, wordRemember);

        return var;
    }

}
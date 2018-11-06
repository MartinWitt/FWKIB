package keksdose.fwkib.modules;

import java.util.List;

import com.google.common.base.Splitter;

import keksdose.fwkib.quiz.DB.MongoDB;

public class Missspell implements Command {

    @Override
    public String apply(String message) {

        List<String> splitter = Splitter.on(" ").omitEmptyStrings().splitToList(message);
        String wordWrong = splitter.size() == 4 ? splitter.get(1).trim() : "";
        String wordCorrect = splitter.size() == 4 ? splitter.get(2).trim() : "";
        String wordRemember = splitter.size() == 4 ? splitter.get(3).trim() : "";

        if (splitter.size() == 2) {

            String var = new MongoDB().getMistake(splitter.get(1));

            if (!var.equalsIgnoreCase("null")) {

                return var;
            }
        }
        if (wordWrong.isEmpty() || wordCorrect.isEmpty() || wordRemember.isEmpty()) {
            return "furchtbar du monster";
        }

        String var = "\"" + wordWrong + "\"" + " schreibt sich eigentlich " + "\"" + wordCorrect + "\""
                + ", du kannst es dir merken mit " + "\"" + wordCorrect + "\"" + " wie " + "\"" + wordRemember + "\" .";

        saveMisstake(var, wordWrong);

        return var;
    }

    private void saveMisstake(String var, String wordWrong) {
        new MongoDB().insertMistake(var, wordWrong);
    }

}
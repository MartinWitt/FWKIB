package keksdose.fwkib.modules;

import java.util.List;

import com.google.common.base.Splitter;

public class Missspell implements Command {

    @Override
    public String apply(String message) {
        List<String> splitter = Splitter.on(" ").omitEmptyStrings().splitToList(message);
        String wordWrong = splitter.size() == 4 ? splitter.get(1).trim() : "";
        String wordCorrect = splitter.size() == 4 ? splitter.get(2).trim() : "";
        String wordRemember = splitter.size() == 4 ? splitter.get(3).trim() : "";
        if (wordWrong.isBlank() || wordCorrect.isBlank() || wordRemember.isBlank()) {
            return "furchtbar du monster";
        }
        String var = "\"" + wordWrong + "\"" + " schreibt sich eigentlich " + "\"" + wordCorrect + "\""
                + " , du kannst es dir merken mit " + "\"" + wordCorrect + "\"" + " wie " + "\"" + wordRemember
                + "\" .";

        return var;
    }

}
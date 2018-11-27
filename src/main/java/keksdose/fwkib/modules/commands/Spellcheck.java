
package keksdose.fwkib.modules.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.quiz.DB.MongoDB;

public class Spellcheck implements Command {

    @Override
    public String apply(String message) {
        List<String> splitter = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(message);
        if (splitter.size() == 1) {
            return "";
        }
        MongoDB db = new MongoDB();
        splitter.remove(splitter.get(0));
        List<String> returnvalue = new ArrayList<>();
        for (String var : splitter) {
            String toTest = db.getCorrectWord(var);
            if (toTest.isEmpty()) {
                returnvalue.add(var);
            } else {
                returnvalue.add(toTest);
            }
        }
        return String.join(" ", returnvalue);
    }

}
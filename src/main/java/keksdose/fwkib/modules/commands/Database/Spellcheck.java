
package keksdose.fwkib.modules.commands.Database;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class Spellcheck implements Command {

    @Override
    public String apply(String message) {
        System.out.println(message);
        List<String> splitter =
                Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(message);
        System.out.println(splitter.toString());

        if (splitter.size() == 0) {
            return "";
        }
        MongoDB db = MongoDB.MongoDB;
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

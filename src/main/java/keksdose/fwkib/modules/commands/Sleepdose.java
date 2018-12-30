package keksdose.fwkib.modules.commands;

import java.util.List;
import com.google.common.base.Splitter;
import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class Sleepdose implements Command {

    @Override
    public String apply(String message) {
        List<String> list = Splitter.on("#sleepdose").trimResults().omitEmptyStrings().splitToList(message);
        if (list.size() == 0) {
            return String.valueOf(new MongoDB().getKeksdose());
        } else {
            return String.valueOf(new MongoDB().getkeksdose(list.get(0)));
        }
    }
}

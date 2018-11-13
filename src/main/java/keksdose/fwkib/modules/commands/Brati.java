package keksdose.fwkib.modules.commands;

import java.util.List;

import com.google.common.base.Splitter;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.quiz.DB.MongoDB;

public class Brati implements Command {

    @Override
    public String apply(String message) {
        if (message.startsWith("#rage")) {
            List<String> list = Splitter.on("#rage").trimResults().omitEmptyStrings().splitToList(message);
            if (list.size() == 0) {
                return String.valueOf(new MongoDB().getBrati());
            } else {
                return String.valueOf(new MongoDB().getBrati(list.get(0)));
            }
        } else {
            List<String> list = Splitter.on("#brati").trimResults().omitEmptyStrings().splitToList(message);
            if (list.size() == 0) {
                return String.valueOf(new MongoDB().getBrati());
            } else {
                return String.valueOf(new MongoDB().getBrati(list.get(0)));
            }

        }

    }
}
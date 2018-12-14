package keksdose.fwkib.modules.commands;

import java.util.List;
import keksdose.fwkib.modules.Command;

import com.google.common.base.Splitter;

import keksdose.fwkib.mongo.MongoDB;

public class QuizStats implements Command {

    @Override
    public String apply(String message) {
        List<String> splitter = Splitter.on("#stats").splitToList(message);
        String username = splitter.size() == 2 ? splitter.get(1).trim() : "";
        if (username.isEmpty()) {
            return new MongoDB().getStats();
        } else {
            return new MongoDB().getStats(username);

        }
    }
}
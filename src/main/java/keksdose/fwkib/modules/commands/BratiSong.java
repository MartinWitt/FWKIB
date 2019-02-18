package keksdose.fwkib.modules.commands;

import java.util.List;
import com.google.common.base.Splitter;
import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class BratiSong implements Command {

    @Override
    public String apply(String message) {
        if (message.trim().isEmpty()) {
            return String.valueOf(new MongoDB().getBratiSong(""));
        } else {
            return String.valueOf(new MongoDB().getBratiSong(message));
        }
    }
}

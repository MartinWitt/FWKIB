package keksdose.fwkib.modules.commands.Database;

import keksdose.fwkib.mongo.MongoDB;

public class BratiSongInsert {

    public String apply(String message, String user) {
        message.replaceAll("~", "");
        message.trim();
        MongoDB.MongoDB.insertBratiSong(message, user);

        return "";
    }
}

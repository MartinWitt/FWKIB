package keksdose.fwkib.modules;

import keksdose.fwkib.mongo.MongoDB;

public class BratiSongInsert {

    public String apply(String message, String user) {
        message.replaceAll("~", "");
        message.trim();
        new MongoDB().insertBratiSong(message, user);

        return "";
    }
}
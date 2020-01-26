package keksdose.fwkib.modules.commands.database;

import keksdose.fwkib.mongo.MongoDB;

public class BratiSongInsert {

  public String apply(String message, String user) {
    message.replaceAll("~", "");
    message.trim();
    MongoDB.MongoDB.insertBratiSong(message, user);

    return "";
  }
}

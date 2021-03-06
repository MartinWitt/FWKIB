package keksdose.fwkib.modules.commands.database;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class BratiSong implements Command {

  @Override
  public String apply(String message) {
    System.out.println(message);
    if (message.trim().isEmpty()) {
      return String.valueOf(MongoDB.MongoDB.getBratiSong(""));
    } else {
      return String.valueOf(MongoDB.MongoDB.getBratiSong(message));
    }
  }

  @Override
  public String help(String message) {
    return "irgendwelche komischen Lieder von Brati. Nutzung #bratiSong $regex";
  }

}

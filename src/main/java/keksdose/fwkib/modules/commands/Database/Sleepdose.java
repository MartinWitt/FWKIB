package keksdose.fwkib.modules.commands.database;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class Sleepdose implements Command {

  @Override
  public String apply(String message) {
    if (message.isBlank()) {
      return String.valueOf(MongoDB.MongoDB.getKeksdose());
    } else {
      return String.valueOf(MongoDB.MongoDB.getkeksdose(message));
    }
  }

  @Override
  public String help(String message) {
    return "irgendwelche komischen Sätze von Sleepdose. Davon ist KEINER wahr. Nutzung #sleepdose $regex";
  }
}

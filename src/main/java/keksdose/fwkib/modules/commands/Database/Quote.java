package keksdose.fwkib.modules.commands.database;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class Quote implements Command {


  @Override
  public String

      apply(String message) {
    if (message.trim().isEmpty()) {
      return String.valueOf(MongoDB.MongoDB.getQuote());
    } else {
      return String.valueOf(MongoDB.MongoDB.getQuote(message));
    }

  }



  @Override
  public String

      help(String message) {
    return "Gute Aussagen von Leuten, die nur der Wahrheit entsprechen und nichts anders sind werden damit ausgegeben. Nutzung: #quote bzw. #quote $regex ";
  }

}

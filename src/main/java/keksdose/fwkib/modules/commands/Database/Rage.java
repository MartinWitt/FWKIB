package keksdose.fwkib.modules.commands.database;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class Rage implements Command {

  @Override
  public String apply(String message) {
    if (message.trim().isEmpty()) {
      return String.valueOf(MongoDB.MongoDB.getRage());
    } else {
      return String.valueOf(MongoDB.MongoDB.getRage(message));
    }
  }

  @Override
  public String help(String message) {
    return "ALTER MANN WILL UT ÄÄHH DOTO SPIELEN. DOOOOTTOOOOO SPIELEN!!!";
  }

}

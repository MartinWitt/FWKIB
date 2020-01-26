
package keksdose.fwkib.modules.commands.database;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;

public class Spelluncheck implements Command {


  @Override
  public String

      apply(String message) {
    List<String> splitter = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(message);
    if (splitter.size() == 0) {
      return "";
    }
    MongoDB db = MongoDB.MongoDB;
    List<String> returnvalue = new ArrayList<>();
    for (String var : splitter) {

      String toTest = db.getWrongWord(var);
      if (toTest.isEmpty()) {
        returnvalue.add(var);
      } else {
        returnvalue.add(toTest);
      }
    }
    return String.join(" ", returnvalue);
  }

}

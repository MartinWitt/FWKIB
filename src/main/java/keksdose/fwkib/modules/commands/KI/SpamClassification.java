package keksdose.fwkib.modules.commands.KI;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.modules.TensorLock;
import xyz.keksdose.fwkib.spamclassifier.exposed.LookUp;

public class SpamClassification implements Command {

  @Override
  public String apply(String message) {
    TensorLock.getLock();
    String s = LookUp.lookUpSentence(message);
    TensorLock.releaseLock();
    return s;
  }

  @Override
  public String help(String message) {
    return "überprüft ob eine Nachricht purer spam ist";
  }


}

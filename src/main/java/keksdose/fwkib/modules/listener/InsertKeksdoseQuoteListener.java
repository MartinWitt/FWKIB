package keksdose.fwkib.modules.listener;

import keksdose.fwkib.modules.eventbus.ListenerManager;
import keksdose.fwkib.mongo.MongoDB;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and inserts quotes if needed.
 */
public class InsertKeksdoseQuoteListener extends AbstractListener {


  public boolean handle(final Message message) {
    if (message.getHostName().equals("195.201.137.123")) {
      if (!(message.getContent().startsWith(">") || message.getContent().startsWith("\""))) {
        MongoDB.MongoDB.insertKeksdose(message.getContent());
      }
    }
    return false;
  }



}

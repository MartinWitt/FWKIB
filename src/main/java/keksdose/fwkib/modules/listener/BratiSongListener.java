package keksdose.fwkib.modules.listener;

import keksdose.fwkib.modules.commands.database.BratiSongInsert;
import keksdose.fwkib.modules.eventbus.MessageConsumer;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and inserts quotes if needed.
 */
@MessageConsumer()

public class BratiSongListener extends AbstractListener {

  @Override
  public boolean handle(final Message message) {
    if (message.getContent().trim().startsWith("~") && message.getNick().contains("brati")
        && message.getContent().trim().endsWith("~")) {
      new BratiSongInsert().apply(message.getContent().replaceAll("~", ""), message.getNick());
      return true;
    }
    return false;
  }
}


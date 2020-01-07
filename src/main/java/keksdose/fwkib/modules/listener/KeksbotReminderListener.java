package keksdose.fwkib.modules.listener;

import keksdose.fwkib.modules.ReminderKeksdose;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and inserts quotes if needed.
 */
public class KeksbotReminderListener extends AbstractListener {

  @Override
  public void handle(final Message message) {
    if (message.getContent().toLowerCase().startsWith("keksbot,")
        || message.getContent().toLowerCase().startsWith("keksbot:")) {
      message.answer(new ReminderKeksdose().apply(message.getContent()));
    }
  }
}


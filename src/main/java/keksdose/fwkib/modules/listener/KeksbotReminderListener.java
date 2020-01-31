package keksdose.fwkib.modules.listener;

import keksdose.fwkib.modules.ReminderKeksdose;
import keksdose.fwkib.modules.eventbus.MessageConsumer;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and inserts quotes if needed.
 */
@MessageConsumer()

public class KeksbotReminderListener extends AbstractListener {


  @Override
  public boolean handle(final Message message) {
    if (message.getContent().toLowerCase().startsWith("keksbot,")
        || message.getContent().toLowerCase().startsWith("keksbot:")) {
      message.answer(new ReminderKeksdose().apply(message.getContent()));
    }
    return false;
  }
}


package keksdose.fwkib.modules.listener;

import keksdose.fwkib.modules.commands.Util.BrotiQuiz;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and inserts quotes if needed.
 */
public class BrotiQuizListener extends AbstractListener {

  @Override
  public void handle(final Message message) {
    if (message.getContent().contains("secs") && message.getNick().equals("fakebroti")
        || message.getNick().equals("fwkib") && message.getContent().contains("zeit: ")) {
      new BrotiQuiz().apply(message);
    }
  }
}


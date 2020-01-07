package keksdose.fwkib.modules.listener;

import keksdose.fwkib.mongo.MongoDB;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and inserts quotes if needed.
 */
public class AddQuoteListener extends AbstractListener {

  @Override
  public void handle(final Message message) {
    if (message.getContent().toLowerCase().startsWith("#addQuote")) {
      message.answer(MongoDB.MongoDB.insertQuote(
          message.getContent().replaceFirst("(?i)#addQuote", "").trim(), message.getNick()));
    }
  }



}

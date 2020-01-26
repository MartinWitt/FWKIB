package keksdose.fwkib.modules.listener;

import keksdose.fwkib.bot.ConfigReader;
import keksdose.fwkib.modules.eventbus.ListenerManager;
import keksdose.fwkib.mongo.MongoDB;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and handles #addBrati messages.
 */
public class AddBratiQuoteListener extends AbstractListener {


  @Override
  public boolean handle(final Message message) {
    final String inserter = ConfigReader.getAdmins()
        .stream()
        .filter(v -> v.contains(message.getHostName()))
        .findAny()
        .orElse("");
    if (!inserter.isBlank() && message.getContent().toLowerCase().startsWith("#addbrati")) {
      message.answer(
          MongoDB.MongoDB.insertBrati(message.getContent().replaceFirst("(?i)#addBrati", "").trim(),
              false, ConfigReader.convertHostnameToString(inserter)));
      return true;
    }
    return false;
  }

}

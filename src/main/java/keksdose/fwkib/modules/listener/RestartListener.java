package keksdose.fwkib.modules.listener;

import java.io.IOException;
import java.util.logging.Logger;
import keksdose.fwkib.bot.ConfigReader;
import keksdose.fwkib.modules.eventbus.ListenerManager;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and checks if a restart is needed.
 */
public class RestartListener extends AbstractListener {


  @Override
  public boolean handle(final Message message) {
    if (ConfigReader.getAdmins().contains(message.getHostName())
        && message.getContent().startsWith("#restart")) {
      try {
        new ProcessBuilder("./restart.sh").start();
      } catch (final IOException e) {
        Logger.getLogger("fwkib").warning(e.getLocalizedMessage());
      }
    }
    return false;
  }

}

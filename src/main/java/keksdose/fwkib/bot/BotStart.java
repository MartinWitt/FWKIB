package keksdose.fwkib.bot;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import keksdose.fwkib.modules.commands.util.Version;
import keksdose.keksirc.IRCStart;
import keksdose.keksirc.message.Message;

public class BotStart implements Runnable {
  private String[] channels;

  public BotStart(String... channels) {
    this.channels = channels;
    initStartTime();
  }

  private void initStartTime() {
    // sorgt für laden der Klasse
    new Version();
  }

  @Override
  public void run() {
    ArrayBlockingQueue<Message> container = new ArrayBlockingQueue<>(100);
    IRCStart start = new IRCStart(container);
    start.setUseCapHandler(true);
    Arrays.stream(channels).forEach(start::addChannel);
    IRCStart.setNickname("fwkib");
    Executors.newSingleThreadExecutor().execute(() -> new FWKIB(container));

    try {
      start.start();
    } catch (IOException e) {
      Logger.getLogger("fwkib").warning(e.getLocalizedMessage());
    }
  }

}

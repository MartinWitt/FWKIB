package keksdose.fwkib.modules.eventbus;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.logging.Logger;
import com.google.common.reflect.ClassPath;
import keksdose.fwkib.modules.listener.AddBratiQuoteListener;
import keksdose.fwkib.modules.listener.AddQuoteListener;
import keksdose.fwkib.modules.listener.BratiSongListener;
import keksdose.fwkib.modules.listener.BrotiQuizListener;
import keksdose.fwkib.modules.listener.InsertKeksdoseQuoteListener;
import keksdose.fwkib.modules.listener.KeksbotReminderListener;
import keksdose.fwkib.modules.listener.Listener;
import keksdose.fwkib.modules.listener.RestartListener;
import keksdose.fwkib.modules.listener.UrlListener;
import keksdose.fwkib.modules.listener.YoutubeListener;

public class ListenerManager {
  private static Collection<Listener> listener = new HashSet<>();

  public ListenerManager() {
    ListenerManager.register(new AddBratiQuoteListener());
    ListenerManager.register(new AddQuoteListener());
    ListenerManager.register(new BratiSongListener());
    ListenerManager.register(new BrotiQuizListener());
    ListenerManager.register(new InsertKeksdoseQuoteListener());
    ListenerManager.register(new KeksbotReminderListener());
    ListenerManager.register(new RestartListener());
    ListenerManager.register(new UrlListener());
    ListenerManager.register(new YoutubeListener());
  }

  public boolean handle(Event event) {
    try {
      if (Objects.nonNull(event.getIrcMessage())) {
        return listener.stream()
            .map(listener -> listener.handle(event.getIrcMessage()))
            .reduce(Boolean::logicalOr)
            .orElse(false);
      }
    } catch (Exception e) {
      Logger.getLogger("fwkib").info(e.getLocalizedMessage());
    }
    return false;

  }

  public static boolean register(Listener subscriber) {
    return Objects.isNull(subscriber) ? false : listener.add(subscriber);
  }
}

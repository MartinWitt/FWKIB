package keksdose.fwkib.modules.eventbus;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import keksdose.fwkib.modules.listener.Listener;

public class ListenerManager {
  private static Collection<Listener> listener = new HashSet<>();

  public ListenerManager() {

  }

  public void handle(Event event) {
    if (Objects.nonNull(event.getIrcMessage())) {
      listener.forEach(listener -> listener.handle(event.getIrcMessage()));
    }

  }

  public static boolean register(Listener subscriber) {
    return Objects.nonNull(subscriber) ? false : listener.add(subscriber);
  }
}

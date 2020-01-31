package keksdose.fwkib.modules.eventbus;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.logging.Logger;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import keksdose.fwkib.modules.CommandLoader;
import keksdose.fwkib.modules.listener.AbstractListener;
import keksdose.fwkib.modules.listener.Listener;

public class ListenerManager {
  private static Collection<Listener> listener = new HashSet<>();

  public ListenerManager() {

    try {
      createListener();
    } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException
        | IllegalAccessException | InvocationTargetException | IOException e) {
      Logger.getLogger("fwkib").info(e.getMessage());
    }
  }

  private void createListener() throws IOException, NoSuchMethodException, ClassNotFoundException,
      InstantiationException, IllegalAccessException, InvocationTargetException {
    for (ClassInfo clazz : ClassPath.from(ClassLoader.getSystemClassLoader())
        .getTopLevelClassesRecursive("keksdose.fwkib")) {
      if (Class.forName(clazz.getName()).getDeclaredAnnotation(MessageConsumer.class) != null) {
        Constructor<?> constructor = Class.forName(clazz.getName()).getDeclaredConstructor();
        AbstractListener createListener = (AbstractListener) constructor.newInstance();
        listener.add(createListener);
      }
    }
    Logger.getLogger("fwkib").info("listener init abgeschlossen. Anzahl: " + listener.size());
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

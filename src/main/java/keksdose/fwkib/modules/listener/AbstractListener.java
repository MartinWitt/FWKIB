package keksdose.fwkib.modules.listener;

import keksdose.fwkib.modules.eventbus.ListenerManager;
import keksdose.keksirc.message.Message;

public abstract class AbstractListener implements Listener {


  @Override
  public void register() {
    ListenerManager.register(this);
  }

  @Override
  public boolean handle(final Message message) {
    return false;
    // default noop
  }


}

package keksdose.fwkib.modules.listener;

import keksdose.keksirc.message.Message;

public interface Listener {


  public void register();

  public boolean handle(Message message);
}

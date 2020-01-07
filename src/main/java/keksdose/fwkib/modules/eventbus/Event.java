package keksdose.fwkib.modules.eventbus;

import keksdose.keksirc.message.Message;

public class Event {

  private Message ircMessage;

  /**
   * @return the ircMessage
   */
  public Message getIrcMessage() {
    return ircMessage;
  }

  /**
   * @param ircMessage
   */
  public Event(Message ircMessage) {
    this.ircMessage = ircMessage;
  }


}

package keksdose.fwkib.modules.listener;

import java.util.EnumSet;
import java.util.Iterator;
import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;
import keksdose.fwkib.modules.commands.ki.OCR;
import keksdose.fwkib.modules.eventbus.ListenerManager;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and tracks last url.
 */
public class UrlListener extends AbstractListener {



  @Override
  public boolean handle(final Message message) {
    LinkExtractor linkExtractor =
        LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW)).build();
    Iterable<LinkSpan> links = linkExtractor.extractLinks(message.getContent());
    Iterator<LinkSpan> it = links.iterator();
    if (it.hasNext()) {
      LinkSpan link = it.next();
      OCR.LAST_URL = message.getContent().substring(link.getBeginIndex(), link.getEndIndex());
      System.out.println("link gesetzt: " + OCR.LAST_URL);
    }
    return false;
  }

}

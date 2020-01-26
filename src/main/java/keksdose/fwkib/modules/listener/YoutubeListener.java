package keksdose.fwkib.modules.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import keksdose.fwkib.modules.commands.misc.Youtube;
import keksdose.fwkib.modules.eventbus.Registerable;
import keksdose.keksirc.message.Message;

/**
 * Reads all incoming messages and checks if a restart is needed.
 */
@Registerable()

public class YoutubeListener extends AbstractListener {
  private static final Pattern compiledPattern =
      Pattern.compile("(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*");



  @Override
  public boolean handle(final Message message) {
    Matcher matcher = compiledPattern.matcher(message.getContent());
    if (matcher.find()) {
      System.out.println(matcher.group(0).split(" ")[0]);
      message.answer(new Youtube()
          .apply("https://www.youtube.com/watch?v=" + matcher.group(0).split(" ")[0].trim()));
    }
    return false;
  }

}

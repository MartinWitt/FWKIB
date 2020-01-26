package keksdose.fwkib.modules.commands.misc;

import java.net.URL;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.mongo.MongoDB;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import edu.stanford.nlp.util.StringUtils;


public class Youtube implements Command {

  private final char NBSP = '\u200B';

  @Override
  public String apply(String message) {
    try {

      String title = message;
      if (StringUtils.isNumeric(message)) {
        return NBSP + MongoDB.MongoDB.getYtLink(Integer.parseInt(title));
      }
      if (title != null) {
        URL embededURL = new URL("http://www.youtube.com/oembed?url=" + title + "&format=json");
        String var = new JSONObject(IOUtils.toString(embededURL)).getString("title");
        MongoDB.MongoDB.insertLink(var + " Link: " + title);
        return NBSP + var;

      } else {
        return NBSP + MongoDB.MongoDB.getYtLink(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";

  }

  @Override
  public String help(String message) {
    return "was das exakt tut weiß keiner so genau aber passiv gibt das infos über yt Links";
  }
}

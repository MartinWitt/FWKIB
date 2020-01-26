package keksdose.fwkib.modules.commands.ki;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import keksdose.fwkib.modules.Command;

public class PrintScrOCr implements Command {
  private static Pattern printScrn = Pattern.compile("(https?:)//prntscr.com/[\\w]*");
  private static Pattern extract = Pattern.compile("https://image.prntscr.com/image/[^>]*.png");


  @Override
  public String

      apply(String message) {
    System.out.println(message);
    Matcher m = printScrn.matcher(message);

    if (m.find()) {
      try {

        URL url = new URL(m.group(0).replaceFirst("http:", "https:"));
        System.out.println(m.group(0));
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        String body = IOUtils.toString(in, encoding);
        Matcher matcher = extract.matcher(body);
        if (matcher.find()) {
          String s = matcher.group(0);
          return new OCR().apply(s);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return "bug?";
  }



  @Override
  public String

      help(String message) {
    return "$Texterkennung $Magie $Matrixrechnung $Spitze Pfeile(Vektoren). Nutzung #ocr $eingabelink.$Eingabelink muss ein PrntScr link sein";
  }

}

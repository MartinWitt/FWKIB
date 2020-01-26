package keksdose.fwkib.modules.commands.misc;

import java.io.IOException;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import keksdose.fwkib.modules.Command;

/**
 * Mensa
 */
public class Mensa implements Command {
  private static Map<String, Document> mensaByDate =
      Collections.synchronizedMap(new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Document> eldest) {
          return size() > 10;
        }
      });
  private static String LINK_MENSA = "https://mensa.akk.org/?DATUM=%s&uni=1&schnell=1";

  @Override
  public String apply(String message) {
    String link = formatDate(message);
    if (link.isBlank()) {
      return "da ging das parsen nicht. Du musst entweder heute,morgen,Montag oder dd.MM matchen";
    }
    if (mensaByDate.containsKey(link)) {
      return parseDoc(mensaByDate.get(link));
    }
    try {
      Document doc = Jsoup.connect(link).validateTLSCertificates(false).get();
      mensaByDate.put(link, doc);
      return parseDoc(doc);
    } catch (IOException e) {
      return "Da ging wohl was schief :(. Fehler: " + e.getLocalizedMessage();
    }
  }

  private String parseDoc(Document doc) {
    return doc.body()
        .select("td")
        .stream()
        .map(v -> v.text())
        .collect(Collectors.joining(" "))
        .trim();
  }

  private String formatDate(String message) {
    if (message.isBlank()) {
      return String.format(LINK_MENSA, KnownDates.TODAY.getDate());
    }
    Optional<KnownDates> date = getKnownDateWithName(message);
    if (date.isPresent()) {
      return String.format(LINK_MENSA, date.get().getDate());
    } else {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM", Locale.GERMAN)
          .withResolverStyle(ResolverStyle.SMART);
      try {
        var parsed = dateFormatter.parse(message);
        MonthDay md = MonthDay.from(parsed);
        return String.format(LINK_MENSA, String.format("%02d", md.getDayOfMonth()) + "."
            + String.format("%02d", md.getMonthValue()) + "." + LocalDate.now().getYear());

      } catch (DateTimeParseException e) {
        return "";
      }
    }
  }

  enum KnownDates {
    TODAY("heute"), TOMORROW("morgen"), MONTAG("Montag");

    private final String date;

    KnownDates(String date) {
      this.date = date;
    }

    /**
     * @return the date
     */
    public String getDate() {
      return date;
    }
  }

  public static Optional<KnownDates> getKnownDateWithName(String name) {
    return Arrays.stream(KnownDates.values())
        .filter(v -> v.getDate().equalsIgnoreCase(name))
        .findFirst();
  }
}



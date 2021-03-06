package keksdose.fwkib.modules.commands.misc;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import keksdose.fwkib.modules.Command;


/**
 * Mensa
 */
public class Mensa implements Command {
  private static Map<String, ExpiringDocument> mensaByDate =
      Collections.synchronizedMap(new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ExpiringDocument> eldest) {
          return size() > 4;
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
      if (mensaByDate.get(link).checkIfValid()) {
        return parseDoc(mensaByDate.get(link));
      } else {
        mensaByDate.remove(link);
      }
    }
    try {
      Document doc = Jsoup.connect(link).validateTLSCertificates(false).get();
      cacheLink(link, new ExpiringDocument(doc));
      return parseDoc(new ExpiringDocument(doc));
    } catch (IOException e) {
      return "Da ging wohl was schief :(. Fehler: " + e.getLocalizedMessage();
    }
  }

  private void cacheLink(String link, ExpiringDocument doc) {
    mensaByDate.put(link, doc);
  }

  private String parseDoc(ExpiringDocument doc) {
    return doc.getDoc()
        .body()
        .select("td")
        .stream()
        .map(v -> v.text())
        .collect(Collectors.joining(" "))
        .trim();
  }

  private String formatDate(String message) {
    if (message.isBlank()) {
      return String.format(LINK_MENSA, KnownDate.TODAY.getDate());
    }
    Optional<KnownDate> date = KnownDate.getKnownDateWithName(message);
    if (date.isPresent()) {
      return String.format(LINK_MENSA, date.get().getDate());
    } else {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM", Locale.GERMAN)
          .withResolverStyle(ResolverStyle.SMART);
      try {
        var parsed = dateFormatter.parse(message);
        MonthDay md = MonthDay.from(parsed);
        return String.format(LINK_MENSA,
            String.format("%02d", md.getDayOfMonth()) + "."
                + String.format("%02d", md.getMonthValue()) + "."
                + LocalDate.now(ZoneId.systemDefault()).getYear());

      } catch (DateTimeParseException e) {
        return e.getMessage();
      }
    }
  }

  enum KnownDate {
    TODAY("heute") {
      @Override
      public String getDate() {
        return formatDate(LocalDate.now(ZoneId.systemDefault()));
      }
    },
    TOMORROW("morgen") {
      @Override
      public String getDate() {
        return formatDate(LocalDate.now(ZoneId.systemDefault()).plusDays(1));
      }
    },
    MONTAG("Montag") {
      @Override
      public String getDate() {
        return formatDate(
            LocalDate.now(ZoneId.systemDefault()).with(TemporalAdjusters.next(DayOfWeek.MONDAY)));

      }
    };

    private final String date;

    KnownDate(String date) {
      this.date = date;
    }

    public abstract String getDate();

    private static String formatDate(LocalDate date) {
      return String.format("%02d", date.getDayOfMonth()) + "."
          + String.format("%02d", date.getMonthValue()) + "." + date.getYear();
    }

    public static Optional<KnownDate> getKnownDateWithName(String name) {
      return Arrays.stream(KnownDate.values())
          .filter(v -> v.date.equalsIgnoreCase(name))
          .findFirst();
    }
  }

  private static class ExpiringDocument {
    private static final long timeout = TimeUnit.HOURS.toSeconds(1);
    private final Document doc;
    private LocalDateTime expiringDate;

    public ExpiringDocument(Document doc) {
      this.doc = doc;
      this.expiringDate = LocalDateTime.now(ZoneId.systemDefault()).plusHours(timeout);
    }


    public boolean checkIfValid() {
      return LocalDateTime.now(ZoneId.systemDefault()).isBefore(expiringDate);
    }


    @Override
    public int hashCode() {
      return Objects.hash(doc);
    }


    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof ExpiringDocument)) {
        return false;
      }
      ExpiringDocument other = (ExpiringDocument) obj;
      return Objects.equals(doc, other.doc);
    }


    public Document getDoc() {
      return doc;
    }
  }
}



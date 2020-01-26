package keksdose.fwkib.modules;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Supplier;
import keksdose.fwkib.modules.commands.database.Brati;
import keksdose.fwkib.modules.commands.database.BratiSong;
import keksdose.fwkib.modules.commands.database.FlipRage;
import keksdose.fwkib.modules.commands.database.Misspell;
import keksdose.fwkib.modules.commands.database.Quote;
import keksdose.fwkib.modules.commands.database.Rage;
import keksdose.fwkib.modules.commands.database.Sleepdose;
import keksdose.fwkib.modules.commands.database.Spellcheck;
import keksdose.fwkib.modules.commands.database.Spelluncheck;
import keksdose.fwkib.modules.commands.ki.FastVectorDose;
import keksdose.fwkib.modules.commands.ki.MoodCheck;
import keksdose.fwkib.modules.commands.ki.NNDose;
import keksdose.fwkib.modules.commands.ki.OCR;
import keksdose.fwkib.modules.commands.ki.ParseDate;
import keksdose.fwkib.modules.commands.ki.PrintScrOCr;
import keksdose.fwkib.modules.commands.ki.RandomBrati;
import keksdose.fwkib.modules.commands.ki.SmartAllah;
import keksdose.fwkib.modules.commands.ki.SmartBrati;
import keksdose.fwkib.modules.commands.ki.SmartDose;
import keksdose.fwkib.modules.commands.ki.SmartMensa;
import keksdose.fwkib.modules.commands.ki.SmartViceTitle;
import keksdose.fwkib.modules.commands.ki.SpamClassification;
import keksdose.fwkib.modules.commands.ki.VectorDose;
import keksdose.fwkib.modules.commands.ki.WeebFilter;
import keksdose.fwkib.modules.commands.misc.Help;
import keksdose.fwkib.modules.commands.misc.Home;
import keksdose.fwkib.modules.commands.misc.JvmStats;
import keksdose.fwkib.modules.commands.misc.Mensa;
import keksdose.fwkib.modules.commands.misc.Qualitaet;
import keksdose.fwkib.modules.commands.misc.QuizStats;
import keksdose.fwkib.modules.commands.misc.Reverse;
import keksdose.fwkib.modules.commands.misc.Shuffle;
import keksdose.fwkib.modules.commands.misc.TvProgramm;
import keksdose.fwkib.modules.commands.misc.Youtube;
import keksdose.fwkib.modules.commands.security.Hash;
import keksdose.fwkib.modules.commands.security.Pwgen;
import keksdose.fwkib.modules.commands.security.RsaGenPri;
import keksdose.fwkib.modules.commands.security.RsaGenPub;
import keksdose.fwkib.modules.commands.security.SecureChoice;
import keksdose.fwkib.modules.commands.security.Security;
import keksdose.fwkib.modules.commands.security.Uuid;
import keksdose.fwkib.modules.commands.util.EmptyCommand;
import keksdose.fwkib.modules.commands.util.FindBrati;
import keksdose.fwkib.modules.commands.util.MongoStats;
import keksdose.fwkib.modules.commands.util.Satanist;
import keksdose.fwkib.modules.commands.util.Version;

public class ModuleSupplier {
  private static final NavigableMap<String, Supplier<Command>> COMMAND_SUPPLIER;
  private static final StringAlgorithmSupplier supplier = new StringAlgorithmSupplier();
  private static String state = "leven";

  static {
    final NavigableMap<String, Supplier<Command>> commands = new TreeMap<>();
    commands.put("#spellcheck", Spellcheck::new);
    commands.put("#spelluncheck", Spelluncheck::new);
    commands.put("#fehler", Misspell::new);
    commands.put("#smartdose", NNDose::new);
    commands.put("#markovdose", SmartDose::new);
    commands.put("#randombrati", RandomBrati::new);
    commands.put("#smartbrati", SmartBrati::new);
    commands.put("#smartmensa", SmartMensa::new);
    commands.put("#smarttitle", SmartViceTitle::new);
    commands.put("#ocr", OCR::new);
    commands.put("#yt", Youtube::new);
    commands.put("#tv", TvProgramm::new);
    commands.put("#stats", QuizStats::new);
    commands.put("#help", Help::new);
    commands.put("#mongo", MongoStats::new);
    commands.put("#security", Security::new);
    commands.put("#uuid", Uuid::new);
    commands.put("#rsagen-pub", RsaGenPub::new);
    commands.put("#rsagen-pri", RsaGenPri::new);
    commands.put("#pwgen", Pwgen::new);
    commands.put("#bratisong", BratiSong::new);
    commands.put("#sleepdose", Sleepdose::new);
    commands.put("#home", Home::new);
    commands.put("#hash", Hash::new);
    commands.put("#brati", Brati::new);
    commands.put("#rage", Rage::new);
    commands.put("#smartallah", SmartAllah::new);
    commands.put("#printscr", PrintScrOCr::new);
    commands.put("#fliprage", FlipRage::new);
    commands.put("#ramstats", JvmStats::new);
    commands.put("#dummdose", Sleepdose::new);
    commands.put("#dummbrati", SmartBrati::new);
    commands.put("#quote", Quote::new);
    commands.put("#mensa", Mensa::new);
    commands.put("#reverse", Reverse::new);
    commands.put("#shuffle", Shuffle::new);
    commands.put("#qualität", Qualitaet::new);
    commands.put("#weeb", WeebFilter::new);
    commands.put("#satanist", Satanist::new);
    commands.put("#findbrati", FindBrati::new);
    commands.put("#vectordose", VectorDose::new);
    commands.put("#fastvectordose", FastVectorDose::new);
    commands.put("#parseDate", ParseDate::new);
    commands.put("#version", Version::new);
    commands.put("#moodcheck", MoodCheck::new);
    commands.put("#securechoice", SecureChoice::new);
    commands.put("#isitspam", SpamClassification::new);
    COMMAND_SUPPLIER = Collections.unmodifiableNavigableMap(commands);
  }

  public Command supplyCommand(String commandString) {
    if (checkForChangeOfAlgo(commandString)) {
      return new EmptyCommand();
    }
    if (commandString.equalsIgnoreCase("#help")) {
      return COMMAND_SUPPLIER.get("#help").get();
    }
    String replaced = commandString.replace("#" + FindBrati.nick, "#brati");
    Command c =
        Objects.isNull(COMMAND_SUPPLIER.get(replaced))
            ? (COMMAND_SUPPLIER.get(COMMAND_SUPPLIER.keySet()
                .stream()
                .min((o1, o2) -> Double.compare((supplier.similarity(state, replaced, o1)),
                    (supplier.similarity(state, replaced, o2))))
                .get()).get())
            : COMMAND_SUPPLIER.get(replaced).get();
    if (c == null) {
      return new EmptyCommand();
    }
    return c;
  }

  private boolean checkForChangeOfAlgo(String commandString) {
    if (commandString.equals("#fuzzy")) {
      state = "fuzzy";
      System.out.println("Habe den Zustand zu : " + state + " gewechselt");
      return true;

    }
    if (commandString.equals("#leven")) {
      state = "leven";
      System.out.println("Habe den Zustand zu : " + state + " gewechselt");
      return true;

    }
    if (commandString.equals("#cosine")) {
      state = "cosine";
      System.out.println("Habe den Zustand zu : " + state + " gewechselt");
      return true;

    }
    if (commandString.equals("#jaro")) {
      state = "jaro";
      System.out.println("Habe den Zustand zu : " + state + " gewechselt");
      return true;
    }
    if (commandString.equals("#scrabble")) {
      state = "scrabble";
      System.out.println("Habe den Zustand zu : " + state + " gewechselt");
      return true;
    }
    return false;
  }
}

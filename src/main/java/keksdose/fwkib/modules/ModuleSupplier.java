package keksdose.fwkib.modules;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;
import keksdose.fwkib.modules.commands.Database.Brati;
import keksdose.fwkib.modules.commands.Database.BratiSong;
import keksdose.fwkib.modules.commands.Database.FlipRage;
import keksdose.fwkib.modules.commands.Database.Misspell;
import keksdose.fwkib.modules.commands.Database.Quote;
import keksdose.fwkib.modules.commands.Database.Rage;
import keksdose.fwkib.modules.commands.Database.Sleepdose;
import keksdose.fwkib.modules.commands.Database.Spellcheck;
import keksdose.fwkib.modules.commands.Database.Spelluncheck;
import keksdose.fwkib.modules.commands.KI.FastVectorDose;
import keksdose.fwkib.modules.commands.KI.MoodCheck;
import keksdose.fwkib.modules.commands.KI.NNDose;
import keksdose.fwkib.modules.commands.KI.OCR;
import keksdose.fwkib.modules.commands.KI.ParseDate;
import keksdose.fwkib.modules.commands.KI.PrintScrOCr;
import keksdose.fwkib.modules.commands.KI.RandomBrati;
import keksdose.fwkib.modules.commands.KI.SmartAllah;
import keksdose.fwkib.modules.commands.KI.SmartBrati;
import keksdose.fwkib.modules.commands.KI.SmartDose;
import keksdose.fwkib.modules.commands.KI.SmartMensa;
import keksdose.fwkib.modules.commands.KI.SmartViceTitle;
import keksdose.fwkib.modules.commands.KI.SpamClassification;
import keksdose.fwkib.modules.commands.KI.VectorDose;
import keksdose.fwkib.modules.commands.KI.WeebFilter;
import keksdose.fwkib.modules.commands.Misc.Help;
import keksdose.fwkib.modules.commands.Misc.Home;
import keksdose.fwkib.modules.commands.Misc.JvmStats;
import keksdose.fwkib.modules.commands.Misc.Qualitaet;
import keksdose.fwkib.modules.commands.Misc.QuizStats;
import keksdose.fwkib.modules.commands.Misc.Reverse;
import keksdose.fwkib.modules.commands.Misc.Shuffle;
import keksdose.fwkib.modules.commands.Misc.TvProgramm;
import keksdose.fwkib.modules.commands.Misc.Youtube;
import keksdose.fwkib.modules.commands.Security.Hash;
import keksdose.fwkib.modules.commands.Security.Pwgen;
import keksdose.fwkib.modules.commands.Security.RsaGenPri;
import keksdose.fwkib.modules.commands.Security.RsaGenPub;
import keksdose.fwkib.modules.commands.Security.SecureChoice;
import keksdose.fwkib.modules.commands.Security.Security;
import keksdose.fwkib.modules.commands.Security.Uuid;
import keksdose.fwkib.modules.commands.Util.EmptyCommand;
import keksdose.fwkib.modules.commands.Util.FindBrati;
import keksdose.fwkib.modules.commands.Util.MongoStats;
import keksdose.fwkib.modules.commands.Util.Satanist;
import keksdose.fwkib.modules.commands.Util.Version;

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
    commands.put("#mensa", SmartMensa::new);
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
    String s = COMMAND_SUPPLIER.keySet()
        .stream()
        .parallel()
        .min((o1, o2) -> Double.compare((supplier.similarity(state, replaced, o1)),
            (supplier.similarity(state, replaced, o2))))
        .get();
    System.out.println(s);
    Command c = COMMAND_SUPPLIER.get(s).get();

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
    if (commandString.equals("#random")) {
      state = "random";
      System.out.println("Habe den Zustand zu : " + state + " gewechselt");
      return true;
    }
    return false;
  }
}

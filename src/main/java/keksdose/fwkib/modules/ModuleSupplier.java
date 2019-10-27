package keksdose.fwkib.modules;

import java.util.Collections;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.LevenshteinDistance;
import keksdose.fwkib.modules.commands.Brati;
import keksdose.fwkib.modules.commands.BratiSong;
import keksdose.fwkib.modules.commands.EmptyCommand;
import keksdose.fwkib.modules.commands.FastVectorDose;
import keksdose.fwkib.modules.commands.FindBrati;
import keksdose.fwkib.modules.commands.FlipRage;
import keksdose.fwkib.modules.commands.Hash;
import keksdose.fwkib.modules.commands.Help;
import keksdose.fwkib.modules.commands.Home;
import keksdose.fwkib.modules.commands.JvmStats;
import keksdose.fwkib.modules.commands.Misspell;
import keksdose.fwkib.modules.commands.MongoStats;
import keksdose.fwkib.modules.commands.NNDose;
import keksdose.fwkib.modules.commands.OCR;
import keksdose.fwkib.modules.commands.ParseDate;
import keksdose.fwkib.modules.commands.PrintScrOCr;
import keksdose.fwkib.modules.commands.Pwgen;
import keksdose.fwkib.modules.commands.Qualitaet;
import keksdose.fwkib.modules.commands.QuizStats;
import keksdose.fwkib.modules.commands.Quote;
import keksdose.fwkib.modules.commands.Rage;
import keksdose.fwkib.modules.commands.RandomBrati;
import keksdose.fwkib.modules.commands.Reverse;
import keksdose.fwkib.modules.commands.RsaGenPri;
import keksdose.fwkib.modules.commands.RsaGenPub;
import keksdose.fwkib.modules.commands.Satanist;
import keksdose.fwkib.modules.commands.Security;
import keksdose.fwkib.modules.commands.Shuffle;
import keksdose.fwkib.modules.commands.Sleepdose;
import keksdose.fwkib.modules.commands.SmartAllah;
import keksdose.fwkib.modules.commands.SmartBrati;
import keksdose.fwkib.modules.commands.SmartDose;
import keksdose.fwkib.modules.commands.SmartMensa;
import keksdose.fwkib.modules.commands.SmartViceTitle;
import keksdose.fwkib.modules.commands.Spellcheck;
import keksdose.fwkib.modules.commands.Spelluncheck;
import keksdose.fwkib.modules.commands.TvProgramm;
import keksdose.fwkib.modules.commands.Uuid;
import keksdose.fwkib.modules.commands.VectorDose;
import keksdose.fwkib.modules.commands.Version;
import keksdose.fwkib.modules.commands.WeebFilter;
import keksdose.fwkib.modules.commands.Youtube;

public class ModuleSupplier {
   private static final NavigableMap<String, Supplier<Command>> COMMAND_SUPPLIER;
   private static final StringAlgorithmSupplier supplier = new StringAlgorithmSupplier();
   private String state = "leven";
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
      return false;
   }
}

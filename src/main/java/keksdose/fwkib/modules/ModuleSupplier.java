package keksdose.fwkib.modules;

import java.util.Collections;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Supplier;

import com.google.common.base.Strings;

import org.apache.commons.text.similarity.LevenshteinDistance;

import keksdose.fwkib.modules.commands.Brati;
import keksdose.fwkib.modules.commands.BratiSong;
import keksdose.fwkib.modules.commands.EmptyCommand;
import keksdose.fwkib.modules.commands.FlipRage;
import keksdose.fwkib.modules.commands.Hash;
import keksdose.fwkib.modules.commands.Help;
import keksdose.fwkib.modules.commands.Home;
import keksdose.fwkib.modules.commands.JvmStats;
import keksdose.fwkib.modules.commands.Misspell;
import keksdose.fwkib.modules.commands.MongoStats;
import keksdose.fwkib.modules.commands.NNDose;
import keksdose.fwkib.modules.commands.OCR;
import keksdose.fwkib.modules.commands.PrintScrOCr;
import keksdose.fwkib.modules.commands.Pwgen;
import keksdose.fwkib.modules.commands.QuizStats;
import keksdose.fwkib.modules.commands.Quote;
import keksdose.fwkib.modules.commands.Rage;
import keksdose.fwkib.modules.commands.RandomBrati;
import keksdose.fwkib.modules.commands.Revert;
import keksdose.fwkib.modules.commands.RsaGenPri;
import keksdose.fwkib.modules.commands.RsaGenPub;
import keksdose.fwkib.modules.commands.Security;
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
import keksdose.fwkib.modules.commands.Youtube;

public class ModuleSupplier {
   private static final NavigableMap<String, Supplier<Command>> COMMAND_SUPPLIER;

   static {
      final NavigableMap<String, Supplier<Command>> commands = new TreeMap<>(Comparator.comparing(Object::toString));
      commands.put("#tv-nau", TvProgramm::new);
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
      commands.put("#revert", Revert::new);

      COMMAND_SUPPLIER = Collections.unmodifiableNavigableMap(commands);
   }

   public Command supplyCommand(String commandString) {

      System.out.println("fange supply an");
      String above = Strings.nullToEmpty(COMMAND_SUPPLIER.ceilingKey(commandString.toLowerCase()));
      String below = Strings.nullToEmpty(COMMAND_SUPPLIER.floorKey(commandString.toLowerCase()));
      System.out.println(above);
      System.out.println(below);
      int distLow = LevenshteinDistance.getDefaultInstance().apply(commandString, below);
      int distHigh = LevenshteinDistance.getDefaultInstance().apply(commandString, above);
      Supplier<Command> command;
      if (distHigh > 3 && distLow > 3) {
         return new EmptyCommand();
      }
      command = (distLow > distHigh) ? COMMAND_SUPPLIER.get(above) : COMMAND_SUPPLIER.get(below);

      if (command == null) {
         return new EmptyCommand();
      }
      System.out.println(command.get().toString());
      return command.get();
   }
}

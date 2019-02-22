package keksdose.fwkib.modules;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import keksdose.fwkib.modules.commands.Brati;
import keksdose.fwkib.modules.commands.BratiSong;
import keksdose.fwkib.modules.commands.EmptyCommand;
import keksdose.fwkib.modules.commands.Hash;
import keksdose.fwkib.modules.commands.Help;
import keksdose.fwkib.modules.commands.Home;
import keksdose.fwkib.modules.commands.Misspell;
import keksdose.fwkib.modules.commands.MongoStats;
import keksdose.fwkib.modules.commands.NNDose;
import keksdose.fwkib.modules.commands.OCR;
import keksdose.fwkib.modules.commands.PrintScrOCr;
import keksdose.fwkib.modules.commands.Pwgen;
import keksdose.fwkib.modules.commands.QuizStats;
import keksdose.fwkib.modules.commands.RandomBrati;
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
   private static final Map<String, Supplier<Command>> COMMAND_SUPPLIER;

   static {
      final Map<String, Supplier<Command>> commands = new HashMap<>();
      commands.put("#tv-nau", TvProgramm::new);
      commands.put("#spellcheck", Spellcheck::new);
      commands.put("#spelluncheck", Spelluncheck::new);
      commands.put("#fehler", Misspell::new);
      commands.put("#smartDose", NNDose::new);
      commands.put("#markovDose", SmartDose::new);
      commands.put("#randomBrati", RandomBrati::new);
      commands.put("#smartBrati", SmartBrati::new);
      commands.put("#smartMensa", SmartMensa::new);
      commands.put("#smartTitle", SmartViceTitle::new);
      commands.put("#ocr", OCR::new);
      commands.put("#remove", Misspell::new);
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
      commands.put("#bratiSong", BratiSong::new);
      commands.put("#sleepdose", Sleepdose::new);
      commands.put("#home", Home::new);
      commands.put("#hash", Hash::new);
      commands.put("#brati", Brati::new);
      commands.put("#rage", Brati::new);
      commands.put("#smartAllah", SmartAllah::new);
      commands.put("#printscr", PrintScrOCr::new);

      COMMAND_SUPPLIER = Collections.unmodifiableMap(commands);
   }

   public Command supplyCommand(String commandString) {
      System.out.println("fange supply an");

      Supplier<Command> command = COMMAND_SUPPLIER.get(commandString);
      System.out.println(String.valueOf(command));
      if (command == null) {
         return new EmptyCommand();
      }
      System.out.println(command.toString());
      return command.get();
   }
}

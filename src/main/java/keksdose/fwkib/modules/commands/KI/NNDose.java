
package keksdose.fwkib.modules.commands.ki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.modules.TensorLock;

public class NNDose implements Command {

  @Override
  public String apply(String message) {

    try {
      TensorLock.getLock();
      String[] command = {"./smartDoseNNscript.sh"};
      ProcessBuilder builder;
      builder = new ProcessBuilder(command);
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(builder.start().getInputStream()));
      List<String> returnvalue = (reader.lines()).filter(v -> !v.isBlank())
          .map(v -> v.replaceAll("(\\t|\\r?\\n)+", ""))
          .map(v -> v.replaceAll("\"", ""))
          .map(v -> StringUtils.capitalize(v))
          .collect(Collectors.toList());
      TensorLock.releaseLock();
      return String.join(". ", returnvalue);

    } catch (IOException e) {
      TensorLock.releaseLock();
      return "da ist tf wohl exlodiert";
    }

  }

  @Override
  public String help(String message) {
    return "Komische Sätze deren Ursprung nicht ganz geklärt ist. Aber auf jedenfall ist der Ursprung kluk";
  }
}

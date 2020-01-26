
package keksdose.fwkib.modules.commands.ki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.modules.TensorLock;

public class RandomBrati implements Command {

  @Override
  public String apply(String message) {

    try {
      TensorLock.getLock();
      String[] command = {"./randomBratiNNscript.sh"};
      Process process;
      process = Runtime.getRuntime().exec(command);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String s;
      String returnvalue = "";
      while ((s = reader.readLine()) != null) {
        returnvalue += s;
      }
      TensorLock.releaseLock();
      return returnvalue.replaceAll("\n", "");
    } catch (IOException e) {
      TensorLock.releaseLock();
      e.printStackTrace();
    }

    return "";
  }

  @Override
  public String help(String message) {
    return "$Magie $Matrixrechnung $Spitze Pfeile(Vektoren). Falls brati fehlt kannst du damit kluge Sätze von ihm erzeugen.";
  }


}

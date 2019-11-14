
package keksdose.fwkib.modules.commands.KI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.modules.TensorLock;

public class SmartAllah implements Command {

    @Override
    public String apply(String message) {

        try {
            TensorLock.getLock();
            String[] command = {"./smartAllahNNscript.sh"};
            Process process;
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            String returnvalue = "";
            while ((s = reader.readLine()) != null) {
                if (s.equals("\n")) {
                    continue;
                }
                returnvalue = returnvalue.concat(
                        StringUtils.capitalize((s + ". ").replaceAll("(\\t|\\r?\\n)+", " ")));
                returnvalue = returnvalue.substring(0,
                        returnvalue.replaceAll("[^a-zA-Z0-9]*$", "").length() - 1);
            }
            TensorLock.releaseLock();
            return returnvalue;
        } catch (IOException e) {
            TensorLock.releaseLock();
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String help(String message) {
        return "$Magie $Matrixrechnung $Spitze Pfeile(Vektoren). Falls der Koran fehlt kannst du damit kluge Sätze daraus erzeugen.";
    }

}

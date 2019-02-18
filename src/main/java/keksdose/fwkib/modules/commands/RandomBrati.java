
package keksdose.fwkib.modules.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import keksdose.fwkib.modules.Command;

public class RandomBrati implements Command {

    @Override
    public String apply(String message) {

        try {
            String[] command = { "./randomBratiNNscript.sh" };
            Process process;
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            String returnvalue = "";
            while ((s = reader.readLine()) != null) {
                returnvalue += s;
            }
            return returnvalue.replaceAll("\n", "");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

}
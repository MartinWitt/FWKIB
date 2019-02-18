
package keksdose.fwkib.modules.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import keksdose.fwkib.modules.Command;

public class SmartBrati implements Command {

    @Override
    public String apply(String message) {

        try {
            String[] command = { "./smartBratiNNscript.sh" };
            Process process;
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            String returnvalue = "";
            while ((s = reader.readLine()) != null) {
                if (s.equals("\n")) {
                    continue;
                }
                returnvalue = returnvalue.concat(StringUtils.capitalize((s + ". ").replaceAll("(\\t|\\r?\\n)+", " ")));

            }
            return returnvalue;
            // .replaceAll("\n", "\\. ").replaceAll("\\?\\.", "\\?").replaceAll("!\\.",
            // "!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

}
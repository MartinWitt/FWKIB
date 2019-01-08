
package keksdose.fwkib.modules.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.apache.commons.lang3.StringUtils;

import keksdose.fwkib.modules.Command;

public class SmartBrati implements Command {

    @Override
    public String apply(String message) {
        String[] args = message.split(" ", 2);
        String prefix = "";
        if (args.length == 2) {
            prefix = args[1];
        }
        try {
            String[] command = { "./smartBratiNNscript.sh", prefix };
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
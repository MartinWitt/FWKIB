
package keksdose.fwkib.modules.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

import keksdose.fwkib.modules.Command;

public class NNDose implements Command {

    @Override
    public String apply(String message) {

        try {
            String[] command = { "./smartDoseNNscript.sh" };
            Process process;
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            String returnvalue = "";
            while ((s = reader.readLine()) != null) {
                returnvalue += StringUtils.capitalize(s);
            }
            return returnvalue.replaceAll("\n", "");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

}
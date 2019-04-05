
package keksdose.fwkib.modules.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import keksdose.fwkib.modules.Command;
import keksdose.fwkib.modules.TensorLock;

public class SmartMensa implements Command {

    @Override
    public String apply(String message) {

        try {
            TensorLock.getLock();
            String[] command = { "./smartMensaNNscript.sh" };
            ProcessBuilder builder;
            builder = new ProcessBuilder(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(builder.start().getInputStream()));
            List<String> returnvalue = (reader.lines()).filter(v -> !v.isBlank()).map(v -> v.replaceAll("\"", ""))
                    .collect(Collectors.toList());

            if (returnvalue.size() != 4) {
                System.out.println(returnvalue.toString());
                return "";

            }
            String date;
            if (StringUtils.isNumeric(message.trim())) {
                date = LocalDate.now().plusDays(Integer.parseInt(message.trim()))
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.GERMANY));
            } else {
                date = LocalDate.now()
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.GERMANY));
            }
            StringBuilder mensa = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#.0");
            Iterator<Double> doubleStream = new SecureRandom().doubles(2, 6).distinct().boxed().iterator();
            mensa.append(date + " ");
            mensa.append("Linie 1: " + returnvalue.get(0) + " " + df.format(doubleStream.next()) + "0 " + "\u20ac ");
            mensa.append("Linie 2: " + returnvalue.get(1) + " " + df.format(doubleStream.next()) + "0 " + "\u20ac ");
            mensa.append("Linie 3: " + returnvalue.get(2) + " " + df.format(doubleStream.next()) + "0 " + "\u20ac ");
            mensa.append("Linie 4/5: " + returnvalue.get(3) + " " + df.format(doubleStream.next()) + "0 " + "\u20ac ");
            TensorLock.releaseLock();
            return StringUtils.normalizeSpace(mensa.toString());

        } catch (IOException e) {
            TensorLock.releaseLock();
            e.printStackTrace();
        }

        return "";
    }

}
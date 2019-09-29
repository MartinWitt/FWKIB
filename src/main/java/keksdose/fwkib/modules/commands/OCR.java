
package keksdose.fwkib.modules.commands;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import keksdose.fwkib.modules.Command;

public class OCR implements Command {
    private static Pattern printScrn = Pattern.compile("http://prntscr.com/[\\w]*");

    @Override
    public String apply(String message) {
        Matcher m = printScrn.matcher(message);
        if (m.find()) {
            return new PrintScrOCr().apply(message);
        }
        try {
            int maxDownload = 1024 * 1024 * 2;
            URL website = new URL(message);

            byte[] outBytes = new byte[maxDownload];
            InputStream stream = website.openStream();

            IOUtils.read(stream, outBytes, 0, maxDownload);

            if (stream.read() != -1) {
                System.out.println("File too big");
            }
            IOUtils.write(outBytes, new FileOutputStream("200MB.jpg"));
            ProcessBuilder pb = new ProcessBuilder("tesseract", "200MB.jpg", "stdout", "-l",
                    "deu+eng", "--oem", "1");
            String output = IOUtils.toString(pb.start().getInputStream());
            output = output.replaceAll("\n", " ");
            output = StringUtils.substring(StringUtils.normalizeSpace(output), 0, 510);
            System.out.println(output);
            return output.isBlank() ? "nix erkannt ;_; " : output;
        } catch (FileNotFoundException e) {
            return "naja wohl mal nachfragen";
        } catch (IOException e) {
            return "naja wohl mal nachfragen";
        }

    }

    @Override
    public String help(String message) {
        return "$Texterkennung $Magie $Matrixrechnung $Spitze Pfeile(Vektoren). Nutzung #ocr $eingabelink.$Eingabelink muss zu einem Bild kleiner 200mb sein";
    }

}

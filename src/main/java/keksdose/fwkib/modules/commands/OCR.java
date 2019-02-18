
package keksdose.fwkib.modules.commands;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import keksdose.fwkib.modules.Command;

public class OCR implements Command {

    @Override
    public String apply(String message) {

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
            ProcessBuilder pb = new ProcessBuilder("tesseract", "200MB.jpg", "stdout", "-l", "deu+eng", "--oem", "1");
            String output = IOUtils.toString(pb.start().getInputStream());
            output = output.replaceAll("\n", " ");
            return output.isBlank() ? "nix erkannt ;_; " : output;
        } catch (FileNotFoundException e) {
            return "naja wohl mal nachfragen";
        } catch (IOException e) {
            return "naja wohl mal nachfragen";
        }

    }

}
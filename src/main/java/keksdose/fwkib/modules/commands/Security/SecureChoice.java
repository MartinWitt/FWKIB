package keksdose.fwkib.modules.commands.Security;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import keksdose.fwkib.modules.Command;

public class SecureChoice implements Command {

    @Override
    public String apply(String message) {
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
            byte[] values = new byte[4];
            secureRandom.nextBytes(values);
            int random = ByteBuffer.wrap(values).getInt();
            List<String> list = List.of(message.split(","));
            return list.size() == 0 ? "das war leer" : list.get(random % list.size());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            return "Es gab keinen guten Zufall";
        }
    }

    @Override
    public String help(String message) {
        return "Endlich kannst du sicher dein Mensaessen auswählen ohne kaputte Crypto.";
    }
}

package keksdose.fwkib.modules.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import keksdose.fwkib.modules.Command;

public class RsaGenPub implements Command {

    @Override
    public String apply(String message) {
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");

            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
            ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteOs);
            dos.writeInt("ssh-rsa".getBytes().length);
            dos.write("ssh-rsa".getBytes());
            dos.writeInt(rsaPublicKey.getPublicExponent().toByteArray().length);
            dos.write(rsaPublicKey.getPublicExponent().toByteArray());
            dos.writeInt(rsaPublicKey.getModulus().toByteArray().length);
            dos.write(rsaPublicKey.getModulus().toByteArray());
            String enc = Base64.getEncoder().encodeToString(byteOs.toByteArray());
            return "ssh-rsa " + enc + " " + "made by fwkib";
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return "wohl kein key für dich";
    }

    @Override
    public String help(String message) {
        return "Erzeugt dir einen public key für den login auf jedem Server, wo du willst. Der Key ist manchmal gültig, aber immer nur für DICH erstellt";
    }
}

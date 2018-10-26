package keksdose.fwkib.modules;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

import org.pircbotx.hooks.events.MessageEvent;

public class RsaGenPri implements Command {

    @Override
    public String apply(String message) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();

            PrivateKey priv = kp.getPrivate();
            byte[] privBytes = priv.getEncoded();
            return "-----BEGIN RSA PRIVATE KEY-----" + "\n" + Base64.getEncoder().encodeToString(privBytes) + "\n"
                    + "-----END RSA PRIVATE KEY-----";

        } catch (NoSuchAlgorithmException e) {

        }

        return " wohl kein key für dich";
    }
}
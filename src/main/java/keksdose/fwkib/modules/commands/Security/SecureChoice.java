package keksdose.fwkib.modules.commands.Security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import keksdose.fwkib.modules.Command;

public class SecureChoice implements Command {

    @Override
    public String apply(String message) {
        try {
            List<String> list = List.of(message.split(","));
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            return list.size() == 0 ? "das war leer"
                    : randomEnum(Phrases.class).insert(list.get(secureRandom.nextInt(list.size())));
        } catch (NoSuchAlgorithmException e) {
            return "Es gab keinen guten Zufall";
        }
    }

    @Override
    public String help(String message) {
        return "Endlich kannst du sicher dein Mensaessen auswählen ohne kaputte Crypto.";
    }

    private enum Phrases {

        WIE_WÄRE_ES("Wie wäre es mit ", "?"), MACH_DOCH("Mach doch ", "?"), ICH_WÜRDE("Ich würde ",
                " machen"), PROBIERS_MAL_MIT("Probiers mal mit ", ".");


        private String beforePhrase;
        private String afterPhrases;

        private Phrases(String beforePhrase, String afterPhrases) {
            this.beforePhrase = beforePhrase;
            this.afterPhrases = afterPhrases;
        }

        public String insert(String insertString) {
            return this.beforePhrase + insertString.trim() + this.afterPhrases;
        }
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        SecureRandom random = new SecureRandom();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}

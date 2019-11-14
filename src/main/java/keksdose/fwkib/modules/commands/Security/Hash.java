package keksdose.fwkib.modules.commands.Security;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import keksdose.fwkib.modules.Command;

public class Hash implements Command {

    @Override
    public String apply(String message) {
        HashFunction hf = Hashing.sha512();
        return hf.newHasher().putString(message, Charsets.UTF_8).hash().toString();
    }

    @Override
    public String help(String message) {
        return "Wer kennt es nicht: Man muss dringend sein Passwort hashen und hat dafür gerade kein hasher zu Hand?";
    }
}

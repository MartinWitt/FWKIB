package keksdose.fwkib.modules.commands;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import keksdose.fwkib.modules.Command;


public class Hash implements Command {

    @Override
    public String apply(String message) {
        HashFunction hf = Hashing.sha512();
        String toHash = message.length() > 4 ? message.substring(5) : "";
        return hf.newHasher().putString(toHash, Charsets.UTF_8).hash().toString();
    }

}
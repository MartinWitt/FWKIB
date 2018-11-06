package keksdose.fwkib.modules;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Hash implements Command {

    @Override
    public String apply(String message) {
        HashFunction hf = Hashing.sha512();
        String toHash = message.length() > 4 ? message.substring(5) : "";
        return hf.newHasher().putString(toHash, Charsets.UTF_8).hash().toString();
    }

}
package keksdose.fwkib.modules;




import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import org.pircbotx.hooks.events.MessageEvent;

public class Hash implements Command {

    @Override
    public String apply(MessageEvent event) {
        HashFunction hf = Hashing.sha512();
        String toHash = event.getMessage().length() > 4 ? event.getMessage().substring(5) : "";
        return hf.newHasher().putString(toHash, Charsets.UTF_8).hash().toString();
    }

}
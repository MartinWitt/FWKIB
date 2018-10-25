package keksdose.fwkib.modules;



import java.util.UUID;

import org.pircbotx.hooks.events.MessageEvent;

public class Uuid implements Command{

    @Override
    public String apply(MessageEvent event) {
        return UUID.randomUUID().toString();

    }

}
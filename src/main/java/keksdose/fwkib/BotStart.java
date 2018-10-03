package keksdose.fwkib;

import java.io.IOException;

import org.jibble.pircbot.*;

public class BotStart implements Runnable {
    private String channel;

    public BotStart(String channel) {
        super();
        this.channel = channel.isEmpty() ? "#kitinfo" : channel;

    }

    @Override
    public void run() {

        FWKIB bot = new FWKIB();

        bot.setVerbose(true);

        try {
            bot.connect("irc.freenode.net");
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }

        bot.joinChannel(channel);

    }

}
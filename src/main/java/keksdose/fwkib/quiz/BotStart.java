package keksdose.fwkib.quiz;

import java.io.IOException;
import java.util.UUID;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

public class BotStart implements Runnable {
    private String channel;

    public BotStart(String channel) {
        super();
        this.channel = channel.isEmpty() ? "#kitinfo" : channel;

    }

    @Override
    public void run() {

        Configuration<PircBotX> config = new Configuration.Builder().setName("fwkib" + UUID.randomUUID())
        .addListener(new FWKIB()).setServer("irc.freenode.net", 6667).setAutoNickChange(true).addAutoJoinChannel(channel).setAutoReconnect(true).buildConfiguration();

        PircBotX bot = new PircBotX(config);
    
        
        try {
            bot.startBot();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IrcException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
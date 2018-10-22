package keksdose.fwkib.quiz;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.LogManager;


import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;

public class BotStart implements Runnable {
    private String channel;

    public BotStart(String channel) {
        super();
        this.channel = channel.isEmpty() ? "#kitinfo" : channel;

    }

    @Override
    public void run() {

        Configuration<PircBotX> config = new Configuration.Builder().setName("fwkib")
        .addListener(new FWKIB()).setServer("irc.freenode.net", 7000).setAutoNickChange(true).setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
        .addAutoJoinChannel(channel).addAutoJoinChannel("#kitinfo-test").addAutoJoinChannel("#kitinfo-botnet").setAutoReconnect(true).buildConfiguration();

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
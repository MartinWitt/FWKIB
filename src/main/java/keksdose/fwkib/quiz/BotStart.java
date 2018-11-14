package keksdose.fwkib.quiz;

import java.io.IOException;
import java.nio.charset.Charset;

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

        Configuration<PircBotX> config = new Configuration.Builder().setName("fwkib").addListener(new FWKIB())
                .setServer("irc.freenode.net", 7000).setAutoNickChange(true)
                .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates()).addAutoJoinChannel("#kitinfo-test")
                .addAutoJoinChannel(channel).addAutoJoinChannel("#kitinfo-botnet").setAutoReconnect(true)
                .setAutoSplitMessage(true).setCapEnabled(true).setEncoding(Charset.forName("UTF-8"))
                .buildConfiguration();

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
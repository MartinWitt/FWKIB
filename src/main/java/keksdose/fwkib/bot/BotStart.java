package keksdose.fwkib.bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;
import keksdose.fwkib.modules.commands.KI.FastVectorDose;
import keksdose.fwkib.modules.commands.Util.Version;
import keksdose.keksIrc.IRCStart;
import keksdose.keksIrc.Message.Message;

public class BotStart implements Runnable {

    public BotStart(String channel) {
        initStartTime();
    }

    private void initStartTime() {
        // sorgt für laden der Klasse
        Version version = new Version();
    }

    @Override
    public void run() {
        ArrayBlockingQueue<Message> container = new ArrayBlockingQueue<>(100);
        IRCStart start = new IRCStart(container);
        start.setUseCapHandler(true);
        start.setNickname("fwkibLokal");
        start.addChannel("#kitinfo-botnet");
        start.addChannel("#kitinfo-test");
        start.addChannel("#kitinfo");
        start.addChannel("##fwkib");
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                new FWKIB(container);
            }
        });

        try {
            start.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

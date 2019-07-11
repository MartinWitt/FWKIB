package keksdose.fwkib.bot;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

import keksdose.keksIrc.IRCStart;
import keksdose.keksIrc.Message.Message;

public class BotStart implements Runnable {

    public BotStart(String channel) {
    }

    @Override
    public void run() {

        ArrayBlockingQueue<Message> container = new ArrayBlockingQueue<>(100);
        IRCStart start = new IRCStart(container);
        start.setUseCapHandler(true); 
        start.setNickname("fwkib");
        start.addChannel("#kitinfo-botnet");
        start.addChannel("#kitinfo-test");
        start.addChannel("#kitinfo");
        Executors.newSingleThreadExecutor().submit(new Runnable() {

            @Override
            public void run() {
                new FWKIB(container);
            }
        });

        try {
            start.start();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

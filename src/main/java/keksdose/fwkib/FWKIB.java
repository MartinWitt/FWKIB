package keksdose.fwkib;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.ArrayListMultimap;

import com.google.common.collect.Multimap;
import com.mongodb.DBObject;

import org.jibble.pircbot.PircBot;

public class FWKIB extends PircBot {

    private Multimap<String, String> answers = ArrayListMultimap.create();

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        switch (message) {
        case ("#quiz"): {

            DBObject o = new MongoDB().getQuestion("test");
            sendMessage(channel, (String) o.get("question"));
            ExecutorService exService = Executors.newSingleThreadExecutor();
            exService.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.wait((long) o.get("time"));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            exService.shutdown();
            while (!exService.isTerminated())
                ; // einfach abwarten. CPU is egal
            if (exService.isTerminated()) {
                sendMessage(channel, (String) o.get("answerLetter"));
                new MongoDB().updateStats(new ArrayList<>(answers.get((String) o.get("answerLetter"))));

            }

        }
        case ("#stats"): {
            sendMessage(channel, new MongoDB().getStats());

        }
        default: {
            if (message.equalsIgnoreCase("a") || message.equalsIgnoreCase("b") || message.equalsIgnoreCase("c")
                    || message.equalsIgnoreCase("d")) {
                answers.put(sender, message.toLowerCase());
            }
        }

        }
    }

}
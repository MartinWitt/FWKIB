package keksdose.fwkib.quiz;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.ArrayListMultimap;

import com.google.common.collect.Multimap;
import com.mongodb.DBObject;

import org.jibble.pircbot.PircBot;

public class FWKIB extends PircBot {
    public FWKIB() {
        this.setName("FWKIB");
    }

    private AtomicBoolean bool = new AtomicBoolean(false);
    private Multimap<String, String> answers = ArrayListMultimap.create();

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        switch (message) {
        case ("#quiz"): {
            if (bool.get()) {
                sendMessage(channel, "quiz running");
                break;
            }
            ExecutorService exService = Executors.newSingleThreadExecutor();

            DBObject o = new MongoDB().getQuestion("math");
            sendMessage(channel, (String) o.get("question") + "    zeit: " + o.get("time").toString() );
            exService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        bool.set(true);
                        
                        System.out.println("mache quiz");
                        System.out.println(Integer.parseInt(o.get("time").toString()));
                        TimeUnit.SECONDS.sleep(Integer.parseInt(o.get("time").toString()));
                        bool.set(false);
                        System.out.println("gebe frei");
                        sendMessage(channel,"richtig ist: " + (String) o.get("answerLetter"));
                        new MongoDB().updateStats(new ArrayList<>(answers.get((String) o.get("answerLetter"))));
                        answers.clear();

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        bool.set(false);
                        answers.clear();
                        e.printStackTrace();
                    }
                }
            });
            break;

        }
        case ("#stats"): {
            sendMessage(channel, new MongoDB().getStats());
            break;

        }
        default: {
            if (message.equalsIgnoreCase("a") || message.equalsIgnoreCase("b") || message.equalsIgnoreCase("c")
                    || message.equalsIgnoreCase("d")) {

                if (!answers.containsEntry(message.toLowerCase(), sender)) {
                    answers.put(message.toLowerCase(), sender);
                }
            }

        }
        }

    }
}
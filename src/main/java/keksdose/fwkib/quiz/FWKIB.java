package keksdose.fwkib.quiz;

import java.util.ArrayList;
import java.util.UUID;
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
        this.setName("fwkib" + UUID.randomUUID());
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
            String topic = message.split("#quiz")[1].trim();
            DBObject o = new MongoDB().getQuestion(topic);
            sendMessage(channel,
                    o.get("question").toString() + getOptions(o) + "    zeit: " + o.get("time").toString());
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
                        sendMessage(channel, "richtig ist: " + o.get("answer").toString());
                        new MongoDB().updateStats(new ArrayList<>(answers.get(o.get("answer").toString())));
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
            if (bool.get() && !answers.containsValue(sender)) {
                answers.put(message.toLowerCase(), sender);

            }
        }

        }
    }

    private String getOptions(DBObject o) {
        String var = "";
        String[] split = o.get("options").toString().split(",");
        char letter = 'a';
        for (String option : split) {
            var.concat(letter + ")" + option + " ");
            letter++;
        }

        return var;
    }
}

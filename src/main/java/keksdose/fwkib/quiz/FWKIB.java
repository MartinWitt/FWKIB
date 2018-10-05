package keksdose.fwkib.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;

import com.google.common.collect.Multimap;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class FWKIB extends ListenerAdapter {

    private AtomicBoolean bool = new AtomicBoolean(false);
    private Multimap<String, String> answers = ArrayListMultimap.create();

    @Override
    public void onMessage(MessageEvent event) throws Exception {

        if (event.getMessage().startsWith("#quiz")) {
            if (bool.get()) {
                event.getChannel().send().message("quiz running");
                return;
            }
            ExecutorService exService = Executors.newSingleThreadExecutor();
            String topic = event.getMessage().split("#quiz")[0].trim();
            DBObject o = new MongoDB().getQuestion(topic);
            event.getChannel().send()
                    .message(o.get("question").toString() + getOptions(o) + "    zeit: " + o.get("time").toString());
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
                        event.getChannel().send().message("richtig ist: " + o.get("answers").toString());
                        BasicDBList list = (BasicDBList) o.get("answers");
                        List<String> qq = new ArrayList<>(); 
                        list.stream().forEach(element->qq.addAll(answers.get(element.toString())));
                        new MongoDB().updateStats(qq);
                        answers.clear();

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        bool.set(false);
                        answers.clear();
                        e.printStackTrace();
                    }
                }
            });
            return;

        }

        if (event.getMessage().equals("#stats")) {
            event.getChannel().send().message(new MongoDB().getStats());
            return;

        }

        if (bool.get() && !answers.containsValue(event.getUser())) {
            answers.put(event.getMessage().toLowerCase(), event.getUser().toString());

        }

    }

    private String getOptions(DBObject o) {
        String var = "";
        if(o == null|| o.get("options") == null){
            return var;
        }
        String list = o.get("options").toString();
        System.out.println(String.valueOf(list));
        
        if(list == null){
            return "";
        }
        
        
        
        char letter = 'a';
        for (String option : list.split("\" , \"")) {
            option.trim();
            System.out.println(String.valueOf(option));
            if(option.startsWith("[")){
                option = option.substring(1).trim();
            }
            if(option.startsWith("\"")){
                option = option.substring(1).trim();
            }
            if(option.endsWith("]")){
                option = option.substring(0,option.length()-1).trim();
            }
            if(option.endsWith("\"")){
                option = option.substring(0,option.length()-1).trim();
            }
            System.out.println(String.valueOf(option));
            var +=letter + ")" + String.valueOf(option) + " ";
            letter++;
            //TODO Map für Options
        }
        System.out.println(String.valueOf(var));
        return var;
    }

}

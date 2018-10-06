package keksdose.fwkib.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;

import com.google.common.collect.Multimap;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import keksdose.fwkib.quiz.DB.MongoDB;
import keksdose.fwkib.quiz.model.Question;
import keksdose.fwkib.quiz.model.QuestionWithAnswer;

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
            //TODO topic sinnvoll parsen
            String topic = String.valueOf(event.getMessage().split("#quiz")[0]).trim();
            
            DBObject o = new MongoDB().getQuestion(topic);
            Question question = new QuestionWithAnswer(o);
            answers.clear();
            event.getChannel().send().message( question.getQuestion());
            exService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        bool.set(true);

                        System.out.println("mache quiz");
                        TimeUnit.SECONDS.sleep(question.getTime());
                        bool.set(false);
                        String answersString = "";
                        for(String var:question.getAnswerList()){
                            answersString += var + " ";
                        }
                        System.out.println(answersString);
                        event.getChannel().send().message("richtig ist: " + answersString);
                        List<String> correctPersons = new ArrayList<>(); 
                        question.getAnswerList().stream().forEach(element->correctPersons.addAll(answers.get(element.toString())));
                        new MongoDB().updateStats(correctPersons);
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

        if (bool.get() && !answers.containsValue(event.getUser()) && !event.getUser().equals(event.getBot().getUserBot())) {
            
            answers.put(event.getMessage().toLowerCase(), event.getUser().toString());

        }

    }

    
}


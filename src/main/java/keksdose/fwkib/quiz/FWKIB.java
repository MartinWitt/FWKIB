package keksdose.fwkib.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;

import com.google.common.collect.Multimap;
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

            List<String> splitter = Splitter.on("#quiz").trimResults().splitToList(event.getMessage());
            String topic = splitter.size() == 1? splitter.get(0):"#test"; 
            
            DBObject o = new MongoDB().getQuestion(topic);
            Question question = new QuestionWithAnswer(o);
            answers.clear();
            event.getChannel().send().message( question.getQuestion());
            exService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        bool.set(true);

                        //System.out.println("mache quiz");
                        TimeUnit.SECONDS.sleep(question.getTime());
                        String answersString = "";
                        for(String var:question.getAnswerList()){
                            answersString += var + " ";
                        }
                        //System.out.println(answersString);
                        event.getChannel().send().message("richtig ist: " + answersString);
                        List<String> correctPersons = new ArrayList<>(); 
                        question.getAnswerList().forEach((element)->correctPersons.addAll(answers.get(element.toLowerCase())));
                        //System.out.println(String.valueOf(correctPersons.size()));
                        correctPersons.forEach(v-> System.out.println(v));
                        new MongoDB().updateStats(correctPersons);
                        event.getChannel().send().message(correctPersons.size() + " were correct of " + answers.size() );
                        answers.clear();
                        bool.set(false);
                        if(correctPersons.size()>0){
                            try {
                                TimeUnit.SECONDS.sleep(10);
                                if(!bool.get()){
                                onMessage(event);
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                //da ist rekursiv wohl kaputt
                            }
                        }

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

        if (bool.get() && !answers.containsValue(event.getUser().getNick()) && !event.getUser().equals(event.getBot().getUserBot())) {
            
            answers.put(event.getMessage().toLowerCase(), event.getUser().getNick());

        }

    }

    
}


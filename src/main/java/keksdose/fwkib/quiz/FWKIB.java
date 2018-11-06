package keksdose.fwkib.quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.DBObject;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import keksdose.fwkib.modules.Brati;
import keksdose.fwkib.modules.BrotiQuiz;
import keksdose.fwkib.modules.Hash;
import keksdose.fwkib.modules.Haskell;
import keksdose.fwkib.modules.HaskellUrl;
import keksdose.fwkib.modules.Help;
import keksdose.fwkib.modules.Home;
import keksdose.fwkib.modules.Missspell;
import keksdose.fwkib.modules.MongoStats;
import keksdose.fwkib.modules.Pwgen;
import keksdose.fwkib.modules.QuizStats;
import keksdose.fwkib.modules.ReminderKeksdose;
import keksdose.fwkib.modules.RsaGenPri;
import keksdose.fwkib.modules.RsaGenPub;
import keksdose.fwkib.modules.Security;
import keksdose.fwkib.modules.TvProgramm;
import keksdose.fwkib.modules.Uuid;
import keksdose.fwkib.modules.Youtube;
import keksdose.fwkib.quiz.DB.MongoDB;
import keksdose.fwkib.quiz.model.Question;
import keksdose.fwkib.quiz.model.QuestionWithAnswer;

public class FWKIB extends ListenerAdapter {

    private AtomicBoolean bool = new AtomicBoolean(false);
    private Multimap<String, String> answers = ArrayListMultimap.create();
    private List<String> ignore = Arrays.asList("Keksbot", "Chrisliebot");
    private List<String> answerList = null;

    // TODO Quiz etc. In Klassen machen und in ne Map putten. CleanUp das hier nur
    // noch einzelne Methoden mit if stehen und nicht mehr der Code.
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (ignore.contains(event.getUser().getNick())) {
            return;
        }
        if (event.getMessage().startsWith("#quiz")) {
            if (bool.get()) {
                event.getChannel().send().message("quiz running");
                return;
            }

            List<String> splitter = Splitter.on("#quiz").splitToList(event.getMessage());
            String topic = splitter.size() == 2 ? splitter.get(1).trim() : "info";
            System.out.println(topic);
            System.out.println(splitter.size());

            DBObject o = new MongoDB().getQuestion(topic);
            System.out.println(String.valueOf(o));
            if (o == null) {
                return;
            }

            Question question = new QuestionWithAnswer(o);
            answers.clear();

            event.getChannel().send().message(question.getQuestion());
            answerList = question.getAnswerList();
            Executors.newSingleThreadExecutor().submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        bool.set(true);

                        TimeUnit.SECONDS.sleep(question.getTime());
                        String answersString = "";
                        for (String var : question.getAnswerList()) {
                            answersString += var + " ";
                        }
                        event.getChannel().send().message("richtig ist: " + answersString);
                        List<String> correctPersons = new ArrayList<>();
                        question.getAnswerList()
                                .forEach((element) -> correctPersons.addAll(answers.get(element.toLowerCase())));
                        correctPersons.forEach(v -> System.out.println(v));
                        new MongoDB().updateStats(correctPersons);
                        event.getChannel().send().message(correctPersons.size() + " were correct of " + answers.size());
                        answers.clear();
                        bool.set(false);

                    } catch (InterruptedException e) {
                        bool.set(false);
                        answers.clear();
                        e.printStackTrace();
                    }
                }
            });
            return;

        }
        if (event.getMessage().startsWith("#tv-nau") || event.getMessage().startsWith("#tv-now")) {
            event.getChannel().send().message(new TvProgramm().apply("now"));

            return;
        }
        if (event.getMessage().startsWith("#fehler")) {
            event.getChannel().send().message(new Missspell().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#remove")) {
            event.getChannel().send().message(new Missspell().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#yt")) {
            event.getChannel().send().message(new Youtube().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#tv")) {
            event.getChannel().send().message(new TvProgramm().apply(""));
            return;
        }
        if (event.getMessage().startsWith("#hash #pwgen")) {
            event.getChannel().send().message(new Hash().compose(new Pwgen()).apply(""));
            return;
        }
        if (event.getMessage().startsWith("#stats")) {
            event.getChannel().send().message(new QuizStats().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#help")) {
            event.getChannel().send().message(new Help().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().equals("#mongo")) {
            event.getChannel().send().message(new MongoStats().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().equals("#security")) {
            event.getChannel().send().message(new Security().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().equals("#uuid")) {
            event.getChannel().send().message(new Uuid().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().equals("#rsagen-pub")) {
            event.getChannel().send().message(new RsaGenPub().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().equals("#rsagen-pri")) {
            event.getChannel().send().message(new RsaGenPri().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().equals("#pwgen")) {
            event.getChannel().send().message(new Pwgen().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#haskell-url")) {
            event.getChannel().send().message(new HaskellUrl().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#haskell")) {
            event.getChannel().send().message(new Haskell().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#brati")) {
            event.getChannel().send().message(new Brati().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#home")) {
            event.getChannel().send().message(new Home().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#hash")) {
            event.getChannel().send().message(new Hash().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().toLowerCase().startsWith("keksbot,")
                || event.getMessage().toLowerCase().startsWith("keksbot:")) {
            event.getChannel().send().message(new ReminderKeksdose().apply(event.getMessage()));
            return;
        }

        if (event.getMessage().contains("secs") && event.getUser().getNick().equals("broti")) {
            new BrotiQuiz().apply(event);
            return;
        }

        if (bool.get() && !event.getUser().equals(event.getBot().getUserBot())) {
            System.out.println(String.valueOf(answerList));
            if (answerList != null && answerList.contains(event.getMessage().toLowerCase())) {

                answers.entries().removeIf(v -> v.getValue().equals(event.getUser().getNick()));

                answers.put(event.getMessage().toLowerCase(), event.getUser().getNick());
            }

        }

    }

}

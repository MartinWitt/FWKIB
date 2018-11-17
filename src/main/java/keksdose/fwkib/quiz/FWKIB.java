package keksdose.fwkib.quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.*;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.DBObject;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import keksdose.fwkib.modules.BrotiQuiz;
import keksdose.fwkib.modules.ReminderKeksdose;
import keksdose.fwkib.modules.commands.Brati;
import keksdose.fwkib.modules.commands.BratiSong;
import keksdose.fwkib.modules.commands.Hash;
import keksdose.fwkib.modules.commands.Haskell;
import keksdose.fwkib.modules.commands.HaskellUrl;
import keksdose.fwkib.modules.commands.Help;
import keksdose.fwkib.modules.commands.Home;
import keksdose.fwkib.modules.commands.Misspell;
import keksdose.fwkib.modules.commands.MongoStats;
import keksdose.fwkib.modules.commands.Pwgen;
import keksdose.fwkib.modules.commands.QuizStats;
import keksdose.fwkib.modules.commands.RsaGenPri;
import keksdose.fwkib.modules.commands.RsaGenPub;
import keksdose.fwkib.modules.commands.Security;
import keksdose.fwkib.modules.commands.TvProgramm;
import keksdose.fwkib.modules.commands.Uuid;
import keksdose.fwkib.modules.commands.Youtube;
import keksdose.fwkib.quiz.DB.MongoDB;
import keksdose.fwkib.quiz.model.Question;
import keksdose.fwkib.quiz.model.QuestionWithAnswer;

public class FWKIB extends ListenerAdapter {

    private AtomicBoolean bool = new AtomicBoolean(false);
    private Multimap<String, String> answers = ArrayListMultimap.create();
    private List<String> ignore = Arrays.asList("Keksbot", "Chrisliebot");
    private List<String> answerList = null;
    private String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
    private List<String> optionList = null;

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
            optionList = question.getOptions();
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
                        question.getAnswerList().forEach((element) -> correctPersons
                                .addAll(answers.get(element.toLowerCase())));
                        correctPersons.forEach(v -> System.out.println(v));
                        new MongoDB().updateStats(correctPersons);
                        event.getChannel().send().message(
                                correctPersons.size() + " were correct of " + answers.size());
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
            event.getChannel().send().message(new Misspell().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#remove")) {
            event.getChannel().send().message(new Misspell().apply(event.getMessage()));
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
        if (event.getMessage().startsWith("#bratisong")) {
            event.getChannel().send().message(new BratiSong().apply(event.getMessage()));
            return;
        }
        if (event.getMessage().startsWith("#brati") || event.getMessage().startsWith("#rage")) {
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

        if (event.getMessage().contains("secs") && event.getUser().getNick().equals("broti")
                || event.getUser().getNick().equals("fwkib")
                        && event.getMessage().contains("zeit: ")) {
            new BrotiQuiz().apply(event);
            return;
        }
        if (event.getMessage().contains("#nick") && event.getUser().getLogin().equals("~Keksdose")
                && event.getUser().getHostmask().equals("2a01:4f8:1c1c:11a7::1")) {
            event.getBot().sendRaw().rawLine("nick " + event.getMessage().split(" ")[1]);
            return;
        }

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(event.getMessage());

        if (matcher.find()) {
            System.out.println(matcher.group(0).split(" ")[0]);
            event.getChannel().send().message(new Youtube().apply(
                    "https://www.youtube.com/watch?v=" + matcher.group(0).split(" ")[0].trim()));
            return;
        }
        if (bool.get() && !event.getUser().equals(event.getBot().getUserBot())) {
            System.out.println(String.valueOf(answerList));
            if (optionList != null && (optionList.contains(event.getMessage().toLowerCase())
                    || optionList.size() == 0)) {

                answers.entries().removeIf(v -> v.getValue().equals(event.getUser().getNick()));

                answers.put(event.getMessage().toLowerCase(), event.getUser().getNick());
            }

        }

    }

}

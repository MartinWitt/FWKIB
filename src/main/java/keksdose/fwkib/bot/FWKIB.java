package keksdose.fwkib.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.*;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.DBObject;

import keksdose.fwkib.modules.BratiSongInsert;
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
import keksdose.fwkib.modules.commands.NNDose;
import keksdose.fwkib.modules.commands.Pwgen;
import keksdose.fwkib.modules.commands.QuizStats;
import keksdose.fwkib.modules.commands.RandomBrati;
import keksdose.fwkib.modules.commands.RsaGenPri;
import keksdose.fwkib.modules.commands.RsaGenPub;
import keksdose.fwkib.modules.commands.Security;
import keksdose.fwkib.modules.commands.Sleepdose;
import keksdose.fwkib.modules.commands.SmartDose;
import keksdose.fwkib.modules.commands.Spellcheck;
import keksdose.fwkib.modules.commands.Spelluncheck;
import keksdose.fwkib.modules.commands.TvProgramm;
import keksdose.fwkib.modules.commands.Uuid;
import keksdose.fwkib.modules.commands.Youtube;
import keksdose.fwkib.mongo.MongoDB;
import keksdose.keksIrc.Message.Message;
import keksdose.fwkib.bot.model.Question;
import keksdose.fwkib.bot.model.QuestionWithAnswer;

public class FWKIB {
    public FWKIB(ArrayBlockingQueue<Message> queue) {
        while (true) {
            Message m;
            try {
                m = queue.take();
                if (m == null) {
                    continue;
                }

                onMessage(m);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private AtomicBoolean bool = new AtomicBoolean(false);
    private Multimap<String, String> answers = ArrayListMultimap.create();
    private List<String> ignore = Arrays.asList("Keksbot", "Chrisliebot");
    private List<String> answerList = null;
    private String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
    private List<String> optionList = null;

    // TODO Quiz etc. In Klassen machen und in ne Map putten. CleanUp das hier nur
    // noch einzelne Methoden mit if stehen und nicht mehr der Code.
    public void onMessage(Message event) throws Exception {

        if (event.getContent().startsWith("#quiz")) {
            if (bool.get()) {
                event.answer("quiz running");
                return;
            }

            List<String> splitter = Splitter.on("#quiz").splitToList(event.getContent());
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

            event.answer(question.getQuestion());

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
                        event.answer("richtig ist: " + answersString);
                        List<String> correctPersons = new ArrayList<>();
                        question.getAnswerList()
                                .forEach((element) -> correctPersons.addAll(answers.get(element.toLowerCase())));
                        correctPersons.forEach(v -> System.out.println(v));
                        new MongoDB().updateStats(correctPersons);
                        event.answer(correctPersons.size() + " were correct of " + answers.size());
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

        if (event.getContent().startsWith("#tv-nau") || event.getContent().startsWith("#tv-now")) {
            event.answer(new TvProgramm().apply("now"));
            return;
        }
        if (event.getContent().startsWith("#spellcheck")) {
            event.answer(new Spellcheck().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#spelluncheck")) {
            event.answer(new Spelluncheck().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#fehler")) {
            event.answer(new Misspell().apply(event.getContent()));
            return;
        }
        // --------------------------NN------------------------------//
        if (event.getContent().startsWith("#smartDose")) {
            event.answer(new NNDose().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#markovDose")) {
            event.answer(new SmartDose().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#randomBrati")) {
            event.answer(new RandomBrati().apply(event.getContent()));
            return;
        }
        // --------------------------NN-Ende-----------------------------//

        if (event.getContent().startsWith("#remove")) {
            event.answer(new Misspell().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#yt")) {
            event.answer(new Youtube().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#tv")) {
            event.answer(new TvProgramm().apply(""));
            return;
        }
        if (event.getContent().startsWith("#hash #pwgen")) {
            event.answer(new Hash().compose(new Pwgen()).apply(""));
            return;
        }
        if (event.getContent().startsWith("#stats")) {
            event.answer(new QuizStats().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#help")) {
            event.answer(new Help().apply(event.getContent()));
            return;
        }
        if (event.getContent().equals("#mongo")) {
            event.answer(new MongoStats().apply(event.getContent()));
            return;
        }
        if (event.getContent().equals("#security")) {
            event.answer(new Security().apply(event.getContent()));
            return;
        }
        if (event.getContent().equals("#uuid")) {
            event.answer(new Uuid().apply(event.getContent()));
            return;
        }
        if (event.getContent().equals("#rsagen-pub")) {
            event.answer(new RsaGenPub().apply(event.getContent()));
            return;
        }
        if (event.getContent().equals("#rsagen-pri")) {
            event.answer(new RsaGenPri().apply(event.getContent()));
            return;
        }
        if (event.getContent().equals("#pwgen")) {
            event.answer(new Pwgen().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#haskell-url")) {
            event.answer(new HaskellUrl().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#haskell")) {
            event.answer(new Haskell().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#bratisong")) {
            event.answer(new BratiSong().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#brati") || event.getContent().startsWith("#rage")) {
            event.answer(new Brati().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#sleepdose")) {
            event.answer(new Sleepdose().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#home")) {
            event.answer(new Home().apply(event.getContent()));
            return;
        }
        if (event.getContent().startsWith("#hash")) {
            event.answer(new Hash().apply(event.getContent()));
            return;
        }
        if (event.getContent().toLowerCase().startsWith("keksbot,")
                || event.getContent().toLowerCase().startsWith("keksbot:")) {
            event.answer(new ReminderKeksdose().apply(event.getContent()));
            return;
        }

        /*
         * if (event.getContent().contains("secs") &&
         * event.getUser().getNick().equals("broti") ||
         * event.getHostName().getNick().equals("fwkib") &&
         * event.getContent().contains("zeit: ")) { new BrotiQuiz().apply(event);
         * return; }
         */
        /*
         * if (event.getContent().contains("#nick") &&
         * event.getUser().getLogin().equals("~Keksdose") &&
         * event.getUser().getHostmask().equals("2a01:4f8:1c1c:11a7::1")) {
         * event.getBot().sendRaw().rawLine("nick " + event.getContent().split(" ")[1]);
         * return; } if (event.getContent().trim().startsWith("~") &&
         * event.getUser().getNick().contains("brati") &&
         * event.getContent().trim().endsWith("~")) { new
         * BratiSongInsert().apply(event.getContent().replaceAll("~", ""),
         * event.getUser().getNick()); return; }
         * 
         */
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(event.getContent());

        if (matcher.find()) {
            System.out.println(matcher.group(0).split(" ")[0]);
            event.answer(
                    new Youtube().apply("https://www.youtube.com/watch?v=" + matcher.group(0).split(" ")[0].trim()));
            return;
        }
        /*
         * if (bool.get() && !event.getUser().equals(event.getBot().getUserBot())) {
         * System.out.println(String.valueOf(answerList)); if (optionList != null &&
         * (optionList.contains(event.getContent().toLowerCase()) || optionList.size()
         * == 0)) {
         * 
         * answers.entries().removeIf(v ->
         * v.getValue().equals(event.getUser().getNick()));
         * 
         * answers.put(event.getContent().toLowerCase(), event.getUser().getNick()); }
         * 
         */
    }

}

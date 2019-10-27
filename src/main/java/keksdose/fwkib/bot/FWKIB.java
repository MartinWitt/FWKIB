package keksdose.fwkib.bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bson.Document;
import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;
import keksdose.fwkib.bot.model.Question;
import keksdose.fwkib.bot.model.QuestionWithAnswer;
import keksdose.fwkib.modules.BratiSongInsert;
import keksdose.fwkib.modules.BrotiQuiz;
import keksdose.fwkib.modules.CommandController;
import keksdose.fwkib.modules.ReminderKeksdose;
import keksdose.fwkib.modules.commands.FindBrati;
import keksdose.fwkib.modules.commands.OCR;
import keksdose.fwkib.modules.commands.Youtube;
import keksdose.fwkib.mongo.MongoDB;
import keksdose.keksIrc.Message.Message;

public class FWKIB {
    public FWKIB(ArrayBlockingQueue<Message> queue) {
        while (true) {
            Message m;
            try {
                m = queue.take();
                if (m == null) {
                    continue;
                }
                pool.execute(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            onMessage(m);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private ExecutorService pool = Executors.newCachedThreadPool();
    private AtomicBoolean bool = new AtomicBoolean(false);
    private Multimap<String, String> answers = ArrayListMultimap.create();
    private List<String> ignore = Arrays.asList("Keksbot", "Chrisliebot");
    private List<String> answerList = null;
    private String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
    private List<String> optionList = null;
    private CommandController controller = new CommandController();


    // TODO Quiz etc. In Klassen machen und in ne Map putten. CleanUp das hier nur
    // noch einzelne Methoden mit if stehen und nicht mehr der Code.
    public void onMessage(Message event) throws Exception {

        if (ignore.contains(event.getNick())) {
            return;
            // stops bots
        }
        checkForBrati(event.getHostName(), event.getNick());

        if (event.getContent().startsWith("#quiz")) {
            if (bool.get()) {
                event.answer("quiz running");
                return;
            }

            List<String> splitter = Splitter.on("#quiz").splitToList(event.getContent());
            String topic = splitter.size() == 2 ? splitter.get(1).trim() : "";
            if (topic.isBlank()) {
                event.answer("cant find topic");
                return;
            }
            System.out.println(topic);
            System.out.println(splitter.size());

            Document o = new MongoDB().getQuestion(topic);
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
                                .forEach((element) -> correctPersons
                                        .addAll(answers.get(element.toLowerCase())));
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
        if (event.getHostName().equals("195.201.137.123")
                && event.getContent().equals("#restart")) {
            new ProcessBuilder("./restart.sh").start();
            return;
        }
        if (event.getHostName().equals("195.201.137.123")
                && event.getContent().startsWith("#addBrati")) {
            event.answer(new MongoDB().insertBrati(
                    event.getContent().replaceFirst("#addBrati", "").trim(), false, "SleepDose"));
            return;
        }
        if (event.getHostName().equals("unaffiliated/chrisliebaer")
                && event.getContent().startsWith("#addBrati")) {
            event.answer(new MongoDB().insertBrati(
                    event.getContent().replaceFirst("#addBrati", "").trim(), false,
                    "Chrisliebaer"));
            return;
        }
        if (event.getContent().startsWith("#addQuote")
                || event.getContent().startsWith("#addquote")) {
            event.answer(new MongoDB().insertQuote(
                    event.getContent().replaceFirst("(?i)#addQuote", "").trim(), event.getNick()));
            return;
        }
        if (event.getContent().startsWith("#") && !event.getContent().startsWith("#!"))

        {
            event.answer(controller.executeInput(event.getContent()));
            return;
        }
        extractUrl(event.getContent());
        if (event.getContent().toLowerCase().startsWith("keksbot,")
                || event.getContent().toLowerCase().startsWith("keksbot:")) {
            event.answer(new ReminderKeksdose().apply(event.getContent()));
            return;
        }

        if (event.getContent().contains("secs") && event.getNick().equals("fakebroti")
                || event.getNick().equals("fwkib") && event.getContent().contains("zeit: ")) {
            new BrotiQuiz().apply(event);
            return;
        }
        if (event.getHostName().equals("195.201.137.123")) {
            if (!(event.getContent().startsWith(">") || event.getContent().startsWith("\""))) {

                new MongoDB().insertKeksdose(event.getContent());
            }
        }

        /*
         * if (event.getContent().contains("#nick") &&
         * event.getUser().getLogin().equals("~Keksdose") &&
         * event.getUser().getHostmask().equals("2a01:4f8:1c1c:11a7::1")) {
         * event.getBot().sendRaw().rawLine("nick " + event.getContent().split(" ")[1]); return; }
         */
        if (event.getContent().trim().startsWith("~") && event.getNick().contains("brati")
                && event.getContent().trim().endsWith("~")) {
            new BratiSongInsert().apply(event.getContent().replaceAll("~", ""), event.getNick());
            return;
        }

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(event.getContent());

        if (matcher.find()) {
            System.out.println(matcher.group(0).split(" ")[0]);
            event.answer(new Youtube().apply(
                    "https://www.youtube.com/watch?v=" + matcher.group(0).split(" ")[0].trim()));
            return;
        }

        if (bool.get() && !event.getNick().equals("fwkib")) {
            System.out.println(String.valueOf(answerList));
            if (optionList != null && (optionList.contains(event.getContent().toLowerCase())
                    || optionList.size() == 0)) {

                answers.entries().removeIf(v -> v.getValue().equals(event.getNick()));

                answers.put(event.getContent().toLowerCase(), event.getNick());
            }

        }

    }

    private void checkForBrati(String hostname, String username) {
        if (hostname.contains("static.48.166.76.144.clients.your-server.de")) {
            FindBrati.nick = username;
            System.out.println("set username: " + username);
        }
    }

    private void extractUrl(String args) {
        LinkExtractor linkExtractor =
                LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW)).build();
        Iterable<LinkSpan> links = linkExtractor.extractLinks(args);

        Iterator<LinkSpan> it = links.iterator();
        if (it.hasNext()) {
            LinkSpan link = it.next();
            OCR.LAST_URL = args.substring(link.getBeginIndex(), link.getEndIndex());
            System.out.println("link gesetzt: " + OCR.LAST_URL);
        }

    }

}

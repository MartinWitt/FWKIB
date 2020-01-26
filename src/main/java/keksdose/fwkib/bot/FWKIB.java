package keksdose.fwkib.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bson.Document;
import keksdose.fwkib.bot.model.Question;
import keksdose.fwkib.bot.model.QuestionWithAnswer;
import keksdose.fwkib.modules.CommandController;
import keksdose.fwkib.modules.commands.util.FindBrati;
import keksdose.fwkib.modules.eventbus.Event;
import keksdose.fwkib.modules.eventbus.ListenerManager;
import keksdose.fwkib.mongo.MongoDB;
import keksdose.keksirc.message.Message;

public class FWKIB {
  public FWKIB(ArrayBlockingQueue<Message> queue) {
    while (true) {
      Message m;
      try {
        m = queue.take();
        if (m == null) {
          continue;
        }
        pool.execute(() -> {
          try {
            onMessage(m);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private ExecutorService pool = Executors.newCachedThreadPool();
  private AtomicBoolean bool = new AtomicBoolean(false);
  private Multimap<String, String> answers = ArrayListMultimap.create();
  private List<String> answerList;
  private List<String> optionList;
  private CommandController controller = new CommandController();

  private ListenerManager listenerManager = new ListenerManager();

  public void onMessage(Message event) throws Exception {
    if (ConfigReader.getBots().contains(event.getNick().toLowerCase())) {
      return;
      // stops bots
    }
    if (event.getContent().equals("#reload")) {
      ConfigReader.reloadAll();
      event.answer("alles neugeladen und kaputt(?)");
      return;
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

      Document o = MongoDB.MongoDB.getQuestion(topic);
      if (o == null) {
        return;
      }

      Question question = new QuestionWithAnswer(o);
      answers.clear();

      event.answer(question.getQuestion());

      answerList = question.getAnswerList();
      optionList = question.getOptions();
      Executors.newSingleThreadExecutor().execute(() -> {
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
          MongoDB.MongoDB.updateStats(correctPersons);
          event.answer(correctPersons.size() + " were correct of " + answers.size());
          answers.clear();
          bool.set(false);
        } catch (InterruptedException e) {
          bool.set(false);
          answers.clear();
          e.printStackTrace();
        }

      });
      return;

    }

    if (listenerManager.handle(new Event(event))) {
      return;
    }
    if (event.getContent().startsWith("#") && !event.getContent().startsWith("#!")) {
      event.answer(controller.executeInput(event.getContent()));
      return;
    }
    if (bool.get() && !event.getNick().equals("fwkib")) {
      System.out.println(String.valueOf(answerList));
      if (optionList != null
          && (optionList.contains(event.getContent().toLowerCase()) || optionList.size() == 0)) {
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
}

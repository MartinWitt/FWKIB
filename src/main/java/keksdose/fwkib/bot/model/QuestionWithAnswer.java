package keksdose.fwkib.bot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mongodb.BasicDBList;

import org.bson.Document;

public class QuestionWithAnswer implements Question {

  private int time = 20;
  private String questionString = "";
  private List<String> answersList = new ArrayList<>();
  private Map<String, String> optionLetterMap = new HashMap<>();
  private List<String> solutionList = new ArrayList<>();

  public QuestionWithAnswer(Document o) {
    questionString = String.valueOf(o.get("question")) + formatOptions(o) + "  zeit: "
        + o.get("time").toString();
    formatSolution(o);
    formatTime(o);
  }

  @Override
  public String getQuestion() {
    return questionString;
  }

  @Override
  public List<String> getOptions() {
    return answersList;
  }

  @Override
  public int getAnzAnswers() {
    return answersList.size();
  }

  @Override
  public List<String> getAnswerList() {
    return solutionList;
  }

  @Override
  public int getTime() {
    return time;
  }

  private String formatOptions(Document o) {
    if (o == null || o.get("options") == null) {
      return "";
    }

    ArrayList<?> list = (ArrayList<?>) o.get("options");
    String var = "  ";
    char letter = 'a';
    for (Object iterObject : list) {
      String option = String.valueOf(iterObject);
      if (option.isEmpty()) {
        continue;
      }
      optionLetterMap.put(option, String.valueOf(letter));
      answersList.add(String.valueOf(letter));
      answersList.add(option);
      var += letter + ") " + String.valueOf(option) + " ";
      letter++;

    }
    return var;
  }

  private void formatSolution(Document o) {
    ArrayList<?> list = (ArrayList<?>) o.get("answers");

    for (Object listObject : list) {
      String option = String.valueOf(listObject);
      solutionList.add(option);
      Optional.ofNullable(optionLetterMap.get(option)).ifPresent(solutionList::add);
    }
  }

  private void formatTime(Document o) {
    time = Integer.parseInt(o.get("time").toString());

  }

}

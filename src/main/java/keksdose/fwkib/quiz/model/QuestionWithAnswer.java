package keksdose.fwkib.quiz.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mongodb.DBObject;

public class QuestionWithAnswer implements Question {

    private int time = 20;
    private String questionString = "";
    private List<String> answersList = new ArrayList<>();
    private Map<String, String> optionLetterMap = new HashMap<>();
    private List<String> solutionList = new ArrayList<>();

    public QuestionWithAnswer(DBObject o) {
        questionString = (o.get("question").toString() + formatOptions(o) + "    zeit: " + o.get("time").toString());
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

    private String formatOptions(DBObject o) {
        String var = "";
        if (o == null || o.get("options") == null) {
            return var;
        }
        String list = o.get("options").toString();
        System.out.println(String.valueOf(list));

        if (list == null) {
            return "";
        }
        char letter = 'a';
        for (String option : list.split("\" , \"")) {
            option.trim();
            System.out.println(String.valueOf(option));
            if (option.startsWith("[")) {
                option = option.substring(1).trim();
            }
            if (option.startsWith("\"")) {
                option = option.substring(1).trim();
            }
            if (option.endsWith("]")) {
                option = option.substring(0, option.length() - 1).trim();
            }
            if (option.endsWith("\"")) {
                option = option.substring(0, option.length() - 1).trim();
            }
            System.out.println(String.valueOf(option));
            optionLetterMap.put(option, String.valueOf(letter));
            var += letter + ")" + String.valueOf(option) + " ";
            letter++;
        }
        System.out.println(String.valueOf(var));
        return var;
    }

    private void formatSolution(DBObject o) {

        String unformatted = o.get("answers").toString();
        System.out.println(String.valueOf(unformatted));

        for (String option : unformatted.split("\" , \"")) {
            option.trim();
            if (option.startsWith("[")) {
                option = option.substring(1).trim();
            }
            if (option.startsWith("\"")) {
                option = option.substring(1).trim();
            }
            if (option.endsWith("]")) {
                option = option.substring(0, option.length() - 1).trim();
            }
            if (option.endsWith("\"")) {
                option = option.substring(0, option.length() - 1).trim();
            }
            System.out.println(option);
            solutionList.add(option);
            Optional.ofNullable(optionLetterMap.get(option)).ifPresent(solutionList::add);
        }

    }

    private void formatTime(DBObject o) {
        time = Integer.parseInt(o.get("time").toString());

    }



}
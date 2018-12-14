package keksdose.fwkib.bot.model;

import java.util.List;

public interface Question {

    public String getQuestion();

    public List<String> getOptions();

    public int getAnzAnswers();

    public List<String> getAnswerList();

    public int getTime();

}
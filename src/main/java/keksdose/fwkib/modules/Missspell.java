package keksdose.fwkib.modules;

import java.util.List;

import com.google.common.base.Splitter;

import keksdose.fwkib.quiz.DB.MongoDB;

public class Missspell implements Command {

    @Override
    public String apply(String message) {
        System.out.println("starte split");

        List<String> splitter = Splitter.on(" ").omitEmptyStrings().splitToList(message);
        String wordWrong = splitter.size() == 4 ? splitter.get(1).trim() : "";
        String wordCorrect = splitter.size() == 4 ? splitter.get(2).trim() : "";
        String wordRemember = splitter.size() == 4 ? splitter.get(3).trim() : "";
        System.out.println(splitter.size());

        if(splitter.size() == 2){
            System.out.println("im if");

           String var= new MongoDB().getMistake(splitter.get(1));
           System.out.println(var);

           if(!var.equalsIgnoreCase("null")){

               return var;
           }
        }
        if (wordWrong.isBlank() || wordCorrect.isBlank() || wordRemember.isBlank()) {
            return "furchtbar du monster";
        }

        String var = "\"" + wordWrong + "\"" + " schreibt sich eigentlich " + "\"" + wordCorrect + "\""
                + " , du kannst es dir merken mit " + "\"" + wordCorrect + "\"" + " wie " + "\"" + wordRemember
                + "\" .";
                System.out.println("state save");

        saveMisstake(var, wordWrong);


        return var;
    }


    private void saveMisstake(String var,String wordWrong){
       new MongoDB().insertMistake(var, wordWrong);
    }

}
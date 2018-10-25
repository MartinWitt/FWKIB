package keksdose.fwkib.modules;


import java.util.List;

import com.google.common.base.Splitter;

import org.pircbotx.hooks.events.MessageEvent;

import keksdose.fwkib.quiz.DB.MongoDB;

public class QuizStats implements Command {

    @Override
	public String apply(MessageEvent event){
        List<String> splitter = Splitter.on("#stats").splitToList(event.getMessage());
        String username = splitter.size() == 2 ? splitter.get(1).trim() : "";
        if (username.isEmpty()) {
            return new MongoDB().getStats();
        } else {
            return new MongoDB().getStats(username);

            
        }    }
}
package keksdose.fwkib.modules;

import org.pircbotx.hooks.events.MessageEvent;

import keksdose.fwkib.quiz.DB.MongoDB;

public class Brati implements Command {

    @Override
    public String apply(String message) {
        return String.valueOf(new MongoDB().getBrati());
       

    }

}
package keksdose.fwkib;

import java.util.concurrent.Executors;

import keksdose.fwkib.quiz.BotStart;

public class Main {
    public static void main(String[] args) {

        // System.out.print("Enter channel:");
        // String input = System.console().readLine();
        Executors.newSingleThreadExecutor().submit(new BotStart("#kitinfo"));

    }

}
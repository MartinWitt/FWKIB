package keksdose.fwkib;

import java.io.IOException;
import java.util.concurrent.Executors;

import keksdose.fwkib.bot.BotStart;

public class Main {
    public static void main(String[] args) throws IOException {

        // System.out.print("Enter channel:");
        // String input = System.console().readLine();
        Executors.newSingleThreadExecutor().submit(new BotStart("#kitinfo"));

    }

}
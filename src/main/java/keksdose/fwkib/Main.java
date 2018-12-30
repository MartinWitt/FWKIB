package keksdose.fwkib;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;

import keksdose.fwkib.bot.BotStart;
import keksdose.keksIrc.IRCStart;
import keksdose.keksIrc.Message.Message;

public class Main {
    public static void main(String[] args) throws IOException {

        // System.out.print("Enter channel:");
        // String input = System.console().readLine();

        Executors.newSingleThreadExecutor().submit(new BotStart("#kitinfo"));

    }

}
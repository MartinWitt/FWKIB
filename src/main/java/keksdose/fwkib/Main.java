package keksdose.fwkib;

import java.util.concurrent.Executors;

import keksdose.fwkib.bot.BotStart;

public class Main {

    public static void main(String[] args) {

        Executors.newSingleThreadExecutor().submit(new BotStart("#kitinfo"));
    }

}

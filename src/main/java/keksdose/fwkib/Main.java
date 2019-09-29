package keksdose.fwkib;

import java.util.concurrent.Executors;

import keksdose.fwkib.bot.BotStart;

public class Main {
    private int a = 3;

    /**
     * toller test geht auch multiline?
     * adsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss
     * 
     * @param args
     */
    public static void main(String[] args) {
        // System.out.print("Enter channel:");
        // String input = System.console().readLine();
        Executors.newSingleThreadExecutor().submit(new BotStart("#kitinfo"));
    }

    public int name() {
        return this.a;
    }
}

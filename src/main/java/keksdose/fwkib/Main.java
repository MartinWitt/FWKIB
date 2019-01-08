package keksdose.fwkib;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.Executors;

import keksdose.fwkib.bot.BotStart;

public class Main {
    public static void main(String[] args) throws IOException {

        // System.out.print("Enter channel:");
        // String input = System.console().readLine();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        Executors.newSingleThreadExecutor().submit(new BotStart("#kitinfo"));

    }

}
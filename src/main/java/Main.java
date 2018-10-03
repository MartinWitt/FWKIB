
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jibble.pircbot.*;

import keksdose.fwkib.BotStart;


public class Main extends PircBot {
    public static void main(String[] args){
        ExecutorService service = Executors.newCachedThreadPool();
        
        for(;;){
        System.out.print("Enter channel:");
        String input = System.console().readLine();
        service.execute(new BotStart(input));
        
        }
            
    }
    
}
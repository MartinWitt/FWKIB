package keksdose.fwkib;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import keksdose.fwkib.quiz.BotStart;


public class Main {
    public static void main(String[] args){
        ExecutorService service = Executors.newCachedThreadPool();
        
        //System.out.print("Enter channel:");
        //String input = System.console().readLine();
        service.execute(new BotStart("#kitinfo")); 
        System.out.close();



            
    }
    
}
package keksdose.fwkib.modules;





public class ReminderKeksdose implements Command {

    @Override
    public String apply(String message) {
        return "^ Keksdose";

    }

}
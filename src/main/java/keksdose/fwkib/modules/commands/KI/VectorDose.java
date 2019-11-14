package keksdose.fwkib.modules.commands.KI;


import java.util.concurrent.ThreadLocalRandom;
import keksdose.fwkib.modules.Command;
import keksdose.fwkib.modules.TensorLock;

public class VectorDose implements Command {


    @Override
    public String apply(String message) {
        return "entfernt bis neuschreiben";
        // try {
        // TensorLock.getLock();
        // TensorLock.releaseLock();
        // return FastVectorDose.vec.predictSeveral(message, 5).stream()
        // .sorted((o1, o2) -> ThreadLocalRandom.current().nextInt(-1, 2)).findAny()
        // .orElse("");
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // TensorLock.releaseLock();
        // return "IO :(";
        // }

    }

    @Override
    public String help(String message) {
        return "eine traurige Bautstelle bis *DU* die neuschreibst";
    }

}

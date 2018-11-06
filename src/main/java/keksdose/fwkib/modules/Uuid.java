package keksdose.fwkib.modules;

import java.util.UUID;

public class Uuid implements Command {

    @Override
    public String apply(String message) {
        return UUID.randomUUID().toString();

    }

}
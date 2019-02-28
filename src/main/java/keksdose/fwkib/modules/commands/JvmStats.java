package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;

public class JvmStats implements Command {
    private static final long MEGABYTE_FACTOR = 1024L * 1024L / 8;

    @Override
    public String apply(String message) {
        long ram = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return "used ram: " + ram / MEGABYTE_FACTOR + "MB" + " thanks new GC";

    }

}
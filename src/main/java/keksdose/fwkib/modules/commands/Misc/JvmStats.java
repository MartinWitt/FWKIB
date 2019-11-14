package keksdose.fwkib.modules.commands.Misc;

import keksdose.fwkib.modules.Command;

public class JvmStats implements Command {
    private static final long MEGABYTE_FACTOR = 1024L * 1024L / 8;

    @Override
    public String apply(String message) {
        long ram = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return "used ram: " + ram / MEGABYTE_FACTOR + "MB" + " thanks new GC";
    }

    @Override
    public String help(String message) {
        return "Falls dieser Bot keine Performante Implementierung hätte, wäre es vllt möglich diese hier vllt unter umständen zu erkennen. Aber auch nur vllt!!!";
    }
}

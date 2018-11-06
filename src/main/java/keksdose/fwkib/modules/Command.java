package keksdose.fwkib.modules;

import java.util.function.Function;

public interface Command extends Function<String, String> {

	public abstract String apply(String message);
}
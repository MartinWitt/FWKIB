package keksdose.fwkib.modules;



import java.util.function.Function;

import org.pircbotx.hooks.events.MessageEvent;

public interface Command extends Function<MessageEvent,String>{


	public abstract String apply(MessageEvent event);
}
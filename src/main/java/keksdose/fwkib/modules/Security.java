package keksdose.fwkib.modules;


import org.pircbotx.hooks.events.MessageEvent;

public class Security implements Command {

    private String securityFeatures = "use:uuid rsagen-pub rsagen-pri pwgen and hash";
    @Override
	public String apply(MessageEvent event){
        return securityFeatures;
    }
}
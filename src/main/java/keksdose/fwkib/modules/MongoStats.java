package keksdose.fwkib.modules;


import org.pircbotx.hooks.events.MessageEvent;

public class MongoStats implements Command {

    private String linkToMongo = "https://cloud.mongodb.com/freemonitoring/cluster/Q24YNZRNFJX5ZOHC7VAIMGNMHTA2WKSG";
    @Override
	public String apply(MessageEvent event){
        return linkToMongo;
    }
}
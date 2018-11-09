package keksdose.fwkib.modules.commands;

import keksdose.fwkib.modules.Command;

public class MongoStats implements Command {

    private String linkToMongo = "https://cloud.mongodb.com/freemonitoring/cluster/Q24YNZRNFJX5ZOHC7VAIMGNMHTA2WKSG";

    @Override
    public String apply(String message) {
        return linkToMongo;
    }
}
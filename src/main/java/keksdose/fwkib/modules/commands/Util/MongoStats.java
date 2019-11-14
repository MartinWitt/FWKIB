package keksdose.fwkib.modules.commands.Util;

import keksdose.fwkib.modules.Command;

public class MongoStats implements Command {

    private String linkToMongo =
            "https://cloud.mongodb.com/freemonitoring/cluster/Q24YNZRNFJX5ZOHC7VAIMGNMHTA2WKSG";

    @Override
    public String apply(String message) {
        return linkToMongo;
    }

    @Override
    public String help(String message) {
        return "Bis heute ist nicht klar was diese Graphen aussagen, aber die sind schon immer da, also leben alle damit.";
    }
}

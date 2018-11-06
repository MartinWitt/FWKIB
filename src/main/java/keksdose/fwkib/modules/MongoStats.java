package keksdose.fwkib.modules;

public class MongoStats implements Command {

    private String linkToMongo = "https://cloud.mongodb.com/freemonitoring/cluster/Q24YNZRNFJX5ZOHC7VAIMGNMHTA2WKSG";

    @Override
    public String apply(String message) {
        return linkToMongo;
    }
}
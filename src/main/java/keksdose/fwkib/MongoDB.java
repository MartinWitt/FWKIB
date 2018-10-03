package keksdose.fwkib;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDB {

    private String dbName = "question";
    private static MongoClient mongoClient;

    public MongoDB() {
        super();
        try {
            if (mongoClient == null) {
                mongoClient = new MongoClient();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public DBObject getQuestion(String collection) {
        DB database = mongoClient.getDB(dbName);
        if (collection.contains(";") || collection.contains(":") || collection.contains("!")) {
            System.out.println("nö ich behalt die DB");
            return null;
        }
        DBCollection questions = database.getCollection(collection);
        Random random = new Random();

        return questions.find().limit(-1).skip(random.nextInt((int) questions.count())).next();
    }

    public void updateStats(List<String> username) {
        DBCollection stats = mongoClient.getDB(dbName).getCollection("stats");
        for (String var : username) {
            DBObject query = new BasicDBObject("name", var);
            DBCursor cursor = stats.find(query);
            if (cursor.hasNext()) {

                DBObject user = cursor.next();
                int number = (int) user.get("number");
                number++;
                // update wäre änderbar
                user.put("number", number);

            } else {
                DBObject user = new BasicDBObject("name", username).append("number", "0");
                stats.insert(user);
            }
        }
    }

    public String getStats() {
        DBCollection stats = mongoClient.getDB(dbName).getCollection("stats");
        DBCursor cursor = stats.find();
        String value = "";
        int counter = 10;
        while (cursor.hasNext() && counter > 0) {
            cursor.next();
            value += (cursor.one().get("name") + " " + cursor.one().get("number"));
        }
        return value;

    }

}
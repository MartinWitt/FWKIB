package keksdose.fwkib.quiz.DB;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import keksdose.fwkib.quiz.model.User;

public class MongoDB {

    private static final char NBSP = '\u200B';
    private String dbName = "test";
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
        if(questions == null){
            return null;
        }

        return questions.find().limit(-1).skip(random.nextInt((int) questions.count())).next();
    }

    public void updateStats(List<String> username) {
        DBCollection stats = mongoClient.getDB(dbName).getCollection("stat");
        for (String var : username) {
            if (var.length() > 40) {
                continue;
            }
            BasicDBObject user = new BasicDBObject();
            user.put("name", var);
            DBObject search = stats.find(user).one();
            if (search == null) {
                DBObject toInsert = new BasicDBObject("name", var).append("number", "1");
                stats.insert(toInsert);
            } else {
                System.out.println("update " + var);
                // update wäre änderbar
                Integer number = Integer.parseInt(search.get("number").toString());
                number++;
                BasicDBObject update = new BasicDBObject();
                update.append("$set", new BasicDBObject().append("number", number.toString()));
                stats.update(search, update);
            }
        }

    }

    public String getStats() {
        DBCollection stats = mongoClient.getDB(dbName).getCollection("stat");
        DBCursor cursor = stats.find();

        String value = "";
        List<User> list = new ArrayList<>();
        while (cursor.hasNext()) {
            cursor.next();

            list.add(new User(cursor.curr().get("name").toString(), cursor.curr().get("number").toString()));

        }
        list.sort(new Comparator<User>() {

            @Override
            public int compare(User o1, User o2) {
                if (o1.getNumber() > o2.getNumber())
                    return -1;
                if (o2.getNumber() > o1.getNumber())
                    return 1;
                return 0;
            }

        });
        for (int i = 0; i < 10 && i <= list.size() - 1; i++) {
            StringBuilder str = new StringBuilder(list.get(i).getName());
            str.insert(1, NBSP);
            value += str.toString() + ":" + list.get(i).getNumber() + ". ";
        }
        return value;

    }

}
package keksdose.fwkib.quiz;

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

public class MongoDB {

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

        return questions.find().limit(-1).skip(random.nextInt((int) questions.count())).next();
    }

    public void updateStats(List<String> username) {
        DBCollection stats = mongoClient.getDB(dbName).getCollection("stat");
        for (String var : username) {
            DBCursor cursor = stats.find();
            while (cursor.hasNext()) {
                cursor.next();
                System.out.println(cursor.curr().toString());
                if (cursor.curr().get("name").equals(var)) {
                    System.out.println("update");


                    // update wäre änderbar

                    BasicDBObject user = new BasicDBObject();
                    Integer number = Integer.parseInt(cursor.curr().get("number").toString());
                    number++;
                    user.append("$set", new BasicDBObject().append("number", number.toString()));
                    stats.update(cursor.curr(), user);
                    

                    System.out.println(user.toString());
                    return;

                }
            }

            DBObject user = new BasicDBObject("name", var).append("number", "1");
            stats.insert(user);
        }
    }

    public String getStats() {
        DBCollection stats = mongoClient.getDB(dbName).getCollection("stat");
        DBCursor cursor = stats.find();

        String value = "";
        List<User> list = new ArrayList<>();
        while (cursor.hasNext()) {
            cursor.next();
            
            list.add(new User(cursor.curr().get("name").toString() ,cursor.curr().get("number").toString()));

        }
        list.sort(new Comparator<User>(){

            @Override
            public int compare(User o1, User o2) {
                if(o1.getNumber()>o2.getNumber()) return -1;
                if(o2.getNumber()>o1.getNumber()) return 1;
                return 0;
            }

        });
        for(int i =0;i<10 && i<=list.size()-1;i++){
            value += list.get(i).getName() + ":" + list.get(i).getNumber() + ". ";
        }
        return value;

    }

}
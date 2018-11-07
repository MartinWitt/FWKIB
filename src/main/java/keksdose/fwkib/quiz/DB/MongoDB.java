package keksdose.fwkib.quiz.DB;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import org.bson.Document;

import keksdose.fwkib.quiz.model.User;

public class MongoDB {

    private static final char NBSP = '\u200B';
    private String dbName = "test";
    private static MongoClient mongoClient;
    private List<Document> list = new ArrayList<>();

    public MongoDB() {
        super();
        if (mongoClient == null) {
            mongoClient = new MongoClient();
        }
    }

    public DBObject getQuestion(String collection) {
        DB database = mongoClient.getDB(dbName);
        if (collection.contains(";") || collection.contains(":") || collection.contains("!")) {
            return null;
        }
        DBCollection questions = database.getCollection(collection);
        Random random = new Random();
        if (questions == null) {
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

    public String getStats(String username) {
        DBCollection stats = mongoClient.getDB(dbName).getCollection("stat");
        BasicDBObject user = new BasicDBObject();
        user.put("name", username);
        DBObject search = stats.find(user).one();
        if (search == null) {
            return "user not found";
        }
        Integer number = Integer.parseInt(search.get("number").toString());
        if (number == null) {
            return "user not found";
        }
        User userObject = new User(search.get("name").toString(), search.get("number").toString());
        StringBuilder str = new StringBuilder(userObject.getName());
        str.insert(1, NBSP);
        return str.toString() + ":" + number + ". ";
    }

    public String getBrati() {
        DBCollection brati = mongoClient.getDB(dbName).getCollection("brati");
        Random random = new Random();

        DBObject json = brati.find().limit(-1).skip(random.nextInt((int) brati.count())).next();
        String s = (String) json.get("brati");
        return String.valueOf(s);

    }

    public void insertMistake(String sentence, String mistake) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection("mistake");
        if (collection.countDocuments() > 1000) {
            collection.findOneAndDelete(collection.find().first());
        }
        Document toInsert = new Document().append("mistake", mistake).append("sentence", sentence);
        collection.insertOne(toInsert);

    }

    public void removeMistake(String mistake) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection("mistake");

        Document toInsert = new Document().append("mistake", mistake);
        collection.findOneAndDelete(toInsert);

    }

    public String getMistake(String mistake) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("mistake");

        collection.aggregate(Arrays.asList(Aggregates.match(Filters.eq("mistake", mistake)))).forEach(printBlock);

        Random random = new SecureRandom();
        if (list.size() != 0) {

            String var = String.valueOf(list.get(random.nextInt(list.size())).get("sentence"));
            list = new ArrayList<>();

            return String.valueOf(var);

        }
        return "";
    }

    public String getYtLink(int n) {
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("youtube");
        if (n == 0) {
            Document doc = collection.find().sort(new BasicDBObject().append("time", -1)).first();

            if (doc != null) {
                collection.deleteOne(doc);
                return String.valueOf(doc.get("link"));

            } else {
                return "stack leer";
            }

        } else {
            Document doc = collection.find().sort(new BasicDBObject().append("time", -1)).skip(n).first();
            if (doc != null) {
                // collection.deleteOne(doc);
                return String.valueOf(doc.get("link"));

            } else {
                return "nicht im stack vorhanden.Vorhanden : " + collection.estimatedDocumentCount();
            }
        }
    }

    public void insertLink(String link) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection("youtube");
        if (collection.countDocuments() > 1000) {
            collection.findOneAndDelete(collection.find().first());
        }
        Document toInsert = new Document().append("link", link).append("uuid", UUID.randomUUID()).append("time",
                LocalTime.now());
        collection.insertOne(toInsert);
    }

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            list.add(document);
        }
    };

}
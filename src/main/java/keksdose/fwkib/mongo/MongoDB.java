package keksdose.fwkib.mongo;

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
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.internal.connection.Time;

import org.bson.Document;

import keksdose.fwkib.bot.model.User;

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

    public Document getQuestion(String collection) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        if (collection.contains(";") || collection.contains(":") || collection.contains("!")) {
            return null;
        }
        MongoCollection<Document> questions = database.getCollection(collection);
        if (questions == null) {
            return null;
        }

        return questions.aggregate(Arrays.asList(Aggregates.sample(1))).first();
    }

    public void updateStats(List<String> username) {
        MongoCollection<Document> stats = mongoClient.getDatabase(dbName).getCollection("stat");
        for (String var : username) {
            if (var.length() > 40) {
                continue;
            }
            Document user = new Document();
            user.put("name", var);
            Document search = stats.find(user).first();
            if (search == null) {
                Document toInsert = new Document("name", var).append("number", "1");
                stats.insertOne(toInsert);
            } else {

                // update wÃ¤re Ã¤nderbar
                Integer number = Integer.parseInt(search.get("number").toString());
                number++;
                BasicDBObject update = new BasicDBObject();
                update.append("$set", new BasicDBObject().append("number", number.toString()));
                stats.updateOne(search, update);

            }
        }

    }

    public String getStats() {
        MongoCollection<Document> stats = mongoClient.getDatabase(dbName).getCollection("stat");
        FindIterable<Document> cursor = stats.find();

        String value = "";
        List<User> list = new ArrayList<>();
        for (Document doc : cursor) {

            list.add(new User(doc.get("name").toString(), doc.get("number").toString()));

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
        MongoCollection<Document> stats = mongoClient.getDatabase(dbName).getCollection("stat");
        BasicDBObject user = new BasicDBObject();
        user.put("name", username);
        Document search = stats.find(user).first();
        if (search == null) {
            return "user not found";
        }
        Integer number = Integer.parseInt(search.get("number").toString());
        User userObject = new User(search.get("name").toString(), search.get("number").toString());
        StringBuilder str = new StringBuilder(userObject.getName());
        str.insert(1, NBSP);
        return str.toString() + ":" + number + ". ";
    }

    public String getBrati() {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("brati");

        Document brati = collection.aggregate(Arrays.asList(Aggregates.sample(1))).first();
        return String.valueOf(brati.get("brati"));

    }

    public String getBrati(String regex) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("brati");

        Document brati = collection
                .aggregate(Arrays.asList(Aggregates.match(Filters.regex("text", regex)), Aggregates.sample(1))).first();
        return String.valueOf(brati.get("brati"));

    }

    public String getKeksdose() {
        MongoCollection<Document> keksdose = mongoClient.getDatabase(dbName).getCollection("keksdose");
        Document json = keksdose.aggregate(Arrays.asList((Aggregates.sample(1)))).first();
        String s = (String) json.get("text");
        return String.valueOf(s);

    }

    public String getkeksdose(String regex) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("keksdose");

        Document brati = collection
                .aggregate(Arrays.asList(Aggregates.match(Filters.regex("text", regex)), Aggregates.sample(1))).first();

        return String.valueOf(brati.get("text"));
    }

    public String getBratiSong(String regex) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("bratiSong");

        Document bratiSong = collection
                .aggregate(Arrays.asList(Aggregates.match(Filters.regex("text", regex)), Aggregates.sample(1))).first();
        return String.valueOf(bratiSong.get("text"));

    }

    public void insertBratiSong(String song, String user) {
        if (song.isBlank()) {
            return;
        }
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("bratiSong");
        Document o = new Document();
        o.append("text", "~~~" + song + "~~~").append("time", Time.nanoTime()).append("user", user);
        collection.insertOne(o);

    }

    public String getHelp(String command) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("hilfe");
        String fix = collection.find(Filters.eq("id", "1")).first().getString("umlautText");
        Document doc = collection.find(Filters.eq("commandName", command.toLowerCase())).first();
        if (doc == null) {
            doc = collection.find(Filters.eq("modulname", command.toLowerCase())).first();
            if (doc == null) {
                return "befehl nicht vorhanden";
            }
        }
        String s = fix + " #" + doc.getString("commandName") + " ||" + doc.getString("helpText");
        return String.valueOf(s);

    }

    public void insertMistake(String wordWrong, String wordCorrect, String wordRemeber) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection("mistake");
        if (collection.countDocuments() > 10000) {
            collection.findOneAndDelete(collection.find().first());
        }
        Document toInsert = new Document().append("wordWrong", wordWrong).append("wordCorrect", wordCorrect)
                .append("wordRemember", wordRemeber);
        collection.insertOne(toInsert);

    }

    public void removeMistake(String mistake) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection("mistake");

        Document toInsert = new Document().append("mistake", mistake);
        collection.findOneAndDelete(toInsert);

    }

    public String getMistake(String wordWrong) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("mistake");

        collection.aggregate(Arrays.asList(Aggregates.match(Filters.eq("wordWrong", wordWrong)))).forEach(printBlock);

        Document output = collection
                .aggregate(Arrays.asList(Aggregates.match(Filters.eq("wordWrong", wordWrong)), Aggregates.sample(1)))
                .first();
        if (output == null) {
            return "";
        }
        String var = "\"" + String.valueOf(output.get("wordWrong")) + "\"" + " schreibt sich eigentlich " + "\""
                + String.valueOf(output.get("wordCorrect")) + "\"" + ", kannst es dir merken mit " + "\""
                + String.valueOf(output.get("wordCorrect")) + "\"" + " wie " + "\""
                + String.valueOf(output.get("wordRemember")) + "\"" + ".";
        return String.valueOf(var);

    }

    public String getWrongWord(String wordCorrect) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("mistake");

        Document output = collection
                .aggregate(
                        Arrays.asList(Aggregates.match(Filters.eq("wordCorrect", wordCorrect)), Aggregates.sample(1)))
                .first();
        if (output == null) {
            return "";
        }
        String var = String.valueOf(output.get("wordWrong"));
        return String.valueOf(var);

    }

    public String getCorrectWord(String wordWrong) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection("mistake");

        collection.aggregate(Arrays.asList(Aggregates.match(Filters.eq("wordWrong", wordWrong)))).forEach(printBlock);

        Document output = collection
                .aggregate(Arrays.asList(Aggregates.match(Filters.eq("wordWrong", wordWrong)), Aggregates.sample(1)))
                .first();
        if (output == null) {
            return "";
        }
        String var = String.valueOf(output.get("wordCorrect"));
        return String.valueOf(var);
    }

    public String getYtLink(int n) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
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

    public void insertKeksdose(String input) {
        if (input.startsWith("\u0001ACTION")) {
            input = "FWKIB glaubt Sleepdose: " + input.replaceAll("\u0001ACTION", "");
            input = input.replaceAll("\u0001", "");
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection("keksdose");
        Document toInsert = new Document().append("text", input).append("time", LocalTime.now());
        collection.insertOne(toInsert);
    }

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            list.add(document);
        }
    };

}

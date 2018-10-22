package keksdose.fwkib.quiz;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;

import com.google.common.collect.Multimap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.mongodb.DBObject;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import keksdose.fwkib.quiz.DB.MongoDB;
import keksdose.fwkib.quiz.model.Question;
import keksdose.fwkib.quiz.model.QuestionWithAnswer;

public class FWKIB extends ListenerAdapter {

    private AtomicBoolean bool = new AtomicBoolean(false);
    private Multimap<String, String> answers = ArrayListMultimap.create();

    // TODO Quiz etc. In Klassen machen und in ne Map putten. CleanUp das hier nur
    // noch einzelne Methoden mit if stehen und nicht mehr der Code.
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith("#quiz")) {
            if (bool.get()) {
                event.getChannel().send().message("quiz running");
                return;
            }
            ExecutorService exService = Executors.newSingleThreadExecutor();

            List<String> splitter = Splitter.on("#quiz").splitToList(event.getMessage());
            String topic = splitter.size() == 2 ? splitter.get(1).trim() : "info";
            System.out.println(topic);
            System.out.println(splitter.size());

            DBObject o = new MongoDB().getQuestion(topic);
            System.out.println(String.valueOf(o));
            if (o == null) {
                return;
            }

            Question question = new QuestionWithAnswer(o);
            answers.clear();
            event.getChannel().send().message(question.getQuestion());
            exService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        bool.set(true);

                        // System.out.println("mache quiz");
                        TimeUnit.SECONDS.sleep(question.getTime());
                        String answersString = "";
                        for (String var : question.getAnswerList()) {
                            answersString += var + " ";
                        }
                        // System.out.println(answersString);
                        event.getChannel().send().message("richtig ist: " + answersString);
                        List<String> correctPersons = new ArrayList<>();
                        question.getAnswerList()
                                .forEach((element) -> correctPersons.addAll(answers.get(element.toLowerCase())));
                        // System.out.println(String.valueOf(correctPersons.size()));
                        correctPersons.forEach(v -> System.out.println(v));
                        new MongoDB().updateStats(correctPersons);
                        event.getChannel().send().message(correctPersons.size() + " were correct of " + answers.size());
                        answers.clear();
                        bool.set(false);
                        if (correctPersons.size() > 0) {
                            try {
                                TimeUnit.SECONDS.sleep(10);
                                if (!bool.get()) {
                                    onMessage(event);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                // da ist rekursiv wohl kaputt
                            }
                        }

                    } catch (InterruptedException e) {
                        bool.set(false);
                        answers.clear();
                        e.printStackTrace();
                    }
                }
            });
            return;

        }

        if (event.getMessage().startsWith("#stats")) {
            List<String> splitter = Splitter.on("#stats").splitToList(event.getMessage());
            String username = splitter.size() == 2 ? splitter.get(1).trim() : "";
            System.out.println(username);
            if (username.isEmpty()) {
                event.getChannel().send().message(new MongoDB().getStats());
                return;
            } else {
                event.getChannel().send().message(new MongoDB().getStats(username));

                return;
            }

        }
        if (event.getMessage().equals("#mongo")) {
            event.getChannel().send()
                    .message("https://cloud.mongodb.com/freemonitoring/cluster/Q24YNZRNFJX5ZOHC7VAIMGNMHTA2WKSG");
            return;
        }
        if (event.getMessage().equals("#security")) {
            event.getChannel().send().message("use:uuid rsagen-pub rsagen-pri pwgen and hash");
            return;
        }
        if (event.getMessage().equals("#uuid")) {
            event.getChannel().send().message(UUID.randomUUID().toString());
            return;
        }
        if (event.getMessage().equals("#rsagen-pub")) {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
            ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteOs);
            dos.writeInt("ssh-rsa".getBytes().length);
            dos.write("ssh-rsa".getBytes());
            dos.writeInt(rsaPublicKey.getPublicExponent().toByteArray().length);
            dos.write(rsaPublicKey.getPublicExponent().toByteArray());
            dos.writeInt(rsaPublicKey.getModulus().toByteArray().length);
            dos.write(rsaPublicKey.getModulus().toByteArray());
            String enc = Base64.getEncoder().encodeToString(byteOs.toByteArray());
            event.getChannel().send().message("ssh-rsa " + enc + " " + "made by fwkib");
            return;
        }
        if (event.getMessage().equals("#rsagen-pri")) {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();
            ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(byteOs);
            dos.writeInt("ssh-rsa".getBytes().length);
            dos.write("ssh-rsa".getBytes());
            dos.writeInt(rsaPrivateKey.getPrivateExponent().toByteArray().length);
            dos.write(rsaPrivateKey.getPrivateExponent().toByteArray());
            dos.writeInt(rsaPrivateKey.getModulus().toByteArray().length);
            dos.write(rsaPrivateKey.getModulus().toByteArray());
            String enc = Base64.getEncoder().encodeToString(byteOs.toByteArray());
            event.getChannel().send().message("-----BEGIN RSA PRIVATE KEY-----");
            event.getChannel().send().message(enc);
            event.getChannel().send().message("-----END RSA PRIVATE KEY-----");
            return;
        }
        if (event.getMessage().equals("#pwgen")) {
            char[] possibleCharacters = (new String(
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?"))
                            .toCharArray();
            String randomStr = RandomStringUtils.random(50, 20, possibleCharacters.length - 1, false, false,
                    possibleCharacters, new SecureRandom());

            event.getChannel().send().message("Here is your secure pw: " + randomStr);
            return;
        }
        if (event.getMessage().startsWith("#haskell-url")) {
            String url = event.getMessage().length() > 12 ? event.getMessage().substring(12) : "";
            if (url.isEmpty()) {
                return;
            }
            String haskellString = getContent(url);
            haskellString.replace("-t", "");
            haskellString.replace("-e", "");
            String[] args = { "mueval", "-E", "-e", haskellString };
            java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(args).getInputStream())
                    .useDelimiter("\\A");
            String output = s.hasNext() ? s.next() : "";

            event.getChannel().send().message(output);
            s.close();
            return;
        }
        if (event.getMessage().startsWith("#haskell")) {
            String haskellString = event.getMessage().length() > 8 ? event.getMessage().substring(8) : "";
            if (haskellString.isEmpty()) {
                return;
            }
            haskellString.replace("-t", "");
            haskellString.replace("-e", "");
            String[] args = { "mueval", "-E", "-e", haskellString };
            java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(args).getInputStream())
                    .useDelimiter("\\A");
            String output = s.hasNext() ? s.next() : "";

            event.getChannel().send().message(output);
            s.close();
            return;
        }

        if (event.getMessage().startsWith("#hash")) {
            HashFunction hf = Hashing.sha512();
            String toHash = event.getMessage().length() > 4 ? event.getMessage().substring(5) : "";
            event.getChannel().send().message(hf.newHasher().putString(toHash, Charsets.UTF_8).hash().toString());
        }
        if (event.getMessage().toLowerCase().startsWith("keksbot,")
                || event.getMessage().toLowerCase().startsWith("keksbot:")) {
            event.getChannel().send().message("^Keksdose");
        }

        if (event.getMessage().contains("secs") && event.getUser().getNick().equals("broti")) {

            ExecutorService service = Executors.newSingleThreadExecutor();
            String message = event.getMessage();

            service.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        int index = message.indexOf("secs");
                        int wait = Integer.parseInt((message.subSequence(index - 3, index).toString()).trim());
                        TimeUnit.SECONDS.sleep(new Random().nextInt(wait / 2) + wait / 2);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    char letter = 'a';
                    int num = 0;
                    if (message.contains("b) "))
                        num++;
                    if (message.contains("c) "))
                        num++;
                    if (message.contains("d) "))
                        num++;
                    if (num == 0)
                        return;
                    num = new Random().nextInt(num);
                    letter = (char) (letter + num);
                    event.getChannel().send().message(String.valueOf(letter));
                }

            });
            return;
        }

        if (bool.get() && !answers.containsValue(event.getUser().getNick())
                && !event.getUser().equals(event.getBot().getUserBot())) {

            answers.put(event.getMessage().toLowerCase(), event.getUser().getNick());

        }

    }

    private String getContent(String adress) {
        URL url;
        if (!adress.trim().startsWith("https://pastebin.com")) {
            return "";
        }
        try {
            // get URL content

            url = new URL(adress);
            System.out.println(String.valueOf(url));
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(5000);

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            String haskell = "";
            while ((inputLine = br.readLine()) != null) {
                haskell += inputLine + "\n";
            }
            br.close();
            System.out.println(haskell);
            return haskell;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}

package keksdose.fwkib.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.common.net.InternetDomainName;

import org.pircbotx.hooks.events.MessageEvent;

public class HaskellUrl implements Command {

    @Override
    public String apply(MessageEvent event) {
        String url = event.getMessage().length() > 12 ? event.getMessage().substring(12) : "";
        if (url.isEmpty()) {
            return "";
        }
        String haskellString = getContent(url);
        String[] args = { "mueval", "-E", "-e", haskellString };
        java.util.Scanner s;
        try {
            s = new java.util.Scanner(Runtime.getRuntime().exec(args).getInputStream()).useDelimiter("\\A");

            String output = s.hasNext() ? s.next() : "";
            s.close();
            return output;
        } catch (IOException e) {
        }
        return "";

    }

    private String getContent(String adress) {
        URL url;
        if (!InternetDomainName.from(adress).topPrivateDomain().toString().equals("pastebin.com")) {
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
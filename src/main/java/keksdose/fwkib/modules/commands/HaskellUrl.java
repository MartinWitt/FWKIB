package keksdose.fwkib.modules.commands;

import java.io.BufferedReader;
import keksdose.fwkib.modules.Command;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.common.net.InternetDomainName;

public class HaskellUrl implements Command {

    @Override
    public String apply(String message) {
        return "";
        /*
         * String url = message.length() > 12 ? message.substring(12) : ""; if
         * (url.isEmpty()) { return ""; } String haskellString = getContent(url);
         * String[] args = { "mueval", "-E", "-e", haskellString }; java.util.Scanner s;
         * try { s = new
         * java.util.Scanner(Runtime.getRuntime().exec(args).getInputStream()).
         * useDelimiter("\\A");
         * 
         * String output = s.hasNext() ? s.next() : ""; s.close(); return output; }
         * catch (IOException e) { } return "";
         */
    }

    private String getContent(String adress) {
        URL url;
        try {
            if (adress.startsWith("https://")) {
                adress = adress.substring(8);
            }
            if (adress.startsWith("http://")) {
                adress = adress.substring(7);
            }

            if (!InternetDomainName.from(adress.trim()).topPrivateDomain().toString().equals("pastebin.com")) {
                return "";
            }
            try {

                // get URL content

                url = new URL(adress);
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
                return haskell;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            return "furchtbar, domain oder so kaputt";
        }

        return "";
    }
}
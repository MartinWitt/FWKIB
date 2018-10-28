package keksdose.fwkib.modules;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TvProgramm implements Command {

    private String link2015 = "https://www.tvspielfilm.de/tv-programm/rss/heute2015.xml";
    private String linkNow = "https://www.tvspielfilm.de/tv-programm/rss/jetzt.xml";
    private String time = "20:15";

    @Override
    public String apply(String message) {
         return message.equals("now")?getContent(linkNow): getContent(link2015);

        /*
         * List<String> splitter = Splitter.on("#tv").splitToList(message); String
         * username = splitter.size() == 2 ? splitter.get(1).trim() : ""; if
         * (username.isEmpty()) { return new MongoDB().getStats(); } else { return new
         * MongoDB().getStats(username);
         * 
         * }
         */
    }

    private String getContent(String link) {
        URL url;
        try {
            // get URL content

            url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setReadTimeout(5000);

            // open the stream and put it into BufferedReader

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            List<String> channel = new ArrayList<>();
            Document doc = builder.parse(conn.getInputStream());
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("title");
            System.out.println(String.valueOf(list));
            String tv ="";
            for (int i = 2; i < list.getLength() && i<9; i++) {
                Element element = (Element) list.item(i);
                channel.add(String.valueOf(element.getTextContent().toString()));                           
            }
            return String.join(",",channel);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return "";
    }
}
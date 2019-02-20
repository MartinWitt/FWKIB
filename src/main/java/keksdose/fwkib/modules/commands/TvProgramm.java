package keksdose.fwkib.modules.commands;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import keksdose.fwkib.modules.Command;

public class TvProgramm implements Command {

    private String link2015 = "https://www.tvspielfilm.de/tv-programm/rss/heute2015.xml";
    private String linkNow = "https://www.tvspielfilm.de/tv-programm/rss/jetzt.xml";

    @Override
    public String apply(String message) {
        return message.equals("now") ? getContent(linkNow) : getContent(link2015);

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
            for (int i = 0; i < list.getLength(); i++) {
                Element element = (Element) list.item(i);
                channel.add(String.valueOf(element.getTextContent().toString()));
            }
            return channel.stream()
                    .filter(v -> v.contains("| Das Erste |") || v.contains("| ZDF |") || v.contains("| RTL |")
                            || v.contains("| SAT.1 |") || v.contains("| ProSieben |") || v.contains("| kabel eins |")
                            || v.contains("| RTL II |") || v.contains("| VOX |"))
                    .collect(Collectors.joining(" || ")).replace("&amp;", "");

            // return String.join(",", channel);
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
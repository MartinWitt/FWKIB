package keksdose.fwkib.modules.commands;

import java.net.URL;
import java.util.List;

import com.google.common.base.Splitter;
import keksdose.fwkib.modules.Command;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

import keksdose.fwkib.mongo.MongoDB;

public class Youtube implements Command {

    private static final char NBSP = '\u200B';

    @Override
    public String apply(String message) {
        try {
            String title = message;
            if (NumberUtils.isCreatable(title)) {
                return NBSP + new MongoDB().getYtLink(Integer.parseInt(title));
            }
            if (title != null) {
                URL embededURL = new URL("http://www.youtube.com/oembed?url=" + title + "&format=json");
                String var = new JSONObject(IOUtils.toString(embededURL)).getString("title");
                new MongoDB().insertLink(var + " Link: " + title);
                return NBSP + var;

            } else {
                return NBSP + new MongoDB().getYtLink(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

}
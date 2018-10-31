package keksdose.fwkib.modules;
import java.net.URL;
import java.util.List;

import com.google.common.base.Splitter;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class Youtube implements Command {

    @Override
    public String apply(String message) {
            try {
                List<String> splitter = Splitter.on("#yt").splitToList(message);
                String title = splitter.size() == 2 ? splitter.get(1).trim() : null;
                if (title != null) {
                    URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                    title + "&format=json");        
                    return new JSONObject(IOUtils.toString(embededURL)).getString("title");
                }
        
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "furchtbar";
        
    }

}
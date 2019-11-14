package keksdose.fwkib.modules.commands.KI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import org.apache.commons.io.Charsets;
import org.apache.commons.text.StringTokenizer;

import keksdose.fwkib.modules.Command;

public class WeebFilter implements Command {

    @Override
    public String apply(String message) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("weebwords.txt"));
            StringTokenizer tk = new StringTokenizer(new OCR().apply(message));
            Set<String> filter = new HashSet<>();
            reader.lines().forEach(v -> filter.add(v.toLowerCase()));
            reader.close();
            List<String> list = tk.getTokenList()
                    .stream()
                    .map(String::toLowerCase)
                    .filter(v -> filter.contains(v))
                    .collect(Collectors.toList());
            StringBuilder s = new StringBuilder();
            for (var string : list) {
                System.out.println(string);
            }
            s.append("Words found are:" + Joiner.on(" ").join(list));
            if (list.size() > 0) {
                s.append(". too much weeb!");
            } else {
                s.append(" Looks good");
            }
            return s.toString();
        } catch (Exception e) {
            return "alles brennt";
        }
    }

    @Override
    public String help(String message) {
        return "eine traurige Bautstelle bis *DU* die neuschreibst";
    }

}

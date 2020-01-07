package keksdose.fwkib.modules.commands.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import keksdose.fwkib.modules.Command;


public class Home implements Command {
  private static String gitHubhome = "https://github.com/MartinWitt/FWKIB";
  private static String quote = "Any fool can write code that a computer can understand:  ";

  @Override
  public String apply(String message) {
    if (message.isBlank()) {
      return quote + gitHubhome;
    } else {
      return quote + gitHubhome;// TODO:
    }
  }

  @Override
  public String help(String message) {
    return "eine traurige Baustelle bis *DU* die neuschreibst. (Schau es lieber nicht an). Nachschauen der Src über nicht leeren input";
  }

  private String readFilesAndSearch(String message) throws IOException, URISyntaxException {
    URL url = getClass().getResource("/srcFiles");
    final Map<String, String> env = new HashMap<>();
    final String[] array = url.toURI().toString().split("!");
    final FileSystem fs = FileSystems.newFileSystem(url.toURI().create(array[0]), env);
    final Path path2 = fs.getPath(array[1]);
    System.out.println(path2);
    Collection<String> result = new HashSet<>();
    Iterator<Path> it = Files.walk(path2).iterator();
    while (it.hasNext()) {

      Path filePath = it.next();
      if (Files.isDirectory(filePath)) {
        continue;
      }
      BufferedReader reader = Files.newBufferedReader(filePath, Charset.defaultCharset());
      reader.lines()
          .filter(v -> v.matches(message))
          .map(v -> filePath.getFileName() + " " + v)
          .forEach(result::add);;
      System.out.println("Größe des Readers: " + reader.lines().count());
      reader.close();
    }
    fs.close();
    System.out.println("Result größe: " + result.size());
    return result.stream()
        .skip((int) (result.size() * Math.random()))
        .findFirst()
        .orElse("nix gefunden :(");
    // .flatMap(v -> v)
    // .filter(v -> v.matches(message))
    // .findAny()
    // .orElse("ich habe nix gefunden :(");
  }
}


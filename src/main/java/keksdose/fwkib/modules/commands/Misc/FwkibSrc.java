package keksdose.fwkib.modules.commands.Misc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import keksdose.fwkib.modules.Command;

public class FwkibSrc implements Command {

  @Override
  public String apply(String message) {
    try {
      return readFilesAndSearch(message);
    } catch (IOException | URISyntaxException e) {
      return "es gab ein Fehler :(";
    }
  }

  @Override
  public String help(String message) {
    return "Endlich kannst du den quelltext von diesem Kunstwerk lesen";
  }

  private String readFilesAndSearch(String message) throws IOException, URISyntaxException {
    try (Stream<Path> paths = Files
        .walk(Paths.get(getClass().getClassLoader().getResource("/srcFiles").toURI().getPath()))) {
      return paths.filter(Files::isRegularFile).map(t -> {
        try {
          return Files.lines(t);
        } catch (IOException e) {
          return Stream.<String>empty();
        }
      })
          .flatMap(v -> v)
          .filter(v -> v.matches(message))
          .findAny()
          .orElse("ich habe nix gefunden :(");
    }
  }
}

package keksdose.fwkib.modules.commands.ki;

import keksdose.fwkib.modules.Command;

public class FastVectorDose implements Command {

  /// public static ParagraphVectors vec;


  public static void loadModell() {
    // vec = WordVectorSerializer.readParagraphVectors("vectorDose.txt");
    // vec.setTokenizerFactory(new NGramTokenizerFactory(new DefaultTokenizerFactory(), 3, 6));

  }



  @Override
  public String

      apply(String message) {
    return "entfernt bis neuschreiben";

    // try {
    // return vec.nearestLabels(message, 5).stream()
    // .sorted((o1, o2) -> ThreadLocalRandom.current().nextInt(-1, 2)).findAny()
    // .orElse("");
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // return "IO :(";
    // }
    //
  }



  @Override
  public String

      help(String message) {
    return "eine traurige Bautstelle bis *DU* die neuschreibst";
  }

}

package keksdose.fwkib.modules;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.*;
import xyz.keksdose.keywordextraction.core.wordmetrics.scrabblescore.ScrabbleValue;

public class StringAlgorithmSupplier {
  public Double similarity(String algo, String left, String right) {
    if (algo.equals("fuzzy")) {
      return fuzzyMatch(left, right);
    }
    if (algo.equals("leven")) {
      return levenshteinMatch(left, right);
    }
    if (algo.equals("cosine")) {
      return cosineSimilarity(left, right);
    }
    if (algo.equals("jaro")) {
      return jaroWinklerSimilarity(left, right);
    }
    if (algo.equals("scrabble")) {
      return scrabbleScore(left, right);
    }
    if (algo.equals("random")) {
      return randomScore(left, right);
    }
    return levenshteinMatch(left, right);

  }

  private Double fuzzyMatch(String left, String right) {
    double d =
        1 / (Double.valueOf(new FuzzyScore(Locale.getDefault()).fuzzyScore(left, right)) + 1);
    return d;
  }

  private Double levenshteinMatch(String left, String right) {
    return Double.valueOf((LevenshteinDistance.getDefaultInstance().apply(left, right)));
  }

  private Double cosineSimilarity(String left, String right) {
    CosineSimilarity dist = new CosineSimilarity();
    Map<CharSequence, Integer> leftVector =
        Arrays.stream(left.split("")).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
    Map<CharSequence, Integer> rightVector =
        Arrays.stream(right.split("")).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
    return 1 - dist.cosineSimilarity(leftVector, rightVector);
  }

  private Double jaroWinklerSimilarity(String left, String right) {
    return 1 / (new JaroWinklerDistance().apply(left, right) + 1);
  }

  private Double scrabbleScore(String left, String right) {
    double d = Double.valueOf(Math.abs(Math.abs(ScrabbleValue.getValueForLetter(left))
        - Math.abs(ScrabbleValue.getValueForLetter(right))));
    System.out.println(d);
    return d;
  }
  
  private Double randomScore(String left, String right) {
    return ThreadLocalRandom.current().nextDouble();
  }
}

package keksdose.fwkib.modules;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import xyz.keksdose.keywordextraction.core.wordmetrics.scrabblescore.ScrabbleValue;

public class StringAlgorithmSupplier {
  private SimilarityAlgorithm algorithm = SimilarityAlgorithm.FUZZY;

  public Double similarity(String left, String right) {
    return algorithm.apply(left, right);
  }

  public boolean setAlgorithm(String algorithmName) {
    Optional<SimilarityAlgorithm> algo = SimilarityAlgorithm.getAlgorithmWithString(algorithmName);
    if (algo.isPresent()) {
      algorithm = algo.get();
      return true;
    } else {
      return false;
    }
  }

  private enum SimilarityAlgorithm implements BiFunction<String, String, Double> {
    FUZZY("fuzzy") {
      private FuzzyScore function = new FuzzyScore(Locale.getDefault());

      @Override
      public Double apply(String left, String right) {
        return 1d / (Double.valueOf(function.fuzzyScore(left, right)) + 1d);
      }

    },
    LEVENSHTEIN("leven") {
      @Override
      public Double apply(String left, String right) {
        return Double.valueOf((LevenshteinDistance.getDefaultInstance().apply(left, right)));
      }
    },
    COSINE("cosinus") {

      @Override
      public Double apply(String left, String right) {
        CosineSimilarity dist = new CosineSimilarity();
        Map<CharSequence, Integer> leftVector =
            Arrays.stream(left.split("")).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        Map<CharSequence, Integer> rightVector =
            Arrays.stream(right.split("")).collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
        return 1 - dist.cosineSimilarity(leftVector, rightVector);
      }
    },
    JARO_WINKLER("jaro") {
      @Override
      public Double apply(String left, String right) {
        return 1d / (new JaroWinklerDistance().apply(left, right) + 1d);
      }
    },
    SCRABBLE("scrabble") {
      @Override
      public Double apply(String left, String right) {
        return Double.valueOf(Math.abs(Math.abs(ScrabbleValue.getValueForLetter(left))
            - Math.abs(ScrabbleValue.getValueForLetter(right))));
      }
    },
    RANDOM("random") {
      private SecureRandom random = new SecureRandom();

      @Override
      public Double apply(String left, String right) {
        return random.nextDouble();
      }
    };

    private String algoName;

    public abstract Double apply(String left, String right);

    public String getName() {
      return algoName;
    }


    private SimilarityAlgorithm(String algoName) {
      this.algoName = algoName;
    }

    private static Optional<SimilarityAlgorithm> getAlgorithmWithString(String algo) {
      return Arrays.stream(SimilarityAlgorithm.values())
          .filter(v -> v.getName().equalsIgnoreCase(algo))
          .findAny();
    }
  }
}

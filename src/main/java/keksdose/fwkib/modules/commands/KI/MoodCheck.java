package keksdose.fwkib.modules.commands.ki;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.ejml.simple.SimpleMatrix;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import keksdose.fwkib.modules.Command;

public class MoodCheck implements Command {
  StanfordCoreNLP pipeline;

  @Override
  public String help(String message) {
    return "findet Zeitpunkte in englischen Texten.#parseDate $Text";
  }

  @Override
  public String apply(String message) {
    initialize();
    String result = getSentimentResult(message);

    return result.trim();
  }


  public void initialize() {
    Properties properties = new Properties();
    properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
    pipeline = new StanfordCoreNLP(properties);
  }

  public String getSentimentResult(String text) {
    StringBuilder result = new StringBuilder();
    SentimentClassification classification = new SentimentClassification();
    SentimentResult sentimentResult = new SentimentResult();
    if (text != null && text.length() > 0) {
      Annotation annotation = pipeline.process(text);
      for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
        // System.out.println(sentence);
        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
        // System.out.println(tree);
        SimpleMatrix simpleMatrix = RNNCoreAnnotations.getPredictions(tree);
        // System.out.println(simpleMatrix);
        classification.setVeryNegative((int) Math.round(simpleMatrix.get(0) * 100d));
        classification.setNegative((int) Math.round(simpleMatrix.get(1) * 100d));
        classification.setNeutral((int) Math.round(simpleMatrix.get(2) * 100d));
        classification.setPositive((int) Math.round(simpleMatrix.get(3) * 100d));
        classification.setVeryPositive((int) Math.round(simpleMatrix.get(4) * 100d));
        String setimentType = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
        sentimentResult.setSentimentType(setimentType);
        sentimentResult.setSentimentClass(classification);
        sentimentResult.setSentimentScore(RNNCoreAnnotations.getPredictedClass(tree));
        System.out.println(sentence.keySet().toString());
        result.append(sentimentResult.getSentimentType() + "  ");
      }
    }
    return result.toString();
  }
}


class SentimentClassification {
  /*
   * 
   * "Very negative" = 0
   * 
   * "Negative" = 1
   * 
   * "Neutral" = 2
   * 
   * "Positive" = 3
   * 
   * "Very positive" = 4
   * 
   */
  int veryPositive;
  int positive;
  int neutral;
  int negative;
  int veryNegative;

  public int getVeryPositive() {
    return veryPositive;
  }

  public void setVeryPositive(int veryPositive) {
    this.veryPositive = veryPositive;
  }

  public int getPositive() {
    return positive;
  }

  public void setPositive(int positive) {
    this.positive = positive;
  }

  public int getNeutral() {
    return neutral;
  }

  public void setNeutral(int neutral) {
    this.neutral = neutral;
  }

  public int getNegative() {
    return negative;
  }

  public void setNegative(int negative) {
    this.negative = negative;
  }

  public int getVeryNegative() {
    return veryNegative;
  }

  public void setVeryNegative(int veryNegative) {
    this.veryNegative = veryNegative;
  }
}


class SentimentResult {
  String sentimentType;
  int sentimentScore;
  SentimentClassification sentimentClass;

  public String getSentimentType() {
    return sentimentType;
  }

  public void setSentimentType(String sentimentType) {
    this.sentimentType = sentimentType;
  }

  public int getSentimentScore() {
    return sentimentScore;
  }

  public void setSentimentScore(int sentimentScore) {
    this.sentimentScore = sentimentScore;
  }

  public SentimentClassification getSentimentClass() {
    return sentimentClass;
  }

  public void setSentimentClass(SentimentClassification sentimentClass) {
    this.sentimentClass = sentimentClass;
  }
}

package keksdose.fwkib.modules.commands.KI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;
import keksdose.fwkib.modules.Command;

public class ParseDate implements Command {

    @Override
    public String help(String message) {
        return "findet Zeitpunkte in englischen Texten.#parseDate $Text";
    }

    @Override
    public String apply(String message) {
        String result = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Properties props = new Properties();
        AnnotationPipeline pipeline = new AnnotationPipeline();
        pipeline.addAnnotator(new TokenizerAnnotator(false));
        pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
        pipeline.addAnnotator(new POSTaggerAnnotator(false));
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));
        Annotation annotation = new Annotation(message);
        annotation.set(CoreAnnotations.DocDateAnnotation.class, df.format(new Date()));
        pipeline.annotate(annotation);
        System.out.println(annotation.get(CoreAnnotations.TextAnnotation.class));
        List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
        for (CoreMap cm : timexAnnsAll) {
            List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
            result += " " + cm.get(TimeExpression.Annotation.class).getTemporal().toString();
        }
        // https://nlp.stanford.edu/software/sutime.shtml#Download
        return result.trim();
    }

}

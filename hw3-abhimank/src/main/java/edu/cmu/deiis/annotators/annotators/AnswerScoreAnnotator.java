/**
 * 
 */
package edu.cmu.deiis.annotators.annotators;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.xmlbeans.impl.regex.Match;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Question;

/**
 * @author abhimank
 *
 */
public class AnswerScoreAnnotator extends JCasAnnotator_ImplBase {

  /*    
   * This is the main process function for AnswerScoreAnnotator Class.
   * The Scoring function used here is (number of correct answers in top N)/N
   * where N is the total number of correct answer.
   * 
   * The ranking of the answers is decided by the ngram matches between question  and the 
   * answer. The one with the highest ngram matches is the highest scorer
   * 
   * */

  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    FSIndex questionIndex = jcas.getAnnotationIndex(Question.type);
    Iterator questionIter = questionIndex.iterator();
    
//    Vector<NGram> questionNgrams = new Vector<NGram>();
    Question ques = (Question) questionIter.next();

    FSIndex answerIndex = jcas.getAnnotationIndex(Answer.type);
    Iterator answerIterator = answerIndex.iterator();
    HashMap<Answer,Vector<NGram>> answerMap = new HashMap<Answer,Vector<NGram>>();

    while(answerIterator.hasNext()){
      Answer ans = (Answer) answerIterator.next();
      Vector<NGram> answerNgrams = new Vector<NGram>();
      answerMap.put(ans, answerNgrams);
    }
    
    int numCorrectAnswers = 0;
    
    FSIndex nGramsIndex = jcas.getAnnotationIndex(NGram.type);
    Iterator nGramIterator = nGramsIndex.iterator();
    
    while(nGramIterator.hasNext()){
      NGram ngram = (NGram) nGramIterator.next();
//      if(ques.getBegin()<=ngram.getBegin() && ques.getEnd()>=ngram.getEnd())
//        questionNgrams.add(ngram);
      for(Answer answer : answerMap.keySet()){
        if(answer.getBegin()<=ngram.getBegin() && answer.getEnd()>=ngram.getEnd()){
          answerMap.get(answer).add(ngram);
        }
      }
    }
    
    HashMap<Answer, Double> scoreMap = new HashMap<Answer, Double>();
    String quesText = ques.getCoveredText().replace("?", "");
    for(Answer answer: answerMap.keySet()){
      double answerScore =0;
      Iterator<NGram> nGramAnsIterator = answerMap.get(answer).iterator();
      while(nGramAnsIterator.hasNext()){
        NGram ngram = nGramAnsIterator.next();
        String ngramText = ngram.getCoveredText().replace(".", "");
        Pattern pattern = Pattern.compile(ngramText);
        Matcher matcher = pattern.matcher(quesText);
        while(matcher.find())
          answerScore++;
      }
      scoreMap.put(answer, answerScore);
      AnswerScore ansScore = new AnswerScore(jcas);
      ansScore.setAnswer(answer);
      if(answer.getIsCorrect())
        numCorrectAnswers++;
      ansScore.setScore(answerScore);
      ansScore.setBegin(answer.getBegin());
      ansScore.setEnd(answer.getEnd());
      ansScore.addToIndexes();
    }
    ValueComparator bvc =  new ValueComparator(scoreMap);
    TreeMap<Answer,Double> sorted_map = new TreeMap<Answer,Double>(bvc);
    sorted_map.putAll(scoreMap);
    int numPredictedCorrect =0;
    Iterator<Answer> sortedAnsIter = sorted_map.keySet().iterator();
    for(int i=0; i<numCorrectAnswers; ++i){
      Answer answer = sortedAnsIter.next();
      if(answer.getIsCorrect())
        numPredictedCorrect++;
//      System.out.println(answer.getIsCorrect() +" "+ sorted_map.get(answer));
    }
    System.out.println("Precision: "+ 1.0*numPredictedCorrect/numCorrectAnswers);
//    System.out.println("results: "+sorted_map);
    
  }

  class ValueComparator implements Comparator<Answer> {

    Map<Answer, Double> base;
    public ValueComparator(Map<Answer, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Answer a, Answer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
  
}

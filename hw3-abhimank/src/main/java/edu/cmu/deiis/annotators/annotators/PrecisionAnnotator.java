/**
 * 
 */
package edu.cmu.deiis.annotators.annotators;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.annotators.annotators.AnswerScoreAnnotator.ValueComparator;
import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Precision;
import edu.cmu.deiis.types.Question;

/**
 * @author abhimank
 *
 */
public class PrecisionAnnotator extends JCasAnnotator_ImplBase {

  /* (non-Javadoc)
   * @see org.apache.uima.analysis_component.JCasAnnotator_ImplBase#process(org.apache.uima.jcas.JCas)
   */
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    
    FSIndex ansScoreIndex = jcas.getAnnotationIndex(AnswerScore.type);
    Iterator ansScoreIter = ansScoreIndex.iterator();
    int numCorrectAnswers = 0;
    int numPredictedCorrect =0;
    HashMap<Answer, Double> scoreMap = new HashMap<Answer, Double>();
    while(ansScoreIter.hasNext()){
      AnswerScore ansScore = (AnswerScore)ansScoreIter.next();
      Answer answer = ansScore.getAnswer();
      if(answer.getIsCorrect())
        numCorrectAnswers++;
      scoreMap.put(answer, ansScore.getScore());
    }
    
    ValueComparator bvc =  new ValueComparator(scoreMap);
    TreeMap<Answer,Double> sorted_map = new TreeMap<Answer,Double>(bvc);
    sorted_map.putAll(scoreMap);
    Iterator<Answer> sortedAnsIter = sorted_map.keySet().iterator();
    
    for(int i=0; i<numCorrectAnswers; ++i){
      Answer answer = sortedAnsIter.next();
      if(answer.getIsCorrect())
        numPredictedCorrect++;
//      System.out.println(answer.getIsCorrect() +" "+ sorted_map.get(answer));
    }
    
    double precDouble = 1.0*numPredictedCorrect/numCorrectAnswers;
    System.out.println("Precision: "+ 1.0*numPredictedCorrect/numCorrectAnswers);
    Precision precision = new Precision(jcas);
    precision.setBegin(0);
    precision.setEnd(jcas.getDocumentText().length());
    precision.setPrecision(precDouble);
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

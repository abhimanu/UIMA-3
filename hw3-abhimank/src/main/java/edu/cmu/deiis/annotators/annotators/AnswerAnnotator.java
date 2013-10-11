/**
 * 
 */
package edu.cmu.deiis.annotators.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;

/**
 * @author abhimank
 *
 */
public class AnswerAnnotator extends JCasAnnotator_ImplBase {

  /* 
   * This is the proces method for answer annotator.
   * The only significant part set here apart from the usual mundane Annotation member setting is
   * setting of the member isCorrect.
   */
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    int prevIndex = 0; //Answer 
    for(String line: jcas.getDocumentText().split("\\n")){
      if(prevIndex == 0){
        prevIndex += (line.length()+1);
        continue;
      }
      Answer answer = new Answer(jcas);
      answer.setBegin(prevIndex+4);
      answer.setEnd(prevIndex+line.length());
      prevIndex += line.length()+1;
      int score = new Integer(line.substring(2, 3));
      answer.setIsCorrect(score==1);
      answer.addToIndexes();
    }
  }

}

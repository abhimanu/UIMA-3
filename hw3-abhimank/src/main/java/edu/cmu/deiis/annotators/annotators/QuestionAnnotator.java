/**
 * 
 */
package edu.cmu.deiis.annotators.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Question;

/**
 * @author abhimank
 *
 */
public class QuestionAnnotator extends JCasAnnotator_ImplBase {

  /*
   * This is the main process class for the QuestionAnnotator. 
   * We dont sent anyother member apart from the base class's begin and end
   *    
   */
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String line = jcas.getDocumentText().split("\\n")[0];
    Question question = new Question(jcas);
    question.setBegin(2);
    question.setEnd(line.length());
    question.addToIndexes();
  }

}

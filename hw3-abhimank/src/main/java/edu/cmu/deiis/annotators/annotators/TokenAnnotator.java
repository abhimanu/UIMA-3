/**
 * 
 */
package edu.cmu.deiis.annotators.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Token;

/**
 * @author abhimank
 *
 */
public class TokenAnnotator extends JCasAnnotator_ImplBase {

  /*
   * This is he main process class for the TokenAnnotator class.
   * We get the tokens here from the raw text.
   * We make the assumption that there is just one white space between two adjacent tokens
   * We split the text first one the basis of the carriage return and then white spaces
   * We update the begin and end superclass members according;y
   * 
   */
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    int prevLineNum = 0; //Answer 
    for(String line: jcas.getDocumentText().split("\\n")){
      int startIndex=prevLineNum+4;
      if(prevLineNum==0){
        startIndex = prevLineNum+2; //Question first line
        line = line.substring(2);
      }else{
        line = line.substring(4);
      }
      int currentIndex = startIndex;
      for(String tokenString: line.split("\\s+")){
        int originalLength  = tokenString.length();
        /*tokenString = tokenString.replaceAll("\\.$", "");
        tokenString = tokenString.replaceAll("\\?$", "");
        */
//        System.out.println(currentIndex + " " + startIndex + " " + originalLength) ;
        Token annotation = new Token(jcas);
        annotation.setBegin(currentIndex);
        currentIndex += originalLength;
//        System.out.println(currentIndex + " " + startIndex + " " + originalLength) ;
        annotation.setEnd(currentIndex);
        annotation.addToIndexes();
        currentIndex+=1;
      }
      prevLineNum  = currentIndex+1;//+= line.length();
    }


  }

}

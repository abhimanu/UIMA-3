

/* First created by JCasGen Thu Oct 10 23:22:59 EDT 2013 */
package edu.cmu.deiis.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** Precision type
 * Updated by JCasGen Thu Oct 10 23:22:59 EDT 2013
 * XML source: /home/abhimank/git/hw3-abhimank/hw3-abhimank/src/main/resources/descriptors/deiis_types.xml
 * @generated */
public class Precision extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Precision.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Precision() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Precision(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Precision(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Precision(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: precision

  /** getter for precision - gets Precision attribute
   * @generated */
  public double getPrecision() {
    if (Precision_Type.featOkTst && ((Precision_Type)jcasType).casFeat_precision == null)
      jcasType.jcas.throwFeatMissing("precision", "edu.cmu.deiis.types.Precision");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Precision_Type)jcasType).casFeatCode_precision);}
    
  /** setter for precision - sets Precision attribute 
   * @generated */
  public void setPrecision(double v) {
    if (Precision_Type.featOkTst && ((Precision_Type)jcasType).casFeat_precision == null)
      jcasType.jcas.throwFeatMissing("precision", "edu.cmu.deiis.types.Precision");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Precision_Type)jcasType).casFeatCode_precision, v);}    
  }

    
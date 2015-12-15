

/* First created by JCasGen Tue Dec 15 19:51:42 CET 2015 */

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Tue Dec 15 20:05:34 CET 2015
 * XML source: /home/dominik/Dokumente/BA/BA_GIT/BA_dl/dlawatsch/src/main/resources/desc/type/SequenceID.xml
 * @generated */
public class SequenceID extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SequenceID.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected SequenceID() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public SequenceID(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public SequenceID(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public SequenceID(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: ID

  /** getter for ID - gets 
   * @generated
   * @return value of the feature 
   */
  public int getID() {
    if (SequenceID_Type.featOkTst && ((SequenceID_Type)jcasType).casFeat_ID == null)
      jcasType.jcas.throwFeatMissing("ID", "SequenceID");
    return jcasType.ll_cas.ll_getIntValue(addr, ((SequenceID_Type)jcasType).casFeatCode_ID);}
    
  /** setter for ID - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setID(int v) {
    if (SequenceID_Type.featOkTst && ((SequenceID_Type)jcasType).casFeat_ID == null)
      jcasType.jcas.throwFeatMissing("ID", "SequenceID");
    jcasType.ll_cas.ll_setIntValue(addr, ((SequenceID_Type)jcasType).casFeatCode_ID, v);}    
   
    
  //*--------------*
  //* Feature: NrOfTokens

  /** getter for NrOfTokens - gets 
   * @generated
   * @return value of the feature 
   */
  public int getNrOfTokens() {
    if (SequenceID_Type.featOkTst && ((SequenceID_Type)jcasType).casFeat_NrOfTokens == null)
      jcasType.jcas.throwFeatMissing("NrOfTokens", "SequenceID");
    return jcasType.ll_cas.ll_getIntValue(addr, ((SequenceID_Type)jcasType).casFeatCode_NrOfTokens);}
    
  /** setter for NrOfTokens - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setNrOfTokens(int v) {
    if (SequenceID_Type.featOkTst && ((SequenceID_Type)jcasType).casFeat_NrOfTokens == null)
      jcasType.jcas.throwFeatMissing("NrOfTokens", "SequenceID");
    jcasType.ll_cas.ll_setIntValue(addr, ((SequenceID_Type)jcasType).casFeatCode_NrOfTokens, v);}    
   
    
  //*--------------*
  //* Feature: Beginn

  /** getter for Beginn - gets 
   * @generated
   * @return value of the feature 
   */
  public int getBeginn() {
    if (SequenceID_Type.featOkTst && ((SequenceID_Type)jcasType).casFeat_Beginn == null)
      jcasType.jcas.throwFeatMissing("Beginn", "SequenceID");
    return jcasType.ll_cas.ll_getIntValue(addr, ((SequenceID_Type)jcasType).casFeatCode_Beginn);}
    
  /** setter for Beginn - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setBeginn(int v) {
    if (SequenceID_Type.featOkTst && ((SequenceID_Type)jcasType).casFeat_Beginn == null)
      jcasType.jcas.throwFeatMissing("Beginn", "SequenceID");
    jcasType.ll_cas.ll_setIntValue(addr, ((SequenceID_Type)jcasType).casFeatCode_Beginn, v);}    
   
    
  //*--------------*
  //* Feature: End

  /** getter for End - gets 
   * @generated
   * @return value of the feature 
   */
  public int getEnd() {
    if (SequenceID_Type.featOkTst && ((SequenceID_Type)jcasType).casFeat_End == null)
      jcasType.jcas.throwFeatMissing("End", "SequenceID");
    return jcasType.ll_cas.ll_getIntValue(addr, ((SequenceID_Type)jcasType).casFeatCode_End);}
    
  /** setter for End - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setEnd(int v) {
    if (SequenceID_Type.featOkTst && ((SequenceID_Type)jcasType).casFeat_End == null)
      jcasType.jcas.throwFeatMissing("End", "SequenceID");
    jcasType.ll_cas.ll_setIntValue(addr, ((SequenceID_Type)jcasType).casFeatCode_End, v);}    
  }

    
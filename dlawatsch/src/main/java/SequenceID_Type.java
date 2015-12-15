
/* First created by JCasGen Tue Dec 15 19:51:42 CET 2015 */

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Tue Dec 15 20:05:34 CET 2015
 * @generated */
public class SequenceID_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (SequenceID_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = SequenceID_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new SequenceID(addr, SequenceID_Type.this);
  			   SequenceID_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new SequenceID(addr, SequenceID_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = SequenceID.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("SequenceID");
 
  /** @generated */
  final Feature casFeat_ID;
  /** @generated */
  final int     casFeatCode_ID;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getID(int addr) {
        if (featOkTst && casFeat_ID == null)
      jcas.throwFeatMissing("ID", "SequenceID");
    return ll_cas.ll_getIntValue(addr, casFeatCode_ID);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setID(int addr, int v) {
        if (featOkTst && casFeat_ID == null)
      jcas.throwFeatMissing("ID", "SequenceID");
    ll_cas.ll_setIntValue(addr, casFeatCode_ID, v);}
    
  
 
  /** @generated */
  final Feature casFeat_NrOfTokens;
  /** @generated */
  final int     casFeatCode_NrOfTokens;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getNrOfTokens(int addr) {
        if (featOkTst && casFeat_NrOfTokens == null)
      jcas.throwFeatMissing("NrOfTokens", "SequenceID");
    return ll_cas.ll_getIntValue(addr, casFeatCode_NrOfTokens);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setNrOfTokens(int addr, int v) {
        if (featOkTst && casFeat_NrOfTokens == null)
      jcas.throwFeatMissing("NrOfTokens", "SequenceID");
    ll_cas.ll_setIntValue(addr, casFeatCode_NrOfTokens, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Beginn;
  /** @generated */
  final int     casFeatCode_Beginn;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getBeginn(int addr) {
        if (featOkTst && casFeat_Beginn == null)
      jcas.throwFeatMissing("Beginn", "SequenceID");
    return ll_cas.ll_getIntValue(addr, casFeatCode_Beginn);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setBeginn(int addr, int v) {
        if (featOkTst && casFeat_Beginn == null)
      jcas.throwFeatMissing("Beginn", "SequenceID");
    ll_cas.ll_setIntValue(addr, casFeatCode_Beginn, v);}
    
  
 
  /** @generated */
  final Feature casFeat_End;
  /** @generated */
  final int     casFeatCode_End;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getEnd(int addr) {
        if (featOkTst && casFeat_End == null)
      jcas.throwFeatMissing("End", "SequenceID");
    return ll_cas.ll_getIntValue(addr, casFeatCode_End);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEnd(int addr, int v) {
        if (featOkTst && casFeat_End == null)
      jcas.throwFeatMissing("End", "SequenceID");
    ll_cas.ll_setIntValue(addr, casFeatCode_End, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public SequenceID_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_ID = jcas.getRequiredFeatureDE(casType, "ID", "uima.cas.Integer", featOkTst);
    casFeatCode_ID  = (null == casFeat_ID) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ID).getCode();

 
    casFeat_NrOfTokens = jcas.getRequiredFeatureDE(casType, "NrOfTokens", "uima.cas.Integer", featOkTst);
    casFeatCode_NrOfTokens  = (null == casFeat_NrOfTokens) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_NrOfTokens).getCode();

 
    casFeat_Beginn = jcas.getRequiredFeatureDE(casType, "Beginn", "uima.cas.Integer", featOkTst);
    casFeatCode_Beginn  = (null == casFeat_Beginn) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Beginn).getCode();

 
    casFeat_End = jcas.getRequiredFeatureDE(casType, "End", "uima.cas.Integer", featOkTst);
    casFeatCode_End  = (null == casFeat_End) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_End).getCode();

  }
}



    
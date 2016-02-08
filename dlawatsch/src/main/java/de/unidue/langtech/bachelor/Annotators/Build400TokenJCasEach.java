package de.unidue.langtech.bachelor.Annotators;



import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.uima.UIMAException;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
/*
 * As there have been problems concerning computational time
 * (see implementation chapter and discussion)
 * the amount of words per jcas is reduced to 400 by this class
 */
public class Build400TokenJCasEach extends JCasAnnotator_ImplBase{
	
	public static int sequenceID;
	public static List<JCas> allJcas = new ArrayList<JCas>();
	List<String> allWords = new ArrayList<String>();
	List<String> allLemma = new ArrayList<String>();
	List<String> allPOS = new ArrayList<String>();
	List<String> allSentences = new ArrayList<String>();
	boolean hasLemmas;
	
    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected String language;
    
    List<JCas> allJCas = new ArrayList<JCas>();
    String documentText;
    int sequenceIDcount;
    File file;
    Writer output;
	int numberOfTokens;
	
	public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);      
            hasLemmas=false;
            
        }   
    
    
    
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		numberOfTokens = 0;
		documentText = "";
		sequenceID = 0;
		

		try {
			/*
			 * until 400 words have been processed or the document end is reached
			 * the sentences are stored in an array as well as pos and lemmas to
			 * annotate them to a new jcas afterwards.
			 */
        	for (Sentence sentance : JCasUtil.select(jcas, Sentence.class)) {
				
				if(numberOfTokens < 400 || sentance.getEnd() < jcas.getDocumentText().length()){						
					documentText+= sentance.getCoveredText();					
					allSentences.add(sentance.getCoveredText());

			        for (Token t : JCasUtil.selectCovered(jcas, Token.class, sentance)) {
			        	numberOfTokens++;
			        	allWords.add(t.getCoveredText());	
			        	//if the language has lemmas those have to be processed too
					        if(t.getLemma() != null){
					        	hasLemmas = true;
					        	allLemma.add(t.getLemma().getCoveredText());					        	
					        }
					    allPOS.add(t.getPos().getPosValue());    					        					                 					             
			        }
			        sentance.getCoveredText();					
				}

				/*
				 * if 400 tokens are reached they will be processed to a new jcas.
				 * the current sentence gets included as well to not lose data
				 */
				if(numberOfTokens >= 400 || sentance.getEnd() == jcas.getDocumentText().length()){
					documentText+= sentance.getCoveredText();					
					allSentences.add(sentance.getCoveredText());

			        for (Token t : JCasUtil.selectCovered(jcas, Token.class, sentance)) {		           
			        	allWords.add(t.getCoveredText());			        	
					        if(t.getLemma() != null){
					        	allLemma.add(t.getLemma().getCoveredText());					        	
					        }
					    allPOS.add(t.getPos().getPosValue());    					        					                 					             
			        }   
			        annotationProcess(documentText);
				}
        	}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*
	 * a new jcas is created and annotated with the 400 tokens from above
	 */
	private void annotationProcess(String txt) {
		try {
			JCas out = JCasFactory.createJCas();
	    	DocumentMetaData meta = DocumentMetaData.create(out);
			UUID uniqueID = UUID.randomUUID();	
	    	meta.setDocumentId("FILE_" + String.valueOf(uniqueID));
	    	out.setDocumentLanguage(language);
	    	out.setDocumentText(txt);
	    	int sentenceBeginn = 0;
			int sentenceEnd = 0;
			
			for(String sentance : allSentences){
				sentenceEnd += sentance.length();
				Sentence s = new Sentence(out, sentenceBeginn, sentenceEnd);
				sentenceBeginn += sentance.length();
				s.addToIndexes();
			}

			int wordBeginn = 0;
			int wordEnd = 0;
			int posCount = 0;
			
	        for (Sentence se : JCasUtil.select(out, Sentence.class)) {
	        	while(wordEnd < se.getEnd()){
	            	
	            	String word = allWords.get(posCount);
	        	
	        		wordEnd += word.length();
	        		Token token = new Token(out, wordBeginn, wordEnd);
	        		wordBeginn += word.length()+1;
	        		wordEnd++;       		        		    		        		                          
	                if(hasLemmas == true){
		                Lemma lemma = new Lemma(out);
		                lemma.setValue(allLemma.get(posCount));
		                lemma.addToIndexes(out);
				        token.setLemma(lemma);
	                }	               
			        POS pos = new POS(out);
			        pos.setPosValue(allPOS.get(posCount));
			        pos.addToIndexes(out);
			        
			        token.setPos(pos);

			        token.addToIndexes(out);  
	        
	        		posCount++;             		
	            }  
	        }
	        
	        /*
	         * all arrays are cleaned to be ready for the next document
	         */
			allWords.clear();
			allLemma.clear();
			allPOS.clear();
			allSentences.clear();
			documentText = "";
			numberOfTokens = 0;
			
			/*
			 * Because the german corpus contains only 1 big file it has to be handled seperately
			 * because this file is that big that storing all newly created JCas's first and write
			 * them to Binary Files later would exceed Memory Limit of 9gb! 
			 * Therefore, it gets directly saved as Binary (which is not a nice method :/ )
			 */
			if(language.equals("GERMAN")){
				SequenceIdAnnotator.processSingleFile(out);
		        System.out.println(meta.getDocumentId() + "PROCESSING");

			}else{
				allJcas.add(out);
			}
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}

package de.unidue.langtech.bachelor.Annotators;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import de.unidue.langtech.bachelor.type.SequenceID;

public class Build400TokenJCasEach extends JCasAnnotator_ImplBase{
	
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
		try {

        	for (Sentence sentance : JCasUtil.select(jcas, Sentence.class)) {
				
				if(numberOfTokens < 400 || sentance.getEnd() < jcas.getDocumentText().length()){						
					documentText+= sentance.getCoveredText();					
					allSentences.add(sentance.getCoveredText());

			        for (Token t : JCasUtil.selectCovered(jcas, Token.class, sentance)) {
			        	numberOfTokens++;
			        	allWords.add(t.getCoveredText());			        	
					        if(t.getLemma() != null){
					        	hasLemmas = true;
					        	allLemma.add(t.getLemma().getCoveredText());					        	
					        }
					    allPOS.add(t.getPos().getPosValue());    					        					                 					             
			        }
			        sentance.getCoveredText();					
				}

				
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
	        
			allWords.clear();
			allLemma.clear();
			allPOS.clear();
			allSentences.clear();
			documentText = "";
			numberOfTokens = 0;
	        System.out.println(meta.getDocumentId() + " META");
			allJcas.add(out);
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}

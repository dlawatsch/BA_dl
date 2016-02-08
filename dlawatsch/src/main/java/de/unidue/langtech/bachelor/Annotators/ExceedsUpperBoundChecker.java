package de.unidue.langtech.bachelor.Annotators;



import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;


import de.unidue.langtech.bachelor.reader.BNCReader;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.NKJPReader;
import de.unidue.langtech.bachelor.reader.SloveneReader;
import de.unidue.langtech.bachelor.type.SequenceID;

/*
 * this class checks if over 1 million tokens have been processed.
 * if this is the case the reader gets informed to stop reading files 
 * at the next iteration
 */

public class ExceedsUpperBoundChecker extends JCasAnnotator_ImplBase{
    
    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected String language;
    
    
    int processedTokenCount;
    int upperBound = 1000000;
    
	public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);          
            processedTokenCount = 0;
        }   
    
    
    
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
       /*
        * as the counted words oer sentence already exist in the annotated sequence ID type
        * the processing is faster than counting each token via JCasUtil.select
        */
		
		if(language.equals("POLNISH")){
        for (SequenceID sid : JCasUtil.select(jcas, SequenceID.class)){
        	processedTokenCount += (sid.getNrOfTokens());
        }
			
        if(processedTokenCount >= upperBound){
        	/*
        	 * The reader has a boolean reachedUpperBound which influences the 
        	 * hasnext method. If false, the processing of new files is stopped
        	 */
        	System.out.println("[UPPER BOUND EXCEEDED: "+ processedTokenCount + " Tokens annotated]");
        	if(language.equals("POLNISH")){
            	NKJPReader.reachedUpperBound = true;
        	}
        }		
	}else{
		/*
		 * all other languages need to be processed differently 
		 * compared to polish which represents the generalized solution
		 */
        for(JCas out : Build400TokenJCasEach.allJcas){
        	for (SequenceID sid : JCasUtil.select(out, SequenceID.class)){
        		processedTokenCount += (sid.getNrOfTokens());
        	}
			
        if(processedTokenCount >= upperBound){
        	System.out.println("[UPPER BOUND EXCEEDED: "+ processedTokenCount + " Tokens annotated]");
        	if(language.equals("ISLANDIC")){
            	IslandicCorpusReader.reachedUpperBound = true;
        	}
        	if(language.equals("ENGLISH")){
            	BNCReader.reachedUpperBound = true;
        	}
        	if(language.equals("SLOVENE")){
            	SloveneReader.reachedUpperBound = true;
        	}
        }	
        }
	}
	}    
}

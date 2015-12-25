package de.unidue.langtech.bachelor.Annotators;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;


import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unidue.langtech.bachelor.reader.BNCReader;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.LatinReader;
import de.unidue.langtech.bachelor.reader.NKJPReader;
import de.unidue.langtech.bachelor.reader.SloveneReader;
import de.unidue.langtech.bachelor.type.SequenceID;



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
        if(language.equals("POLNISH")){
        for (SequenceID sid : JCasUtil.select(jcas, SequenceID.class)){
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
        	if(language.equals("LATIN")){
            	LatinReader.reachedUpperBound = true;
        	}
        	if(language.equals("POLNISH")){
            	NKJPReader.reachedUpperBound = true;
        	}
        	if(language.equals("SLOVENE")){
            	SloveneReader.reachedUpperBound = true;
        	}
        }		
	}else{
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
        	if(language.equals("LATIN")){
            	LatinReader.reachedUpperBound = true;
        	}
        	if(language.equals("POLNISH")){
            	NKJPReader.reachedUpperBound = true;
        	}
        	if(language.equals("SLOVENE")){
            	SloveneReader.reachedUpperBound = true;
        	}
        }	
        }
	}
	}    
}

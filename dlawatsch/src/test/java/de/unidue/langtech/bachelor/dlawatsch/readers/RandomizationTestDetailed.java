package de.unidue.langtech.bachelor.dlawatsch.readers;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.type.SequenceID;

public class RandomizationTestDetailed {
	@Test
	public void testRandomizationExtended() throws Exception{
		/*
		/*
		 * Icelandic Reader was used because generating test files
		 * for this format is easier than for the other readers
		 */
		CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
				IslandicCorpusReader.class,
				IslandicCorpusReader.PARAM_SOURCE_LOCATION, "src/test/resources/reader/",
				IslandicCorpusReader.PARAM_PATTERNS, "randomizationTest.txt"
				);
		
		for(JCas jcas : new JCasIterable(reader)){
	        //Change this maximumToken value to change amount of tokens that should be processed
			//as the test file contains 18 tokens (6 tokens per sentence) two sequence id's should be chosen by the randomizer
	        int maximumToken = 12;
			
		    Random randomGenerator = new Random();
			List<String> allSequenceIDs = new ArrayList<String>();
			int currentTokenCount = 0;
			Set<Integer> randomizedSID = new HashSet<Integer>();
	        FileReader fr;
	        
	        //read sequences
	        try {
				fr = new FileReader("src/test/resources/RandomizationTest/LANGUAGES/POLNISH/SEQUENCES/SEQUENCE_ID.txt");
	            BufferedReader br = new BufferedReader(fr);
	            String currentline;
	            while((currentline = br.readLine()) != null){
	            	allSequenceIDs.add(currentline);
	            }
	            br.close();
	            int allToken = 0;
	            for(String l : allSequenceIDs){
	            	String[] parts = l.split(" ");
	            	allToken += Integer.valueOf(parts[1]);
	            }
	            
	            //check if wrong value was set
	            if(maximumToken > allToken){
	            	System.out.println("[NOT ENOUGH TOKEN IN DOCUMENT! " + allToken + " AVAILABLE | " + maximumToken + " CHOSEN]");
	            	System.out.println("[REDUCED USE_X_MAX_TOKEN TO " + allToken+"]");
	            	maximumToken = allToken;
	            }
	            
	            //chose random item until amount of tokens equals your chosen maximumToken integer
	            while(currentTokenCount < maximumToken){
		            int index = randomGenerator.nextInt(allSequenceIDs.size());
		            String currentSID = allSequenceIDs.get(index);
		            String[] parts = currentSID.split(" ");
		            
		            int sequenceID = Integer.valueOf(parts[0]);
		            int tokenInSID = Integer.valueOf(parts[1]);
		            
		            if(randomizedSID.add(sequenceID)){
		            	 currentTokenCount += tokenInSID;
		            }		           
	            }
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        //if maximumToken is 0, the sentence with smallest amount of token is used
			Sentence minsentence = null;
			int mintoken = Integer.MAX_VALUE;
			
			//iterate over all sentences and search for sequence IDs that have been chosen by the randomizer
	        for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
	        	
	        	for (SequenceID sid : JCasUtil.selectCovering(jcas, SequenceID.class, sentence)){
	        		if(sid.getNrOfTokens() < mintoken){
	        			mintoken = sid.getNrOfTokens();
	        			minsentence = sentence;
	        		}
	        		if(randomizedSID.contains(sid.getID())){
	        			randomizedSID.remove(sid.getID());
	        			System.out.println("[ANNOTATING SENTENCE ID: " + sid.getID() + "]");
	        			//HERE THE ANNOTATION PROCESS CAN BE STARTED! (left out because not interesting for the test)
	        		}
				}
			}
	        assertEquals(2, randomizedSID.size());
		}
	}	
}	

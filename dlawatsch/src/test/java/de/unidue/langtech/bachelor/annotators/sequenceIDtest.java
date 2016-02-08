package de.unidue.langtech.bachelor.annotators;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.unidue.langtech.bachelor.Annotators.SequenceIdAnnotator;

public class sequenceIDtest {
	@Test
	public void testBasicInformationAnnotator() throws Exception{
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
			
			for (Sentence se : JCasUtil.select(jcas, Sentence.class)) {
				System.out.println(se.getCoveredText());
			}
			/*
			 * Polish is used here because all other languages process files stored in the Build400TokenJCasEach annotator.
			 * Therefore, the handling of Polish files depicts the generalized standard process.
			 */
			
			AnalysisEngineDescription sidAnnotator = AnalysisEngineFactory.createEngineDescription(SequenceIdAnnotator.class,
					SequenceIdAnnotator.PARAM_CORPUSLOCATION, "src/test/resources/RandomizationTest/", 
            		SequenceIdAnnotator.PARAM_LANGUAGE, "POLNISH");
			AnalysisEngine detectionEngine = AnalysisEngineFactory.createEngine(sidAnnotator);
			detectionEngine.process(jcas);

			
			FileReader fr = new FileReader("src/test/resources/RandomizationTest/LANGUAGES/POLNISH/SEQUENCES/SEQUENCE_ID.txt");
            BufferedReader br = new BufferedReader(fr);
            String currentline;
            
            int sentenceID = 0;
            int tokenPerSequence = 6;
            
            //check if the sequences were annotated correctly with 6 tokens per sequence and 4 sequence ids (starting at 0)
            while((currentline = br.readLine()) != null){
            	assertEquals(currentline, sentenceID +" "+tokenPerSequence);
            	sentenceID++;
            }
            br.close();		
		}   
	}
}	

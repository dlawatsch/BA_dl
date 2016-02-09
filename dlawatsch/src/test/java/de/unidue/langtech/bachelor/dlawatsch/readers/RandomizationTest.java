package de.unidue.langtech.bachelor.dlawatsch.readers;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.unidue.langtech.bachelor.reader.BinaryReaderRandomization;


public class RandomizationTest {
	@Test
	public void testBasicInformationAnnotator() throws Exception{
		/*
		 * This test uses a binary created out of the randomizationTest.txt
		 * and a SequenceID.txt (from the SequenceIDTest)
		 * There are 4 Sentences stored in this binary,
		 * you can change the PARAM_USE_X_MAX_TOKEN and the output in the console
		 * where you can see that the chosen sentences for annotation are randomized
		 */
		CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
				BinaryReaderRandomization.class,
        		BinaryReaderRandomization.PARAM_SOURCE_LOCATION, "src/test/resources/RandomizationTest/LANGUAGES/POLNISH/BINARIES/",
        		BinaryReaderRandomization.PARAM_CORPUSLOCATION, "src/test/resources/RandomizationTest/",
				BinaryReaderRandomization.PARAM_PATTERNS, "testBinary.bin",
				BinaryReaderRandomization.PARAM_LANGUAGE, "POLNISH",
				BinaryReaderRandomization.PARAM_USE_X_MAX_TOKEN, "18",
				BinaryReaderRandomization.PARAM_COARSEGRAINED, "false"
				);

		for(JCas jcas : new JCasIterable(reader)){	
		}
	}
}	

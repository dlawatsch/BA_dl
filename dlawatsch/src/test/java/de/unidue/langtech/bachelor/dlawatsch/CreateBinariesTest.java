package de.unidue.langtech.bachelor.dlawatsch;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.junit.Test;

import de.unidue.langtech.bachelor.Annotators.ExceedsUpperBoundChecker;
import de.unidue.langtech.bachelor.Annotators.SequenceIdAnnotator;
import de.unidue.langtech.bachelor.Annotators.WriteBinJcas;
import de.unidue.langtech.bachelor.reader.NKJPReader;

public class CreateBinariesTest {
    @Test
    public void testBinarieCreation()throws Exception
    {
    	String corpusLocation = "src/main/resources/Binaries/polish/**/";
    	String language = "POLISH";
		SimplePipeline.runPipeline(
	            CollectionReaderFactory.createReader(
	                    NKJPReader.class,
	                    NKJPReader.PARAM_SOURCE_LOCATION, corpusLocation,
	                    NKJPReader.PARAM_PATTERNS, "ann_words.xml"
	            ),
	            AnalysisEngineFactory.createEngineDescription(SequenceIdAnnotator.class, SequenceIdAnnotator.PARAM_CORPUSLOCATION, corpusLocation, 
	            		SequenceIdAnnotator.PARAM_LANGUAGE, language),
	            AnalysisEngineFactory.createEngineDescription(ExceedsUpperBoundChecker.class,
	            		ExceedsUpperBoundChecker.PARAM_LANGUAGE, language),
	            AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, language, 
						WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
    }
}

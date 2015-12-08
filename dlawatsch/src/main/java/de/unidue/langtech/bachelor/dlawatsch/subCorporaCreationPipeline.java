package de.unidue.langtech.bachelor.dlawatsch;

import java.io.File;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;


	public class subCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                            IslandicCorpusReader.class,
	                            IslandicCorpusReader.PARAM_SOURCE_LOCATION, "/home/dominikl/Dokumente/BA/CORPORA/ICELANDIC_GOLD/MIM-GOLD_0.9/",
	                            IslandicCorpusReader.PARAM_PATTERNS, "*.txt"
	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

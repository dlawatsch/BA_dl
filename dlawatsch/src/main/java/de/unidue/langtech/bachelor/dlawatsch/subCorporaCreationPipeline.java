package de.unidue.langtech.bachelor.dlawatsch;

import java.io.File;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.unidue.langtech.bachelor.dlawatsch.IslandicCorpusReader;

	public class subCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                            LatinReader.class,
	                            LatinReader.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA",
	                            LatinReader.PARAM_PATTERNS, "latin.txt"
	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

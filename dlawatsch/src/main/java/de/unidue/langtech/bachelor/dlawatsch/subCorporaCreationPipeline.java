package de.unidue.langtech.bachelor.dlawatsch;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;


	public class subCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                            CorpusReader.class,
	                            CorpusReader.PARAM_SOURCE_LOCATION, "/home/dominikl/Dokumente/BA/models/",
	                            CorpusReader.PARAM_PATTERNS, "*.xml"                          
	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

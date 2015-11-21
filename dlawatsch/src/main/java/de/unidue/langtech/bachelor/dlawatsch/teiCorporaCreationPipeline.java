package de.unidue.langtech.bachelor.dlawatsch;


import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

	public class teiCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                            NKJPReader.class,
	                            NKJPReader.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/pltest/",
	                            NKJPReader.PARAM_PATTERNS, "*.xml"
	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

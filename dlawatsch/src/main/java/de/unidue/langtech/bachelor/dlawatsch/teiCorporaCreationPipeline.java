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
	                            BNCReader.class,
	                            BNCReader.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/CORPORA/BNC/2554/download/Texts/A/A0",
	                            BNCReader.PARAM_PATTERNS, "A00.xml"
	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

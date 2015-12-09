package de.unidue.langtech.bachelor.dlawatsch;

import java.io.File;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.PipelineEngineFactories.WriteBinJcas;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.ReadBinJCasToJCasForModelGeneration;


	public class subCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                    		ReadBinJCasToJCasForModelGeneration.class,
	                    		ReadBinJCasToJCasForModelGeneration.PARAM_BIN_LOCATION, "/home/dominik/Dokumente/BA/CORPORA/BINARIES/ISLANDIC/",
	                    		ReadBinJCasToJCasForModelGeneration.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/CORPORA/BINARIES/ISLANDIC/",
	                    		ReadBinJCasToJCasForModelGeneration.PARAM_PATTERNS, "Sentence*.bin",
	                    		ReadBinJCasToJCasForModelGeneration.PARAM_MAX_TOKEN_SIZE, "5000"
	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

package de.unidue.langtech.bachelor.dlawatsch;

import java.io.File;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.unidue.langtech.bachelor.Annotators.WriteBinJcas;
import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.reader.BinaryReaderRandomization;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.ReadBinJCasToJCasForModelGeneration;


	public class subCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                    		BinaryReaderRandomization.class,
	                    		BinaryReaderRandomization.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/CORPORA/LANGUAGES/ISLANDIC/BINARIES/",
	                    		BinaryReaderRandomization.PARAM_PATTERNS, "FILE*.bin",
	                    		BinaryReaderRandomization.PARAM_CORPUSLOCATION, "/home/dominik/Dokumente/BA/CORPORA/",
	                    		BinaryReaderRandomization.PARAM_LANGUAGE, "ISLANDIC",
	                    		BinaryReaderRandomization.PARAM_USE_X_MAX_TOKEN, "25000",
	                    		BinaryReaderRandomization.PARAM_TYPE_SYSTEM_LOCATION, "typesystem.bin"
	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

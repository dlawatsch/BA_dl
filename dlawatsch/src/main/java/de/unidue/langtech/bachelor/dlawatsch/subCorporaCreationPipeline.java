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
import de.unidue.langtech.bachelor.reader.SloveneReader;


	public class subCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                    		IslandicCorpusReader.class,
	                    		IslandicCorpusReader.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/CORPORA/ICELANDIC_GOLD/MIM-GOLD_0.9",
	                    		IslandicCorpusReader.PARAM_PATTERNS, "*.txt"

	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

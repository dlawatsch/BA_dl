package de.unidue.langtech.bachelor.dlawatsch;

import java.io.File;

import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.PipelineEngineFactories.WriteBinJcas;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;


	public class subCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                            IslandicCorpusReader.class,
	                            IslandicCorpusReader.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/CORPORA/ICELANDIC_GOLD/MIM-GOLD_0.9/",
	                            IslandicCorpusReader.PARAM_PATTERNS, "adjucations.txt"
	                    ),

	                     AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class));
	        }
}

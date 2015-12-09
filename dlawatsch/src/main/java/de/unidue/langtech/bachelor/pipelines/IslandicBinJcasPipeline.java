package de.unidue.langtech.bachelor.pipelines;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.PipelineEngineFactories.WriteBinJcas;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;

public class IslandicBinJcasPipeline {
	public static void writeSentencesToBinJCas(String corpusLocation) throws ResourceInitializationException, UIMAException, IOException{		
		SimplePipeline.runPipeline(
                CollectionReaderFactory.createReader(
                        IslandicCorpusReader.class,
                        IslandicCorpusReader.PARAM_SOURCE_LOCATION, corpusLocation + "ICELANDIC_GOLD/MIM-GOLD_0.9/",
                        IslandicCorpusReader.PARAM_PATTERNS, "*.txt"
                ),

                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "ISLANDIC", 
						   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
	}
}

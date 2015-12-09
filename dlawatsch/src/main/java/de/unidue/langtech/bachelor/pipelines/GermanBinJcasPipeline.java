package de.unidue.langtech.bachelor.pipelines;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.PipelineEngineFactories.WriteBinJcas;
import de.unidue.langtech.bachelor.reader.TigerConLLReader;

public class GermanBinJcasPipeline {
	public static void writeSentencesToBinJCas(String corpusLocation) throws ResourceInitializationException, UIMAException, IOException{		
		SimplePipeline.runPipeline(
                CollectionReaderFactory.createReader(
                        TigerConLLReader.class,
                        TigerConLLReader.PARAM_SOURCE_LOCATION, corpusLocation + "TIGER/",
                        TigerConLLReader.PARAM_PATTERNS, "*.conll09"
                ),

                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "GERMAN", 
						   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
	}
}

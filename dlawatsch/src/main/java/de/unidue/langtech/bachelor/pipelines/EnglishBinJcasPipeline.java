package de.unidue.langtech.bachelor.pipelines;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.PipelineEngineFactories.WriteBinJcas;
import de.unidue.langtech.bachelor.reader.BNCReader;

public class EnglishBinJcasPipeline {
	public static void writeSentencesToBinJCas(String corpusLocation) throws ResourceInitializationException, UIMAException, IOException{		
		SimplePipeline.runPipeline(
                CollectionReaderFactory.createReader(
                        BNCReader.class,
                        BNCReader.PARAM_SOURCE_LOCATION, corpusLocation + "BNC/2554/download/Texts/**/**",
                        BNCReader.PARAM_PATTERNS, "*.xml"
                ),

                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "ENGLISH", 
						   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
	}
}

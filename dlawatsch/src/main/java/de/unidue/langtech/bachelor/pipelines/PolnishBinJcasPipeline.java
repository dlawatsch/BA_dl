package de.unidue.langtech.bachelor.pipelines;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.PipelineEngineFactories.WriteBinJcas;
import de.unidue.langtech.bachelor.reader.NKJPReader;

public class PolnishBinJcasPipeline {
	public static void writeSentencesToBinJCas(String corpusLocation) throws ResourceInitializationException, UIMAException, IOException{		
		SimplePipeline.runPipeline(
                CollectionReaderFactory.createReader(
                        NKJPReader.class,
                        NKJPReader.PARAM_SOURCE_LOCATION, corpusLocation + "POLNISH-NATIONAL_NKJP-PodkorpusMilionowy-1.0/**/",
                        NKJPReader.PARAM_PATTERNS, "ann_words.xml"
                ),

                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "POLNISH", 
						   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
	}
}

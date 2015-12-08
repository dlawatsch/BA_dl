package de.unidue.langtech.bachelor.dlawatsch;

import java.io.IOException;
import java.io.Reader;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;

public class PartOfSpeechTaggingModelPipelines {
	public static void main(String[] args) throws ResourceInitializationException, UIMAException, IOException {
		BuildAllBinaryJCas.buildAllBinCas();

	}
	
	public static void writeSentenceToBinCas(Object sentence) throws ResourceInitializationException, UIMAException, IOException{		
		sentence.getClass();
		SimplePipeline.runPipeline(
                CollectionReaderFactory.createReader(
                        IslandicCorpusReader.class,
                        IslandicCorpusReader.PARAM_SOURCE_LOCATION, "/home/dominikl/Dokumente/BA/CORPORA/ICELANDIC_GOLD/MIM-GOLD_0.9/",
                        IslandicCorpusReader.PARAM_PATTERNS, "*.txt"
                ),

                 AnalysisEngineFactory.createEngineDescription(TestEval.class));
	}
}

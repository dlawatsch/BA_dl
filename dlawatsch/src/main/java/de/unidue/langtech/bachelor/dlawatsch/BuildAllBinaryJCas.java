package de.unidue.langtech.bachelor.dlawatsch;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase;
import de.tudarmstadt.ukp.dkpro.core.api.parameter.ComponentParameters;

public class BuildAllBinaryJCas{

	public static void buildAllBinCas() throws ResourceInitializationException, UIMAException, IOException{		
		SimplePipeline.runPipeline(
                CollectionReaderFactory.createReader(
                        IslandicCorpusReader.class,
                        IslandicCorpusReader.PARAM_SOURCE_LOCATION, "/home/dominikl/Dokumente/BA/CORPORA/ICELANDIC_GOLD/MIM-GOLD_0.9/",
                        IslandicCorpusReader.PARAM_PATTERNS, "*.txt"
                ),

                 AnalysisEngineFactory.createEngineDescription(TestEval.class));
	}
}

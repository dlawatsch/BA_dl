package de.unidue.langtech.bachelor.dlawatsch;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.pipeline.SimplePipeline.runPipeline;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasWriter;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;

public class BinCasProcessTest {
public static void main(String[] args) {
		boolean aWriteTypeSystem = true;
    try {
		CollectionReader textReader = CollectionReaderFactory.createReader(IslandicCorpusReader.class,
				IslandicCorpusReader.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/CORPORA/ICELANDIC_GOLD/MIM-GOLD_0.9/",
				IslandicCorpusReader.PARAM_PATTERNS, "adjucations.txt");
        AnalysisEngine writer;
		writer = createEngine(
                BinaryCasWriter.class, 
                BinaryCasWriter.PARAM_FORMAT, "S", 
                BinaryCasWriter.PARAM_TARGET_LOCATION, "/home/dominik/Dokumente/BA/TESTCAS",
                BinaryCasWriter.PARAM_USE_DOCUMENT_ID, true,
                BinaryCasWriter.PARAM_TYPE_SYSTEM_LOCATION, 
                        aWriteTypeSystem ? "typesystem.bin" : null);
        try {
			runPipeline(textReader, writer);
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (ResourceInitializationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}

package de.unidue.langtech.bachelor.pipelines;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.langtech.bachelor.Annotators.SequenceIdAnnotator;
import de.unidue.langtech.bachelor.Annotators.WriteBinJcas;
import de.unidue.langtech.bachelor.reader.BNCReader;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.LatinReader;
import de.unidue.langtech.bachelor.reader.NKJPReader;
import de.unidue.langtech.bachelor.reader.SloveneReader;
import de.unidue.langtech.bachelor.reader.TigerConLLReader;

public class CreateBinariesPipeline {
	
	public static void writeToBinJCas(String corpusLocation, boolean islandic, boolean english, boolean german, boolean polnish, boolean latin, boolean slovene) throws ResourceInitializationException, UIMAException, IOException{		
		
		
		if(islandic){
			SimplePipeline.runPipeline(
	                CollectionReaderFactory.createReader(
	                        IslandicCorpusReader.class,
	                        IslandicCorpusReader.PARAM_SOURCE_LOCATION, corpusLocation + "ICELANDIC_GOLD/MIM-GOLD_0.9/",
	                        IslandicCorpusReader.PARAM_PATTERNS, "*.txt"
	                ),
	                AnalysisEngineFactory.createEngineDescription(SequenceIdAnnotator.class, SequenceIdAnnotator.PARAM_CORPUSLOCATION, corpusLocation, 
	                		SequenceIdAnnotator.PARAM_LANGUAGE, "ISLANDIC"),
	                AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "ISLANDIC", 
							WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
		}
		
		if(english){
		SimplePipeline.runPipeline(
                CollectionReaderFactory.createReader(
                        BNCReader.class,
                        BNCReader.PARAM_SOURCE_LOCATION, corpusLocation + "BNC/2554/download/Texts/**/**",
                        BNCReader.PARAM_PATTERNS, "*.xml"
                ),
                AnalysisEngineFactory.createEngineDescription(SequenceIdAnnotator.class, SequenceIdAnnotator.PARAM_CORPUSLOCATION, corpusLocation, 
                		SequenceIdAnnotator.PARAM_LANGUAGE, "ENGLISH"),
                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "ENGLISH", 
						   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
		}
		
		if(german){
			SimplePipeline.runPipeline(
	                CollectionReaderFactory.createReader(
	                        TigerConLLReader.class,
	                        TigerConLLReader.PARAM_SOURCE_LOCATION, corpusLocation + "TIGER/",
	                        TigerConLLReader.PARAM_PATTERNS, "*.conll09"
	                ),
	                AnalysisEngineFactory.createEngineDescription(SequenceIdAnnotator.class, SequenceIdAnnotator.PARAM_CORPUSLOCATION, corpusLocation, 
	                		SequenceIdAnnotator.PARAM_LANGUAGE, "GERMAN"),
	                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "GERMAN", 
							   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
		}
		
		if(polnish){
				SimplePipeline.runPipeline(
		                CollectionReaderFactory.createReader(
		                        NKJPReader.class,
		                        NKJPReader.PARAM_SOURCE_LOCATION, corpusLocation + "POLNISH-NATIONAL_NKJP-PodkorpusMilionowy-1.0/**/",
		                        NKJPReader.PARAM_PATTERNS, "ann_words.xml"
		                ),
		                AnalysisEngineFactory.createEngineDescription(SequenceIdAnnotator.class, SequenceIdAnnotator.PARAM_CORPUSLOCATION, corpusLocation, 
		                		SequenceIdAnnotator.PARAM_LANGUAGE, "POLNISH"),
		                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "POLNISH", 
								   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
		}
		
		if(latin){
			SimplePipeline.runPipeline(
	                CollectionReaderFactory.createReader(
	                        LatinReader.class,
	                        LatinReader.PARAM_SOURCE_LOCATION, corpusLocation + "POLNISH-NATIONAL_NKJP-PodkorpusMilionowy-1.0/**/",
	                        LatinReader.PARAM_PATTERNS, "ann_words.xml"
	                ),
	                AnalysisEngineFactory.createEngineDescription(SequenceIdAnnotator.class, SequenceIdAnnotator.PARAM_CORPUSLOCATION, corpusLocation, 
	                		SequenceIdAnnotator.PARAM_LANGUAGE, "LATIN"),
	                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "LATIN", 
							   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
		}
		
		if(slovene){
			SimplePipeline.runPipeline(
	                CollectionReaderFactory.createReader(
                    		SloveneReader.class,
                    		SloveneReader.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/CORPORA/SLOVENE-PARALLEL_IJS-ELAN/",
                    		SloveneReader.PARAM_PATTERNS, "*-sl.xml"
	                ),
	                AnalysisEngineFactory.createEngineDescription(SequenceIdAnnotator.class, SequenceIdAnnotator.PARAM_CORPUSLOCATION, corpusLocation, 
	                		SequenceIdAnnotator.PARAM_LANGUAGE, "SLOVENE"),
	                 AnalysisEngineFactory.createEngineDescription(WriteBinJcas.class, WriteBinJcas.PARAM_LANGUAGE, "LATIN", 
							   WriteBinJcas.PARAM_CORPUSLOCATION, corpusLocation));
		}
	}
}

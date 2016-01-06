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
import de.unidue.langtech.bachelor.reader.NKJPReader;
import de.unidue.langtech.bachelor.reader.SloveneReader;


	public class subCorporaCreationPipeline {
	    public static void main(String[] args)
	            throws Exception
	        {
	            SimplePipeline.runPipeline(
	                    CollectionReaderFactory.createReader(
	                    		NKJPReader.class,
		                        NKJPReader.PARAM_SOURCE_LOCATION, "/home/dominikl/Dokumente/BA/CORPORA/POLNISH-NATIONAL_NKJP-PodkorpusMilionowy-1.0/**/",
		                        NKJPReader.PARAM_PATTERNS, "ann_morphosyntax.xml"

	                    ),

	                     AnalysisEngineFactory.createEngineDescription(TestEval.class));
	        }
}

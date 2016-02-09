/*******************************************************************************
 * Copyright 2015
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.unidue.langtech.bachelor.pipelines;

import static java.util.Arrays.asList;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.component.NoOpAnnotator;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.ConditionalFrequencyDistribution;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.tudarmstadt.ukp.dkpro.core.io.tei.TeiReader;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.lab.Lab;
import de.tudarmstadt.ukp.dkpro.lab.task.BatchTask.ExecutionPolicy;
import de.tudarmstadt.ukp.dkpro.lab.task.Dimension;
import de.tudarmstadt.ukp.dkpro.lab.task.ParameterSpace;
import de.tudarmstadt.ukp.dkpro.tc.core.Constants;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.CRFSuiteAdapter;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.CRFSuiteBatchCrossValidationReport;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.task.serialization.SaveModelCRFSuiteBatchTask;
import de.tudarmstadt.ukp.dkpro.tc.examples.io.BrownCorpusReader;
import de.tudarmstadt.ukp.dkpro.tc.features.length.NrOfCharsUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.ngram.LuceneCharacterNGramUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.ngram.LuceneNGramUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.style.IsSurroundedByCharsUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.token.CurrentToken;
import de.tudarmstadt.ukp.dkpro.tc.features.token.NextToken;
import de.tudarmstadt.ukp.dkpro.tc.features.token.PreviousToken;
import de.tudarmstadt.ukp.dkpro.tc.ml.ExperimentCrossValidation;
import de.unidue.langtech.bachelor.Annotators.BaselineAnnotator;
import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.reader.BaselineBinaryReaderRandomization;
import de.unidue.langtech.bachelor.reader.BinaryReaderRandomization;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.NKJPReader;
import de.unidue.langtech.bachelor.reader.PosAmbiguityEvaluator;


/**
 * 
 * @author dominik
 *
 */
public class CreateBaselinePipeline implements Constants{
	/*
	 * This baseline runs 3 times for each language with 10.000, 50.000 and 100.000 token 
	 * to get a baseline learning curve 
	 */
	static String experimentName;
	static File modelOutputFolder;
	static String homeFolder;
	static String languageCode;
	static int iteration;
	static int i;
	static String corpus;
	static String modelOutputDir;
	public static ConditionalFrequencyDistribution<String, String> cfd;
	public static void process(String corpusLocation, boolean islandic, boolean english, boolean german, boolean polnish, boolean slovene, boolean coarseGrained) throws ResourceInitializationException, UIMAException, IOException{
		
		String coarse = "";
		if(coarseGrained){
			coarse = "true";
		}else{
			coarse = "false";
		}
		
		if(islandic){
			i = 0;
			iteration = 1;
			corpus = corpusLocation;
			languageCode = "ISLANDIC";

		
			for(;iteration <= 3; iteration++){
				cfd = new ConditionalFrequencyDistribution<String, String>();
				if(iteration == 1){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(10000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(10000), coarse);
				}
				if(iteration == 2){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(50000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(50000), coarse);				
					}
				if(iteration == 3){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startPosAmbiguityEvaluation(corpusLocation, languageCode, String.valueOf(100000), coarse);

					}
			}
		}	
		
		if(english){
			i = 0;
			iteration = 1;
			corpus = corpusLocation;
			languageCode = "ENGLISH";
		
			for(;iteration <= 3; iteration++){
				cfd = new ConditionalFrequencyDistribution<String, String>();
				if(iteration == 1){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(10000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(10000), coarse);
				}
				if(iteration == 2){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(50000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(50000), coarse);				
					}
				if(iteration == 3){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startPosAmbiguityEvaluation(corpusLocation, languageCode, String.valueOf(100000), coarse);

					}
			}
		}
		
		if(german){
			i = 0;
			corpus = corpusLocation;
			iteration = 1;
			languageCode = "GERMAN";
		
			for(;iteration <= 3; iteration++){
				cfd = new ConditionalFrequencyDistribution<String, String>();
				if(iteration == 1){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(10000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(10000), coarse);
				}
				if(iteration == 2){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(50000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(50000), coarse);				
					}
				if(iteration == 3){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startPosAmbiguityEvaluation(corpusLocation, languageCode, String.valueOf(100000), coarse);

					}
			}
		}
		
		if(polnish){
			i = 0;
			corpus = corpusLocation;
			languageCode = "POLNISH";
			iteration = 1;
		
			for(;iteration <= 3; iteration++){
				cfd = new ConditionalFrequencyDistribution<String, String>();
				if(iteration == 1){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(10000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(10000), coarse);
				}
				if(iteration == 2){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(50000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(50000), coarse);				
					}
				if(iteration == 3){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startPosAmbiguityEvaluation(corpusLocation, languageCode, String.valueOf(100000), coarse);
					}
			}
		}

		
		if(slovene){
			i = 0;
			corpus = corpusLocation;
			languageCode = "SLOVENE";
			iteration = 1;
		
			for(;iteration <= 3; iteration++){
				cfd = new ConditionalFrequencyDistribution<String, String>();
				if(iteration == 1){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(10000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(10000), coarse);
				}
				if(iteration == 2){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(50000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(50000), coarse);				
					}
				if(iteration == 3){
					startFrequencyCalculation(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startBaselineEvaluation	(corpusLocation, languageCode, String.valueOf(100000), coarse);
					startPosAmbiguityEvaluation(corpusLocation, languageCode, String.valueOf(100000), coarse);
					}
			}
		}
	}
	
	//POS ambiguity statistic only for 100.000 token as smaller amounts are not interesting	
	private static void startPosAmbiguityEvaluation(String corpusLocation, String languageCode2, String count,
			String coarse) throws IOException {
		PosAmbiguityEvaluator.startEvaluation(corpusLocation, languageCode, count, coarse);
	}

	//create frequency distribution
	private static void startFrequencyCalculation(String corpusLocation, String languageCode, String maxToken, String coarse) throws ResourceInitializationException, UIMAException, IOException{				
			SimplePipeline.runPipeline(
	        CollectionReaderFactory.createReader(
	        		BaselineBinaryReaderRandomization.class,
	        		BaselineBinaryReaderRandomization.PARAM_SOURCE_LOCATION, corpus + "/LANGUAGES/" + languageCode + "/BINARIES/",
	        		BaselineBinaryReaderRandomization.PARAM_PATTERNS, "FILE*.bin",
	        		BaselineBinaryReaderRandomization.PARAM_CORPUSLOCATION, corpus,
	        		BaselineBinaryReaderRandomization.PARAM_LANGUAGE, languageCode,
	        		BaselineBinaryReaderRandomization.PARAM_COARSEGRAINED, coarse,
	        		BaselineBinaryReaderRandomization.PARAM_USE_X_MAX_TOKEN, maxToken
	        ),

	         AnalysisEngineFactory.createEngineDescription(TestEval.class));	         
	}
	
	//actual baseline annotation process including statistics which are saved to hard drive
	private static void startBaselineEvaluation(String corpusLocation, String languageCode, String maxToken, String coarse) throws ResourceInitializationException, UIMAException, IOException{				
			SimplePipeline.runPipeline(
	        CollectionReaderFactory.createReader(
	        		BaselineAnnotator.class,
	        		BaselineAnnotator.PARAM_SOURCE_LOCATION, corpus + "/LANGUAGES/" + languageCode + "/BINARIES/",
	        		BaselineAnnotator.PARAM_PATTERNS, "FILE*.bin",
	        		BaselineAnnotator.PARAM_CORPUSLOCATION, corpus,
	        		BaselineAnnotator.PARAM_LANGUAGE, languageCode,
	        		BaselineAnnotator.PARAM_COARSEGRAINED, coarse,
	        		BaselineAnnotator.PARAM_USE_X_MAX_TOKEN, maxToken
	        ),

         AnalysisEngineFactory.createEngineDescription(TestEval.class));	         
	}
}

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.component.NoOpAnnotator;
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
import de.unidue.langtech.bachelor.reader.BinaryReaderRandomization;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.NKJPReader;


/**
 * 
 * @author dominik
 *
 */
public class CreateBaselinePipeline implements Constants{
	
	static String experimentName;
	static File modelOutputFolder;
	static String homeFolder;
	static String languageCode;
	static int iteration;
	static int i;
	static String corpus;
	static String modelOutputDir;
	public static ConditionalFrequencyDistribution<String, String> cfd;
	public static void process(String corpusLocation, boolean islandic, boolean english, boolean german, boolean polnish, boolean latin, boolean slovene){

		
		if(islandic){
			cfd = new ConditionalFrequencyDistribution<String, String>();
			i = 0;
			iteration = 1;
			corpus = corpusLocation;
			languageCode = "ISLANDIC";
			homeFolder = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/";
			modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/" + String.valueOf(iteration*10000)+"_UNITS_MODEL/";

		
			for(;iteration <= 3; iteration++){
				if(iteration == 1){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 1);
				}
				if(iteration == 2){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 5);
				}
				if(iteration == 3){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 10);
				}
			}
		}	
		
		if(english){
			cfd = new ConditionalFrequencyDistribution<String, String>();
			i = 0;
			iteration = 1;
			corpus = corpusLocation;
			languageCode = "ENGLISH";
			homeFolder = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/";
			modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/" + String.valueOf(iteration*10000)+"_UNITS_MODEL/";

		
			for(;iteration <= 3; iteration++){
				if(iteration == 1){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 1);
				}
				if(iteration == 2){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 5);
				}
				if(iteration == 3){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 10);
				}
			}
		}
		
		if(german){
			cfd = new ConditionalFrequencyDistribution<String, String>();
			i = 0;
			corpus = corpusLocation;
			iteration = 1;
			languageCode = "GERMAN";
			homeFolder = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/";
			modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/" + String.valueOf(iteration*10000)+"_UNITS_MODEL/";

		
			for(;iteration <= 3; iteration++){
				if(iteration == 1){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 1);
				}
				if(iteration == 2){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 5);
				}
				if(iteration == 3){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 10);
				}
			}
		}
		
		if(polnish){
			cfd = new ConditionalFrequencyDistribution<String, String>();
			i = 0;
			corpus = corpusLocation;
			languageCode = "POLNISH";
			iteration = 1;
			homeFolder = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/";
			modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/" + String.valueOf(iteration*10000)+"_UNITS_MODEL/";

		
			for(;iteration <= 3; iteration++){
				if(iteration == 1){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 1);
				}
				if(iteration == 2){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 5);
				}
				if(iteration == 3){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 10);
				}
			}
		}

		if(latin){
			cfd = new ConditionalFrequencyDistribution<String, String>();
			i = 0;
			corpus = corpusLocation;
			languageCode = "LATIN";
			iteration = 1;
			homeFolder = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/";
			modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/" + String.valueOf(iteration*10000)+"_UNITS_MODEL/";

		
			for(;iteration <= 3; iteration++){
				if(iteration == 1){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 1);
				}
				if(iteration == 2){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 5);
				}
				if(iteration == 3){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 10);
				}
			}
		}
		
		if(slovene){
			cfd = new ConditionalFrequencyDistribution<String, String>();
			i = 0;
			corpus = corpusLocation;
			languageCode = "SLOVENE";
			iteration = 1;
			homeFolder = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/";
			modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/BASELINE/" + String.valueOf(iteration*10000)+"_UNITS_MODEL/";

		
			for(;iteration <= 3; iteration++){
				if(iteration == 1){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 1);
				}
				if(iteration == 2){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 5);
				}
				if(iteration == 3){
					BaselineTrainAndSaveModell.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, 10);
				}
			}
		}
	}
}

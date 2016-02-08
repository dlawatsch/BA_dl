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


import java.io.File;


import de.tudarmstadt.ukp.dkpro.tc.core.Constants;



/**
 * 
 * @author dominik
 *
 */
public class TrainModels implements Constants{
	/*
	 * This class starts the training of the models. 10 iterations are needed
	 * for the learning curve, adding 10.000 more tokens at each iteration
	 * until 100.000 tokens are the last iteration.
	 */
	static String experimentName;
	static File modelOutputFolder;
	static String homeFolder;
	static String languageCode;
	static int iteration;
	static int i;
	static String corpus;
	static String modelOutputDir;
	
	public static void process(String corpusLocation, boolean islandic, boolean english, boolean german, boolean polnish, boolean slovene, boolean useCoarseGrained){
		
		if(islandic){
			i = 0;
			iteration = 1;
			corpus = corpusLocation;
			languageCode = "ISLANDIC";
			if(useCoarseGrained){
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_coarseGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/COARSE/";
			}else{
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_fineGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/FINE/";
			}

			for(;iteration <= 10; iteration++){
				TrainAndSaveNewModelCRF.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, iteration, useCoarseGrained);
			}
		}	
		
		if(english){
			i = 0;
			corpus = corpusLocation;
			languageCode = "ENGLISH";
			if(useCoarseGrained){
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_coarseGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/COARSE/";
			}else{
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_fineGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/FINE/";
			}
			iteration = 1;

			for(;iteration <= 10; iteration++){
				TrainAndSaveNewModelCRF.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, iteration, useCoarseGrained);
			}	
		}
		
		if(german){
			i = 0;
			corpus = corpusLocation;
			languageCode = "GERMAN";
			if(useCoarseGrained){
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_coarseGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/COARSE/";
			}else{
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_fineGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/FINE/";
			}
			iteration = 1;

			for(;iteration <= 10; iteration++){
				TrainAndSaveNewModelCRF.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, iteration, useCoarseGrained);
			}	
		}
		
		if(polnish){
			i = 0;
			corpus = corpusLocation;
			languageCode = "POLNISH";
			if(useCoarseGrained){
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_coarseGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/COARSE/";
			}else{
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_fineGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/FINE/";
			}
			iteration = 1;

			for(;iteration <= 10; iteration++){
				TrainAndSaveNewModelCRF.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, iteration, useCoarseGrained);
			}	
		}

		
		if(slovene){
			i = 0;
			corpus = corpusLocation;
			languageCode = "SLOVENE";
			if(useCoarseGrained){
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_coarseGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/COARSE/";
			}else{
				modelOutputDir = corpusLocation + "/LANGUAGES/" + languageCode + "/MODELS/" + "100000_UNITS_MODEL_fineGrained/";
				homeFolder = corpusLocation + "/LANGUAGES/EVALUATION/FINE/";
			}
			iteration = 1;

			for(;iteration <= 10; iteration++){
				TrainAndSaveNewModelCRF.TrainAndSaveCRF(corpusLocation, languageCode, homeFolder, modelOutputDir, iteration, useCoarseGrained);
			}	
		}

	}
}

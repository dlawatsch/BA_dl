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
import de.tudarmstadt.ukp.dkpro.lab.task.Dimension;
import de.tudarmstadt.ukp.dkpro.lab.task.ParameterSpace;
import de.tudarmstadt.ukp.dkpro.lab.task.BatchTask.ExecutionPolicy;
import de.tudarmstadt.ukp.dkpro.tc.core.Constants;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.CRFSuiteAdapter;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.CRFSuiteBatchCrossValidationReport;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.task.serialization.SaveModelCRFSuiteBatchTask;
import de.tudarmstadt.ukp.dkpro.tc.features.length.NrOfCharsUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.ngram.LuceneCharacterNGramUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.ngram.LuceneNGramUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.token.CurrentToken;
import de.tudarmstadt.ukp.dkpro.tc.features.token.NextToken;
import de.tudarmstadt.ukp.dkpro.tc.features.token.PreviousToken;
import de.tudarmstadt.ukp.dkpro.tc.ml.ExperimentCrossValidation;
import de.unidue.langtech.bachelor.PipelineEngineFactories.TestEval;
import de.unidue.langtech.bachelor.reader.BaselineBinaryReaderRandomization;
import de.unidue.langtech.bachelor.reader.BinaryReaderRandomization;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.NKJPReader;

/**
 * Example class of how to train an own model
 */
public class BaselineTrainAndSaveModell implements Constants {
static String experimentName;
static File modelOutputFolder;
static String homeFolder;
static String languageCode;
static int iteration;
static int i;
static String corpus;
static String modelOutputDir;
public static ConditionalFrequencyDistribution<String, String> cfd;

public static void TrainAndSaveCRF(String cLoc, String languageCODE, String homeDir, String modelOutputFoldera, int i){
	iteration = i;
	corpus = cLoc;
	languageCode = languageCODE;
	homeFolder = homeDir;

	System.setProperty("DKPRO_HOME", homeFolder);

	i *= 10000;
	modelOutputFolder = new File(modelOutputFoldera);
	modelOutputFolder.mkdirs();
	experimentName = languageCode + String.valueOf(i);
	
    cfd = new ConditionalFrequencyDistribution<String, String>();

	
    try {
		SimplePipeline.runPipeline(
		        CollectionReaderFactory.createReader(
		        		BaselineBinaryReaderRandomization.class,
		        		BaselineBinaryReaderRandomization.PARAM_SOURCE_LOCATION, corpus + "/LANGUAGES/" + languageCode + "/BINARIES/",
		        		BaselineBinaryReaderRandomization.PARAM_PATTERNS, "FILE*.bin",
		        		BaselineBinaryReaderRandomization.PARAM_CORPUSLOCATION, corpus,
		        		BaselineBinaryReaderRandomization.PARAM_LANGUAGE, languageCode,
		        		BaselineBinaryReaderRandomization.PARAM_USE_X_MAX_TOKEN, String.valueOf(iteration * 10000)
		        ),

		         AnalysisEngineFactory.createEngineDescription(TestEval.class));
	} catch (ResourceInitializationException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (UIMAException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}


	ParameterSpace pSpace;
//	try {
//		pSpace = getParameterSpace(Constants.FM_SEQUENCE,
//				Constants.LM_SINGLE_LABEL);
//		BaselineTrainAndSaveModell experiment = new BaselineTrainAndSaveModell();
//		if(iteration==10){
//			experiment.validation(pSpace);
//		}
//		experiment.runCrossValidation(pSpace);
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
}

@SuppressWarnings("unchecked")
public static ParameterSpace getParameterSpace(String featureMode,
		String learningMode) throws Exception {

	// configure training and test data reader dimension
	Map<String, Object> dimReaders = new HashMap<String, Object>();

	Dimension<List<String>> dimFeatureSets = Dimension.create(
			DIM_FEATURE_SET,
			Arrays.asList(new String[] { CurrentToken.class.getName(),
					NextToken.class.getName(),
					PreviousToken.class.getName(),
					LuceneCharacterNGramUFE.class.getName(),
					NrOfCharsUFE.class.getName()
			}));

	Dimension<List<String>> dimClassificationArgs = Dimension
			.create(DIM_CLASSIFICATION_ARGS,
					asList(new String[] { CRFSuiteAdapter.ALGORITHM_ADAPTIVE_REGULARIZATION_OF_WEIGHT_VECTOR }));

	Dimension<List<Object>> dimPipelineParameters = Dimension.create(
			DIM_PIPELINE_PARAMS, Arrays.asList(new Object[] {
					LuceneCharacterNGramUFE.PARAM_CHAR_NGRAM_MAX_N, 4,
					LuceneCharacterNGramUFE.PARAM_CHAR_NGRAM_MIN_N, 2,
					LuceneCharacterNGramUFE.PARAM_CHAR_NGRAM_USE_TOP_K, 1000,
					}));



	dimReaders.put(DIM_READER_TRAIN, BinaryReaderRandomization.class);
	dimReaders.put(DIM_READER_TRAIN_PARAMS, Arrays.asList(
        		BinaryReaderRandomization.PARAM_SOURCE_LOCATION, corpus + "/LANGUAGES/" + languageCode + "/BINARIES/",
        		BinaryReaderRandomization.PARAM_PATTERNS, "FILE*.bin",
        		BinaryReaderRandomization.PARAM_CORPUSLOCATION, corpus,
        		BinaryReaderRandomization.PARAM_LANGUAGE, languageCode,
        		BinaryReaderRandomization.PARAM_USE_X_MAX_TOKEN, String.valueOf(iteration * 10000) ,
        		BinaryReaderRandomization.PARAM_USE_BASELINE, "true",
        		BinaryReaderRandomization.PARAM_TYPE_SYSTEM_LOCATION, "typesystem.bin"));

	ParameterSpace pSpace = new ParameterSpace(Dimension.createBundle(
			"readers", dimReaders), Dimension.create(DIM_LEARNING_MODE,
			learningMode), Dimension.create(DIM_FEATURE_MODE, featureMode),
			dimPipelineParameters, dimFeatureSets, dimClassificationArgs);



	return pSpace;
}

protected void validation(ParameterSpace pSpace) throws Exception {


	SaveModelCRFSuiteBatchTask batch = new SaveModelCRFSuiteBatchTask(
			experimentName, modelOutputFolder, CRFSuiteAdapter.class,
			getPreprocessing());


	batch.setParameterSpace(pSpace);

	// Run
	Lab.getInstance().run(batch);
}

protected void runCrossValidation(ParameterSpace pSpace) throws Exception {


	ExperimentCrossValidation batch = new ExperimentCrossValidation(
			experimentName, CRFSuiteAdapter.class, 5);

	batch.setPreprocessing(getPreprocessing());
	batch.setParameterSpace(pSpace);
	batch.setExecutionPolicy(ExecutionPolicy.RUN_AGAIN);
	batch.addReport(CRFSuiteBatchCrossValidationReport.class);

	batch.setParameterSpace(pSpace);

	// Run
	Lab.getInstance().run(batch);
}

protected AnalysisEngineDescription getPreprocessing()
		throws ResourceInitializationException {
	return createEngineDescription(NoOpAnnotator.class);

}
}


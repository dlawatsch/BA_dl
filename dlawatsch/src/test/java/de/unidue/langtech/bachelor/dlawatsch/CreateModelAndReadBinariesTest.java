package de.unidue.langtech.bachelor.dlawatsch;


import static java.util.Arrays.asList;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.component.NoOpAnnotator;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.lab.Lab;
import de.tudarmstadt.ukp.dkpro.lab.task.Dimension;
import de.tudarmstadt.ukp.dkpro.lab.task.ParameterSpace;
import de.tudarmstadt.ukp.dkpro.lab.task.BatchTask.ExecutionPolicy;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.core.Constants;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.CRFSuiteAdapter;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.CRFSuiteBatchCrossValidationReport;
import de.tudarmstadt.ukp.dkpro.tc.crfsuite.task.serialization.SaveModelCRFSuiteBatchTask;
import de.tudarmstadt.ukp.dkpro.tc.features.length.NrOfCharsUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.ngram.LuceneCharacterNGramUFE;
import de.tudarmstadt.ukp.dkpro.tc.features.token.CurrentToken;
import de.tudarmstadt.ukp.dkpro.tc.features.token.NextToken;
import de.tudarmstadt.ukp.dkpro.tc.features.token.PreviousToken;
import de.tudarmstadt.ukp.dkpro.tc.ml.ExperimentCrossValidation;
import de.unidue.langtech.bachelor.reader.BinaryReaderRandomization;

public class CreateModelAndReadBinariesTest implements Constants {
	static String experimentName;
	static File modelOutputFolder;
	static String homeFolder;
	static String languageCode;
	static int iteration;
	static int i;
	static String corpus;
	static String modelOutputDir;
	
	@Test
	public void TrainAndSaveCRF(){

		languageCode = "POLNISH";
		homeFolder = "src/main/resources/Binaries/polnish/";
		String modelOutputFolderS = "src/main/resources/Binaries/polnish/MODELS/";
		System.setProperty("DKPRO_HOME", homeFolder);

		modelOutputFolder = new File(modelOutputFolderS);
		modelOutputFolder.mkdirs();
		experimentName = languageCode + String.valueOf(1000);


		ParameterSpace pSpace;
		try {
			pSpace = getParameterSpace(Constants.FM_SEQUENCE,
					Constants.LM_SINGLE_LABEL);
			CreateModelAndReadBinariesTest experiment = new CreateModelAndReadBinariesTest();
			if(iteration==10){
				experiment.validation(pSpace);
			}
			experiment.runCrossValidation(pSpace);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


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
				BinaryReaderRandomization.PARAM_SOURCE_LOCATION, "src/main/resources/Binaries/polnish/**/LANGUAGES/POLNISH/BINARIES",
				BinaryReaderRandomization.PARAM_PATTERNS, "FILE*.bin",
				BinaryReaderRandomization.PARAM_CORPUSLOCATION, "src/main/resources/Binaries/polnish/**/",
				BinaryReaderRandomization.PARAM_LANGUAGE, "POLNISH",
				BinaryReaderRandomization.PARAM_USE_X_MAX_TOKEN, "1000",
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
				experimentName, CRFSuiteAdapter.class, 10);
		
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
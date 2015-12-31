package de.unidue.langtech.bachelor.execution;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.langtech.bachelor.pipelines.CreateBinariesPipeline;
import de.unidue.langtech.bachelor.pipelines.TrainModels;

/*	This class has to be executed first in order to 
 *  create Binary JCas for each sentence 
 *  for the chosen corpora below
 */
public class CreateAndTrainModels {
	public static void main(String[] args) throws ResourceInitializationException, UIMAException, IOException {
		
		/*Please provide the location of the extracted 
		 * "CORPORA" folder (like "/home/dominik/Dokumente/BA/CORPORA/")
		 * which can be found on the CD  
		 */
		String corpusLocation = "/home/dominikl/Dokumente/BA/CORPORA/";
		
		/* Set the boolean of the languages to false 
		 * if you dont wish those
		 * corpora to be processed to Binaries
		 */
		boolean islandic = false;
		boolean english = false;
		boolean german = false;
		boolean polnish = false;
		boolean latin = false;
		boolean slovene = true;
		
		TrainModels.process(corpusLocation, islandic, english, german, polnish, latin, slovene);		
	}
}

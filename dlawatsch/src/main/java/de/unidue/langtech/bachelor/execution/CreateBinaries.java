package de.unidue.langtech.bachelor.execution;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.langtech.bachelor.pipelines.CreateBinariesPipeline;

/*	This class has to be executed first in order to 
 *  create Binary JCas for each sentence 
 *  for the chosen corpora below
 */
public class CreateBinaries {
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
		boolean islandic = true;
		boolean english = true;
		boolean german = true;
		boolean polnish = true;
		boolean latin = true;
		boolean slovene = true;
		
		CreateBinariesPipeline.writeToBinJCas(corpusLocation, islandic, english, german, polnish, latin, slovene);		
	}
}

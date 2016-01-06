package de.unidue.langtech.bachelor.execution;

import de.unidue.langtech.bachelor.pipelines.CreateBaselinePipeline;
import de.unidue.langtech.bachelor.pipelines.TrainModels;

public class CreateBaseline {
	public static void main(String[] args) {
	
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
	
	CreateBaselinePipeline.process(corpusLocation, islandic, english, german, polnish, latin, slovene);	
	}
}
	

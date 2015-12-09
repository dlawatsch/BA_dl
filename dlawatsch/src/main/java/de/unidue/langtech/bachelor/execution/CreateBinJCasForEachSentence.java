package de.unidue.langtech.bachelor.execution;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.resource.ResourceInitializationException;

import de.unidue.langtech.bachelor.pipelines.EnglishBinJcasPipeline;
import de.unidue.langtech.bachelor.pipelines.GermanBinJcasPipeline;
import de.unidue.langtech.bachelor.pipelines.IslandicBinJcasPipeline;
import de.unidue.langtech.bachelor.pipelines.LatinBinJcasPipeline;
import de.unidue.langtech.bachelor.pipelines.PolnishBinJcasPipeline;

/*	This class has to be executed first in order to 
 *  create Binary JCas for each sentence 
 *  for the chosen corpora below
 */
public class CreateBinJCasForEachSentence {
	public static void main(String[] args) throws ResourceInitializationException, UIMAException, IOException {
		
		/*Please provide the location of the extracted 
		 * "CORPORA" folder (like "/home/dominik/Dokumente/BA/CORPORA/")
		 * which can be found on the CD  
		 */
		String corpusLocation = "/home/dominik/Dokumente/BA/CORPORA/";
		
		
		/* Uncomment the Corpora you dont 
		 * wish to be processed to BinJcas'
		 */
		IslandicBinJcasPipeline.writeSentencesToBinJCas(corpusLocation);
//		EnglishBinJcasPipeline.writeSentencesToBinJCas(corpusLocation);
//		GermanBinJcasPipeline.writeSentencesToBinJCas(corpusLocation);
//		PolnishBinJcasPipeline.writeSentencesToBinJCas(corpusLocation);
//		LatinBinJcasPipeline.writeSentencesToBinJCas(corpusLocation);			
	}
}

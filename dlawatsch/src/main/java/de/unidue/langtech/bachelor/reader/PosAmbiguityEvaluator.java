package de.unidue.langtech.bachelor.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import de.unidue.langtech.bachelor.pipelines.CreateBaselinePipeline;
/*
 * Load the created frequency distribution
 * that has been created at the baseline processing
 * and get all different pos tags 
 * that have been recorded for the actual word and
 * count the ambiguities.
 * Results are saved to the hard drive
 */
public class PosAmbiguityEvaluator {
	
	public static void startEvaluation(String corpusLocation, String language, String count,
			String coarse) throws IOException{
		int pos1 = 0;
		int pos2 = 0;
		int pos3 = 0;
		int pos4 = 0;
		int pos5 = 0;
		int pos6 = 0;
		int pos7 = 0;
		int pos8 = 0;
		int pos9 = 0;
		int pos10 = 0;
		int posmorethan10 = 0;
		
		File out;
        if(coarse.equals("true")){
            out = new File(corpusLocation + "/LANGUAGES/" + language + "/BASELINE/" + count+"_PosAmbiguity_COARSE");
        }else{
            out = new File(corpusLocation + "/LANGUAGES/" + language + "/BASELINE/" + count+"_PosAmbiguity_FINE");
        }
		
        
        Set<String> allWords = CreateBaselinePipeline.cfd.getConditions();
        for(String word : allWords){
        	if(word.equals("POS")){
        	}else{
            	int numberOfRecordedPOSTagsForThisWord = CreateBaselinePipeline.cfd.getFrequencyDistribution(word).getKeys().size();
            	
            	switch(numberOfRecordedPOSTagsForThisWord){
            	case 1: pos1++;
            			break;
            	case 2: pos2++;
    					break;
            	case 3: pos3++;
    					break;
            	case 4: pos4++;
    					break;
            	case 5: pos5++;
    					break;
            	case 6: pos6++;
    					break;
            	case 7: pos7++;
    					break;
            	case 8: pos8++;
    					break;
            	case 9: pos9++;
    					break;
            	case 10: pos10++;
    					break;
            	default: posmorethan10++;
    					 break;
            	}
        	}

        }
        
        int wordCount = allWords.size()-1;
		

        BufferedWriter output = new BufferedWriter(new FileWriter(out, true));
        output.write("ALL WORDS: " + wordCount);
        output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 1 POS tag: " + (((double)pos1/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 2 POS tags: " + (((double)pos2/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 3 POS tags: " + (((double)pos3/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 4 POS tags: " + (((double)pos4/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 5 POS tags: " + (((double)pos5/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 6 POS tags: " + (((double)pos6/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 7 POS tags: " + (((double)pos7/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 8 POS tags: " + (((double)pos8/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 9 POS tags: " + (((double)pos9/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to 10 POS tags: " + (((double)pos10/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));
		output.write("Words assigned to more than 10 POS tags: " + (((double)posmorethan10/(double)wordCount) * 100) + "%");
		output.write(System.getProperty("line.separator"));

		
		output.write("RECORDED POS TAGS:" + CreateBaselinePipeline.cfd.getFrequencyDistribution("POS").getB());
		output.write(System.getProperty("line.separator"));
		output.write("MOST FREQUENT POS TAGS");
		output.write(System.getProperty("line.separator"));
        List<String> mostFrequentPos = CreateBaselinePipeline.cfd.getFrequencyDistribution("POS").getMostFrequentSamples(10);
        for(String pos : mostFrequentPos){
        	output.write(pos + " OCCURED: " + CreateBaselinePipeline.cfd.getFrequencyDistribution("POS").getCount(pos) + " TIMES " + (((double)CreateBaselinePipeline.cfd.getFrequencyDistribution("POS").getCount(pos)/(double)100000) * 100) + "%");
    		output.write(System.getProperty("line.separator"));
        }
        
 		output.flush();
		output.close();
        
	}
}

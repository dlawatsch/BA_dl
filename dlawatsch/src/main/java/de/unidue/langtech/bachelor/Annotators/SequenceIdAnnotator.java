package de.unidue.langtech.bachelor.Annotators;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;


import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unidue.langtech.bachelor.type.SequenceID;

/*
 * This class is needed for randomization of sentences
 */

public class SequenceIdAnnotator extends JCasAnnotator_ImplBase{
    public static final String PARAM_CORPUSLOCATION = "PARAM_CORPUSLOCATION";
    @ConfigurationParameter(name = PARAM_CORPUSLOCATION, mandatory = true, defaultValue ="TEST")
    protected String corpusLocation;
    
    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected static String language;
    
    
    int sequenceIDcount;
    static File file;
    static Writer output;
    
	public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);          
            sequenceIDcount = 0;	
            File dir = new File(corpusLocation + "/LANGUAGES/" + language + "/SEQUENCES/");
            dir.mkdirs();
            
            file = new File(corpusLocation + "/LANGUAGES/" + language + "/SEQUENCES/" + "SEQUENCE_ID.txt");
        }   
    
    
    
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		/*
		 * Polish Corpus has many files already and therefore no new Jcas's had to be 
		 * created and therefore its JCas's are handled in a normal way.
		 * This is the generalized way and is suitable for further research.
		 */
		if(language.equals("POLNISH")){
	        try {
				output = new BufferedWriter(new FileWriter(file, true));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        //writes "ID	NrOfToken" per line into the "SEQUENCE_IDs.txt"
			for (Sentence s : JCasUtil.select(jcas, Sentence.class)) {
				int numberOfTokens = 0;
				SequenceID sid = new SequenceID(jcas);
				sid.setBegin(s.getBegin());
				sid.setEnd(s.getEnd());
				sid.setID(sequenceIDcount);
				
				for (Token t : JCasUtil.selectCovered(jcas, Token.class, s)){
					numberOfTokens++;
				}
				
				sid.setNrOfTokens(numberOfTokens);
				sid.addToIndexes();
				
				addToFile(sid.getID() + " " + sid.getNrOfTokens());
				sequenceIDcount++;
			}
			try {
				output.flush();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			/*
			 * All other corpora have to be handled with help of the 
			 * Build400TokenJCasEach class where the newly generated
			 * Jcas's are stored.
			 */
	        try {
				output = new BufferedWriter(new FileWriter(file, true));
		        for(JCas out : Build400TokenJCasEach.allJcas){
					for (Sentence s : JCasUtil.select(out, Sentence.class)) {
						int numberOfTokens = 0;
						
						SequenceID sid = new SequenceID(out);
						sid.setBegin(s.getBegin());
						sid.setEnd(s.getEnd());
						sid.setID(sequenceIDcount);
						
						for (Token t : JCasUtil.selectCovered(out, Token.class, s)){
							numberOfTokens++;
						}
						
						sid.setNrOfTokens(numberOfTokens);
						sid.addToIndexes(out);
						
						addToFile(sid.getID() + " " + sid.getNrOfTokens());
						sequenceIDcount++;									
					}
				}		        	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				output.flush();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}		    
	}



	private static void addToFile(String string) {
		try {
			output.write(string);
			output.write(System.getProperty("line.separator"));
	        
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	 * This method is for the german corpus as mentioned in the Build400TokenJCas class
	 * to prevent GC Limit Overhead!
	 */
	public static void processSingleFile(JCas jcas){
        try {
			output = new BufferedWriter(new FileWriter(file, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		for (Sentence s : JCasUtil.select(jcas, Sentence.class)) {
			int numberOfTokens = 0;
			SequenceID sid = new SequenceID(jcas);
			sid.setBegin(s.getBegin());
			sid.setEnd(s.getEnd());
			sid.setID(Build400TokenJCasEach.sequenceID);
			
			for (Token t : JCasUtil.selectCovered(jcas, Token.class, s)){
				numberOfTokens++;
			}
			
			sid.setNrOfTokens(numberOfTokens);
			sid.addToIndexes();
			
			addToFile(sid.getID() + " " + sid.getNrOfTokens());
			//sequenceIDcount++ exclusively for german;	
			Build400TokenJCasEach.sequenceID++;
		}
		try {
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * really not nice to hand them directly to the binary writer
		 * but as already mentioned this is how the GC limit overhead can be prevented
		 */
		WriteBinJcas.createBinariesOutOfSingleFile(jcas);	
	}
}

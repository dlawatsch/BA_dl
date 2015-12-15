package de.unidue.langtech.bachelor.Annotators;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

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



public class SequenceIdAnnotator extends JCasAnnotator_ImplBase{
    public static final String PARAM_CORPUSLOCATION = "PARAM_CORPUSLOCATION";
    @ConfigurationParameter(name = PARAM_CORPUSLOCATION, mandatory = true, defaultValue ="TEST")
    protected String corpusLocation;
    
    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected String language;
    
    
    int sequenceIDcount;
    File file;
    Writer output;
	public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);          
            sequenceIDcount = 0;
            File dir = new File(corpusLocation + language + "/SEQUENCES/");
            dir.mkdir();
            
            file = new File(corpusLocation + language + "/SEQUENCES/" + "SEQUENCE_ID.txt");
            try {
				output = new BufferedWriter(new FileWriter(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }   
    
    
    
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		
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
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	private void addToFile(String string) {
		try {
			System.out.println(string);			
			output.write(string);
			output.write(System.getProperty("line.separator"));
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}

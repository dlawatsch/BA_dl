package de.unidue.langtech.bachelor.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.uima.UIMAException;
import org.apache.uima.UimaContext;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.w3c.dom.Element;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;
import de.unidue.langtech.bachelor.type.SequenceID;

public class BinaryReaderRandomization extends BinaryCasReader{
    public static final String PARAM_CORPUSLOCATION = "PARAM_CORPUSLOCATION";
    @ConfigurationParameter(name = PARAM_CORPUSLOCATION, mandatory = true, defaultValue ="TEST")
    protected String corpusLocation;
    
    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected String language;
    
    public static final String PARAM_USE_X_MAX_TOKEN = "PARAM_USE_X_MAX_TOKEN";
    @ConfigurationParameter(name = PARAM_USE_X_MAX_TOKEN, mandatory = true, defaultValue ="TEST")
    protected String maxToken;
    
    static int currentDocument;
	static Object[] allDocuments;
    Random randomGenerator;
	List<String> allSequenceIDs = new ArrayList<String>();
	int currentTokenCount;
	int maximumToken;
	Set<Integer> randomizedSID = new HashSet<Integer>();
	int realtokens;
    JCas jcas;
    int annotatedToken;
    
	public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);    
            allDocuments = getResources().toArray();
            currentDocument = 0;
            
            randomGenerator = new Random();
            
            maximumToken = Integer.valueOf(maxToken);
            currentTokenCount = 0;
            annotatedToken = 0;
            FileReader fr;

			try {
				fr = new FileReader(corpusLocation + "/LANGUAGES/" + language + "/SEQUENCES/" + "SEQUENCE_ID.txt");
	            BufferedReader br = new BufferedReader(fr);
	            String currentline;
	            while((currentline = br.readLine()) != null){
	            	allSequenceIDs.add(currentline);
	            }
	            br.close();

	            while(currentTokenCount <= maximumToken){
		            int index = randomGenerator.nextInt(allSequenceIDs.size());
		            String currentSID = allSequenceIDs.get(index);
		            String[] parts = currentSID.split(" ");
		            
		            int sequenceID = Integer.valueOf(parts[0]);
		            int tokenInSID = Integer.valueOf(parts[1]);
		            
		            if(randomizedSID.add(sequenceID)){
		            	 currentTokenCount += tokenInSID;
		            }		           
	            }	            
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


        }  
	
	public boolean hasNext() throws IOException, CollectionException {
		if((currentDocument < allDocuments.length)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void getNext(CAS cas) throws IOException, CollectionException {
			super.getNext(cas);
			realtokens = 0;
			try {
				jcas = JCasFactory.createJCas();
				jcas = cas.getJCas();
				DocumentMetaData meta = DocumentMetaData.get(jcas);
				System.out.println("[PROCESSING: " + meta.getDocumentId() + "]");
				
				
				Sentence minsentence = null;
				int mintoken = Integer.MAX_VALUE;
				
		        for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
		        	for (SequenceID sid : JCasUtil.selectCovering(jcas, SequenceID.class, sentence)){
		        		if(sid.getNrOfTokens() < mintoken){
		        			mintoken = sid.getNrOfTokens();
		        			minsentence = sentence;
		        		}
		        		if(randomizedSID.contains(sid.getID())){
		        			randomizedSID.remove(sid.getID());
		        			System.out.println("[ANNOTATING SENTENCE ID: " + sid.getID() + "]");
		        			addAnnotations(sentence);
		        			
		        		}
		        	}		        	
		        }
		        currentDocument++;
		        if(realtokens == 0 && hasNext() == true){
		        	getNext(cas);	        	
		        }else if(realtokens == 0 && hasNext() == false){
		        	addAnnotations(minsentence);	        	
		        }
		        
		        System.out.println("[TOKENS IN THIS DOCUMENT: " + realtokens + "]");
		        System.out.println("[ANNOTATED: " + annotatedToken + "/" + currentTokenCount + "]");

			} catch (UIMAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


        

	}


	private void addAnnotations(Sentence sentence) {
        TextClassificationSequence sequence = new TextClassificationSequence(jcas, sentence.getBegin(), sentence.getEnd());
        sequence.addToIndexes();
        int tokensInThis = 0;
        for (Token token : JCasUtil.selectCovered(jcas, Token.class, sentence)) {
            TextClassificationUnit unit = new TextClassificationUnit(jcas, token.getBegin(), token.getEnd());

            // will add the token content as a suffix to the ID of this unit 
            unit.setSuffix(token.getCoveredText());
            unit.addToIndexes();
            
            TextClassificationOutcome outcome = new TextClassificationOutcome(jcas, token.getBegin(), token.getEnd());
            outcome.setOutcome(token.getPos().getPosValue());
            outcome.addToIndexes();
            realtokens++;
            tokensInThis++;
        }
        annotatedToken += tokensInThis;
	}

}

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
import org.apache.uima.cas.Type;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.resources.MappingProvider;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.unidue.langtech.bachelor.pipelines.CreateBaselinePipeline;
import de.unidue.langtech.bachelor.type.SequenceID;

/*
 * This class creates the Baseline with a Majority Tag approach
 */
public class BaselineBinaryReaderRandomization extends BinaryCasReader{
    public static final String PARAM_CORPUSLOCATION = "PARAM_CORPUSLOCATION";
    @ConfigurationParameter(name = PARAM_CORPUSLOCATION, mandatory = true, defaultValue ="TEST")
    protected String corpusLocation;
    
    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected String language;
    
    public static final String PARAM_USE_X_MAX_TOKEN = "PARAM_USE_X_MAX_TOKEN";
    @ConfigurationParameter(name = PARAM_USE_X_MAX_TOKEN, mandatory = true, defaultValue ="TEST")
    protected String maxToken;
    
    public static final String PARAM_COARSEGRAINED= "PARAM_COARSEGRAINED";
    @ConfigurationParameter(name = PARAM_COARSEGRAINED, mandatory = true, defaultValue ="TEST")
    protected String coarseGrained;
    
    static MappingProvider posMappingProvider;
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
    static FrequencyDistribution<String> fd;
    static File outputfile;

	public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);    
            File dir = new File(corpusLocation + "/LANGUAGES/" + language + "/FREQUENCIES/");
            dir.mkdirs();
            
            if(coarseGrained.equals("true")){
                outputfile = new File(corpusLocation + "/LANGUAGES/" + language + "/FREQUENCIES/" + maxToken+"_FREQUENCIES_COARSE");
            }else{
                outputfile = new File(corpusLocation + "/LANGUAGES/" + language + "/FREQUENCIES/" + maxToken+"_FREQUENCIES_FINE");
            }
            
            allDocuments = getResources().toArray();
            currentDocument = 0;
            fd = new FrequencyDistribution<String>();

            randomGenerator = new Random();
            
            maximumToken = Integer.valueOf(maxToken);
            currentTokenCount = 0;
            annotatedToken = 0;
            FileReader fr;

            String posMappingString ="";
            String overrider ="";
            if(language.equals("ISLANDIC")){
            	posMappingString = "src/main/resources/POSMapping/is.map";
            	overrider = "is.map";
            }else if(language.equals("SLOVENE")){
            	posMappingString = "src/main/resources/POSMapping/sl-SI.map";
            	overrider = "sl-SI.map";
            }else if(language.equals("ENGLISH")){
            	posMappingString = "classpath:/de/tudarmstadt/ukp/dkpro/" + "core/api/lexmorph/tagset/en-c5-pos.map";
            	overrider = "en-c5-pos.map";   	
            }else if(language.equals("GERMAN")){
            	posMappingString = "classpath:/de/tudarmstadt/ukp/dkpro/" + "core/api/lexmorph/tagset/de-pos.map";
            	overrider = "de-pos.map";           	
            }else if(language.equals("POLNISH")){
            	posMappingString = "src/main/resources/POSMapping/pl-ncp-simple.map";
            	overrider = "pl-ncp-simple.map";           	
            }
            
    		posMappingProvider = new MappingProvider();
    		posMappingProvider
    				.setDefault(
    						MappingProvider.LOCATION,
    						"src/main/resources/POSMapping/is.map");
    		posMappingProvider.setDefault(MappingProvider.BASE_TYPE,
    				POS.class.getName());
    		posMappingProvider.setDefault("tagger.tagset", "default");
    		posMappingProvider.setOverride(MappingProvider.LOCATION,
    				posMappingString);
    		posMappingProvider.setOverride(MappingProvider.LANGUAGE, language);
    		posMappingProvider.setOverride(overrider, overrider);
    		
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
        	CreateBaselinePipeline.cfd.setFrequencyDistribution("POS", fd);
			fd.save(outputfile);
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
				posMappingProvider.configure(jcas.getCas());
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
		        			addFrequencies(sentence);
		        			
		        		}
		        	}		        	
		        }
		        currentDocument++;		        


			} catch (UIMAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


        

	}


	private void addFrequencies(Sentence sentence) {
        int tokensInThis = 0;
        for (Token token : JCasUtil.selectCovered(jcas, Token.class, sentence)) {
        	String outcome = getOutcome(token);
        	CreateBaselinePipeline.cfd.addSample(token.getCoveredText(), outcome, 1);
        	fd.addSample(outcome, 1);   
        	
            realtokens++;
            tokensInThis++;
        }
        annotatedToken += tokensInThis;
        System.out.println("[THIS DOCUMENT ANNOTATED: " + annotatedToken + "/" + currentTokenCount + "]");
	}


	private String getOutcome(Token token) {
		if(coarseGrained.equals("true")){
			if(language.equals("ISLANDIC")){
	            String post = token.getPos().getPosValue().replace("þ", "XX");
	    		Type posTag = posMappingProvider.getTagType(post);
	    		POS pos = (POS) jcas.getCas().createAnnotation(posTag, token.getBegin(), token.getEnd());
	            return pos.getClass().getSimpleName();
			}else{
	    		Type posTag = posMappingProvider.getTagType(token.getPos().getPosValue());
	    		POS pos = (POS) jcas.getCas().createAnnotation(posTag, token.getBegin(), token.getEnd());
	            return pos.getClass().getSimpleName();				
			}
		}else{
			return token.getPos().getPosValue();
		}				
	}
}

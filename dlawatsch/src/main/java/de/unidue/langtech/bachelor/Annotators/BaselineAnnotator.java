package de.unidue.langtech.bachelor.Annotators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

public class BaselineAnnotator extends BinaryCasReader{
    public static final String PARAM_CORPUSLOCATION = "PARAM_CORPUSLOCATION";
    @ConfigurationParameter(name = PARAM_CORPUSLOCATION, mandatory = true, defaultValue ="TEST")
    protected String corpusLocation;
    
    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected String language;
    
    public static final String PARAM_USE_X_MAX_TOKEN = "PARAM_USE_X_MAX_TOKEN";
    @ConfigurationParameter(name = PARAM_USE_X_MAX_TOKEN, mandatory = true, defaultValue ="TEST")
    protected String maxToken;
    
    public static final String PARAM_COARSEGRAINED = "PARAM_COARSEGRAINED";
    @ConfigurationParameter(name = PARAM_COARSEGRAINED, mandatory = true, defaultValue ="TEST")
    protected String coarseGrained;
    static FrequencyDistribution<String> fd;
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
    static int correctItems;
    static int allItems;
    static String mostFreq;
    /*
     * The baseline uses the most frequent POS tag of each word 
     * as the baseline solution.
     * If this word did not appear yet, the overall most frequent
     * POS tag is used.
     */
    
	public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);    
            allDocuments = getResources().toArray();
            currentDocument = 0;
            //get the overall most frequent POS tag once
        	mostFreq = CreateBaselinePipeline.cfd.getFrequencyDistribution("POS").getSampleWithMaxFreq();

            randomGenerator = new Random();
            
            maximumToken = Integer.valueOf(maxToken);
            currentTokenCount = 0;
            annotatedToken = 0;
            correctItems = 0;
            allItems = 0;
            FileReader fr;
            
            File outputfile;
            
            if(coarseGrained.equals("true")){
                outputfile = new File(corpusLocation + "/LANGUAGES/" + language + "/FREQUENCIES/" + maxToken+"_FREQUENCIES_COARSE");
            }else{
                outputfile = new File(corpusLocation + "/LANGUAGES/" + language + "/FREQUENCIES/" + maxToken+"_FREQUENCIES_FINE");
            }


            fd = new FrequencyDistribution<String>();
            //load the frequency distribution which was created previously by the BaselineBinaryReaderRandomization
            	try {
					fd.load(outputfile);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
                String posMappingString ="";
                String overrider ="";
                //depending on the language, its POS map has to be loaded
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
        						"/home/dominikl/git/BA_dl_final/dlawatsch/src/main/resources/POSMapping/is.map");
        		posMappingProvider.setDefault(MappingProvider.BASE_TYPE,
        				POS.class.getName());
        		posMappingProvider.setDefault("tagger.tagset", "default");
        		posMappingProvider.setOverride(MappingProvider.LOCATION,
        				posMappingString);
        		posMappingProvider.setOverride(MappingProvider.LANGUAGE, language);
        		posMappingProvider.setOverride(overrider, overrider);

    		/*
    		 * Reading the Sequence IDs of the language to randomize
    		 */
			try {
				fr = new FileReader(corpusLocation + "/LANGUAGES/" + language + "/SEQUENCES/" + "SEQUENCE_ID.txt");
	            BufferedReader br = new BufferedReader(fr);
	            
	            String currentline;
	            while((currentline = br.readLine()) != null){
	            	allSequenceIDs.add(currentline);
	            }
	            br.close();

	            //sum up all tokens per sentence
	            int allToken = 0;
	            for(String l : allSequenceIDs){
	            	String[] parts = l.split(" ");
	            	allToken += Integer.valueOf(parts[1]);
	            }
	            
	            //if chosen amount of tokens > overall amount then reduce to overall amount
	            if(maximumToken > allToken){
	            	System.out.println("[NOT ENOUGH TOKEN IN DOCUMENT! " + allToken + " AVAILABLE | " + maximumToken + " CHOSEN]");
	            	System.out.println("[REDUCED USE_X_MAX_TOKEN TO " + allToken+"]");
	            	maximumToken = allToken;
	            }
	            
	            //take random sample as long chosen amount of tokens is reached
	            while(currentTokenCount < maximumToken){
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
			//if false save statistics to hard drive
			File out;
            if(coarseGrained.equals("true")){
                out = new File(corpusLocation + "/LANGUAGES/" + language + "/BASELINE/" + maxToken+"_BASELINE_COARSE");
            }else{
                out = new File(corpusLocation + "/LANGUAGES/" + language + "/BASELINE/" + maxToken+"_BASELINE_FINE");
            }
			double prozent = ((double)correctItems/(double)allItems) * 100;
			String prozentstring = String.valueOf(prozent);
			String correct = String.valueOf(correctItems);
			String all = String.valueOf(allItems);
		     try {
			    	BufferedWriter output = new BufferedWriter(new FileWriter(out, true));
					output.write("Accuracy: " + prozentstring + "%");
					output.write(System.getProperty("line.separator"));
					output.write("( " + correct + " / " + all + " )");
			 		output.flush();
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		        					
		}
				return false;
	}
	
	@Override
	public void getNext(CAS cas) throws IOException, CollectionException {
			super.getNext(cas);
			try {
				jcas = JCasFactory.createJCas();
				jcas = cas.getJCas();
				posMappingProvider.configure(jcas.getCas());
			} catch (UIMAException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			realtokens = 0;

			
			try {
				jcas = JCasFactory.createJCas();
				jcas = cas.getJCas();
				DocumentMetaData meta = DocumentMetaData.get(jcas);
				System.out.println("[PROCESSING: " + meta.getDocumentId() + "]");
				
								
		        for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
		        	/*
		        	 * if a sentence contains a sequence IDs out of the chosen random sequence ids
		        	 * then it will be annotated with TCUs and TCSs
		        	 */
		        	for (SequenceID sid : JCasUtil.selectCovering(jcas, SequenceID.class, sentence)){

		        		if(randomizedSID.contains(sid.getID())){
		        			randomizedSID.remove(sid.getID());
		        			System.out.println("[ANNOTATING SENTENCE ID: " + sid.getID() + "]");
		        			
		        			addBaselineAnnotations(sentence);
		        		}
		        	}		        	
		        }
		        currentDocument++;
		        if(realtokens == 0 && hasNext() == true){
		        	getNext(cas);	        	
		        }


			} catch (UIMAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


        

	}


	private void addBaselineAnnotations(Sentence sentence) {
        int tokensInThis = 0;
        for (Token token : JCasUtil.selectCovered(jcas, Token.class, sentence)) {
            // will add the token content as a suffix to the ID of this unit 
            String baselineOutcome = "";
            try{
                baselineOutcome = CreateBaselinePipeline.cfd.getFrequencyDistribution(token.getCoveredText()).getSampleWithMaxFreq();
            }catch(Exception e){
            	baselineOutcome = mostFreq;
            }
            if(coarseGrained.equals("true")){
            	//if coarse grained is true then get the coarse grained tag
                if(getOutcome(token).equals(baselineOutcome)){
                	correctItems++;
                }
            }else{             
                if(token.getPos().getPosValue().equals(baselineOutcome)){
                	correctItems++;
                }
            }

            allItems++;
            realtokens++;
            tokensInThis++;
        }
        annotatedToken += tokensInThis;
        System.out.println("[THIS DOCUMENT ANNOTATED: " + annotatedToken + "/" + currentTokenCount + "]");
	}
	
	/*
	 * as already mentioned on some OS (windows) problems appeared with handling the letter "þ"
	 * this problem is bypassed by replacing it with "XX" and editing the is.map to fit þ as well as XX
	 */
	private String getOutcome(Token token) {
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
	}				
}

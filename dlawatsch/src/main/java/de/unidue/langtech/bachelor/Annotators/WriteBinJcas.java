package de.unidue.langtech.bachelor.Annotators;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import java.util.UUID;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;

import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasWriter;
/*
 * Saves the jcas as binary
 */
public class WriteBinJcas extends JCasAnnotator_ImplBase{

    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected static String language;
    
    public static final String PARAM_CORPUSLOCATION = "PARAM_CORPUSLOCATION";
    @ConfigurationParameter(name = PARAM_CORPUSLOCATION, mandatory = true, defaultValue ="TEST")
    protected static String corpusLocation;
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		/*
		 * again, polish depicts the generalized approach
		 * whereas the other languages have to be handled 
		 * differently because of the problems occured (see BA chapter 5)
		 */
		if(language.equals("POLNISH")){
			UUID uniqueID = UUID.randomUUID();
			
	    	DocumentMetaData meta = DocumentMetaData.get(jcas);
	    	meta.setDocumentId("FILE_" + String.valueOf(uniqueID));
			try {
				generateBinJCas(jcas);
			} catch (ResourceInitializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
	        for(JCas out : Build400TokenJCasEach.allJcas){
	        	System.out.println("TRUE : " + out.getDocumentLanguage());
	    		UUID uniqueID = UUID.randomUUID();
	        	DocumentMetaData meta = DocumentMetaData.get(jcas);
	        	meta.setDocumentId("FILE_" + String.valueOf(uniqueID));
					try {
						generateBinJCas(out);
					} catch (ResourceInitializationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        }
	        //clear this array to be empty at next iteration to prevent double processing of same files
	        Build400TokenJCasEach.allJcas.clear();

		}
	
	}
	
	private void generateBinJCas(JCas out) throws ResourceInitializationException, AnalysisEngineProcessException {
	      AnalysisEngine writer = createEngine(
	                BinaryCasWriter.class, 
	                BinaryCasWriter.PARAM_FORMAT, "S", 
	                BinaryCasWriter.PARAM_TARGET_LOCATION, corpusLocation + "/LANGUAGES/" + language + "/BINARIES/",
	                BinaryCasWriter.PARAM_USE_DOCUMENT_ID, true,
	                BinaryCasWriter.PARAM_TYPE_SYSTEM_LOCATION, 
	                        true ? "typesystem.bin" : null);	      
	        writer.process(out);
	        writer.collectionProcessComplete();
	}
	
	//method for german, used in sequenceIDannotator class
	public static void createBinariesOutOfSingleFile(JCas jcas){
		UUID uniqueID = UUID.randomUUID();
		
    	DocumentMetaData meta = DocumentMetaData.get(jcas);
    	meta.setDocumentId("FILE_" + String.valueOf(uniqueID));
			try {
			      AnalysisEngine writer = createEngine(
			                BinaryCasWriter.class, 
			                BinaryCasWriter.PARAM_FORMAT, "S", 
			                BinaryCasWriter.PARAM_TARGET_LOCATION, corpusLocation + "/LANGUAGES/" + language + "/BINARIES/",
			                BinaryCasWriter.PARAM_USE_DOCUMENT_ID, true,
			                BinaryCasWriter.PARAM_TYPE_SYSTEM_LOCATION, 
			                        true ? "typesystem.bin" : null);	      
			        try {
						writer.process(jcas);
						writer.collectionProcessComplete();
					} catch (AnalysisEngineProcessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			       
			} catch (ResourceInitializationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}

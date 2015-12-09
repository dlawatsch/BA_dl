package de.unidue.langtech.bachelor.PipelineEngineFactories;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.util.SystemOutLogger;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;

import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasWriter;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;

public class WriteBinJcas extends JCasAnnotator_ImplBase{

    public static final String PARAM_LANGUAGE = "PARAM_LANGUAGE";
    @ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = true, defaultValue ="TEST")
    protected String language;
    
    public static final String PARAM_CORPUSLOCATION = "PARAM_CORPUSLOCATION";
    @ConfigurationParameter(name = PARAM_CORPUSLOCATION, mandatory = true, defaultValue ="TEST")
    protected String corpusLocation;
	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
			JCas out;
			
			try {
				out = JCasFactory.createJCas();
			
		        out.setDocumentLanguage(jcas.getDocumentLanguage());
		        out.setDocumentText(sentence.getCoveredText());

		        DocumentMetaData meta = DocumentMetaData.create(out);
		        
		        UUID uniqueID = UUID.randomUUID();
		        meta.setDocumentId("Sentence_ID_" + String.valueOf(uniqueID));
		        
		        Sentence sentenceout = new Sentence(out, 0, (sentence.getEnd() - sentence.getBegin()));
		        
		        sentenceout.addToIndexes(out);
		        
		        	
				int wordBeginn = 0;
				int wordEnd = 0;
				
				for(Sentence se : JCasUtil.select(out, Sentence.class)){
		            TextClassificationSequence sequence = new TextClassificationSequence(out, se.getBegin(), se.getEnd());
		            sequence.addToIndexes(out);
	            	for (Token t : JCasUtil.selectCovered(jcas, Token.class, sentence)){
	            		wordEnd += (t.getEnd() - t.getBegin());
		        		Token token = new Token(out, wordBeginn, wordEnd);
		        		wordBeginn += t.getCoveredText().length() + 1;
		        		wordEnd++;        	
		        		
		                TextClassificationUnit unit = new TextClassificationUnit(out, token.getBegin(), token.getEnd());
		                unit.setSuffix(t.getCoveredText());
		                unit.addToIndexes();
		                
		                POS pos = new POS(out);
		                pos.setPosValue(t.getPos().getPosValue());
		                pos.addToIndexes(out);
		                
		                token.setPos(pos);
		                if(t.getLemma() != null){
		                	Lemma lemma = new Lemma(out);
		                	lemma.setValue(t.getLemma().getValue());
		                	lemma.addToIndexes(out);
		                	token.setLemma(lemma);
		                }

		                token.addToIndexes(out);
		                TextClassificationOutcome outcome = new TextClassificationOutcome(out, token.getBegin(), token.getEnd());
		                outcome.setOutcome(token.getPos().getPosValue());
		                outcome.addToIndexes(out);  
	            	}
				}
				
				
				generateBinJCas(out);
   		
	            

		   

			} catch (UIMAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	private void generateBinJCas(JCas out) throws ResourceInitializationException, AnalysisEngineProcessException {
	      AnalysisEngine writer = createEngine(
	                BinaryCasWriter.class, 
	                BinaryCasWriter.PARAM_FORMAT, "S", 
	                BinaryCasWriter.PARAM_TARGET_LOCATION, corpusLocation + "BINARIES/" + language,
	                BinaryCasWriter.PARAM_USE_DOCUMENT_ID, true,
	                BinaryCasWriter.PARAM_TYPE_SYSTEM_LOCATION, 
	                        true ? "typesystem.bin" : null);	      
	        writer.process(out);
	        writer.collectionProcessComplete();
	}


}

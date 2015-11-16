package de.unidue.langtech.bachelor.dlawatsch;


import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.io.tei.TeiReader;
import de.tudarmstadt.ukp.dkpro.tc.api.io.TCReaderSequence;

public class CorpusReader extends TeiReader
implements TCReaderSequence{
   
	static int tokenCount;
	static int percentageCount;
	
	/*
     * initializes the reader
     */
    //@Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);
            percentageCount = 10;
            tokenCount = 0;

        }   

    @Override
	    public void getNext(CAS cas)
	        throws IOException, CollectionException
	    {
	        super.getNext(cas);
	        
	        JCas jcas;
	        try {
	            jcas = cas.getJCas();
	        }
	        catch (CASException e) {
	            throw new CollectionException(e);
	        }
	        
	        boolean reachedLimit = false;
	        for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
	            TextClassificationSequence sequence = new TextClassificationSequence(jcas, sentence.getBegin(), sentence.getEnd());
	            sequence.addToIndexes();
	            
	            if(reachedLimit == true){
	            	break;
	            }
	            
	            for (Token token : JCasUtil.selectCovered(jcas, Token.class, sentence)) {
	            	if(tokenCount < percentageCount*5){
		                TextClassificationUnit unit = new TextClassificationUnit(jcas, token.getBegin(), token.getEnd());
		            	tokenCount++;
		                // will add the token content as a suffix to the ID of this unit 
		                unit.setSuffix(token.getCoveredText());
		                unit.addToIndexes();
		                System.out.print(" " + unit.getCoveredText() + tokenCount + " ");
		                
		                TextClassificationOutcome outcome = new TextClassificationOutcome(jcas, token.getBegin(), token.getEnd());
		                outcome.setOutcome(getTextClassificationOutcome(jcas, unit));
		                System.out.println(" " + outcome.getOutcome() + " ");
		                outcome.addToIndexes();
	            	}else{
	            		reachedLimit = true;
	            		percentageCount++;
	            		System.out.println();
	            		System.out.println();
	            		break;
	            	}
	            }
	         }
	        
	    }

	public String getTextClassificationOutcome(JCas jcas, TextClassificationUnit unit)        throws CollectionException
    {
        List<POS> posList = JCasUtil.selectCovered(jcas, POS.class, unit);
        if (posList.size() != 1) {
            throw new CollectionException(new Throwable("Could not get unique POS annotation to be used as TC outome."));
        }
        
        String outcome = "";     
        
        outcome = posList.get(0).getPosValue();
        
        return outcome;
    }
}

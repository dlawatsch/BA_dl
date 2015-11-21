package de.unidue.langtech.bachelor.dlawatsch;

import java.io.IOException;
import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.tei.TeiReader;
import de.tudarmstadt.ukp.dkpro.tc.api.io.TCReaderSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;

public class newTEIReader extends TEIREADER
	implements TCReaderSequence{
	   @Override
	    public void getNext(CAS cas)
	        throws IOException, CollectionException
	    {
	        super.getNext(cas);

	        JCas jcas;
	        try {
	            jcas = cas.getJCas();
//		        System.out.println(jcas.getDocumentText());
	        }
	        catch (CASException e) {
	            throw new CollectionException(e);
	        }
	        String txt = "";
	        for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
	            TextClassificationSequence sequence = new TextClassificationSequence(jcas, sentence.getBegin(), sentence.getEnd());
	            sequence.addToIndexes();
//	            System.out.println("Sentence: " + sentence.getCoveredText());
	            
	            for (Token token : JCasUtil.selectCovered(jcas, Token.class, sentence)) {
	                TextClassificationUnit unit = new TextClassificationUnit(jcas, token.getBegin(), token.getEnd());
		            txt += token.getCoveredText() + " ";
		            
//		            System.out.println("TOKEN: " + token.getCoveredText());
	                // will add the token content as a suffix to the ID of this unit 
	                unit.setSuffix(token.getCoveredText());
//	                System.out.println(token.getCoveredText());
	                unit.addToIndexes();
	                
	                TextClassificationOutcome outcome = new TextClassificationOutcome(jcas, token.getBegin(), token.getEnd());
	                outcome.setOutcome(getTextClassificationOutcome(jcas, unit));
	                outcome.addToIndexes();
	            }
	        }
	    }
	    

	public String getTextClassificationOutcome(JCas jcas, TextClassificationUnit unit) throws CollectionException {
		// TODO Auto-generated method stub
		return null;
	}
	
}

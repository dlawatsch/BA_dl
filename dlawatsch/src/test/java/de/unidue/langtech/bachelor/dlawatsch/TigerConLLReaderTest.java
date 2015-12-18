package de.unidue.langtech.bachelor.dlawatsch;

import static org.junit.Assert.assertEquals;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;
import de.unidue.langtech.bachelor.reader.TigerConLLReader;


public class TigerConLLReaderTest
{

    @Test
    public void testReaderExample()throws Exception
    {
        CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
        		TigerConLLReader.class,
        		TigerConLLReader.PARAM_SOURCE_LOCATION, "src/test/resources/reader/",
        		TigerConLLReader.PARAM_PATTERNS, "TigerConLLReaderTest.conll09"
        );


        for (JCas jcas : new JCasIterable(reader)) {
            System.out.println(jcas.getDocumentText());
            assertEquals("Ich bin schon viel um die Welt gereist . Yoda würde sagen \" Schon viel gesehen du hast \" ", jcas.getDocumentText());
                
            assertEquals("ge", jcas.getDocumentLanguage());
                
            int posid = 1;
		    for (Sentence se : JCasUtil.select(jcas, Sentence.class)) {
		        for(Token t : JCasUtil.selectCovered(jcas, Token.class, se)){		        	
		        	System.out.println(t.getCoveredText() + " POS: " + t.getPos().getPosValue() + " Lemma: " + t.getLemma().getValue());
		        	assertEquals(String.valueOf(posid), t.getPos().getPosValue());		
		        	assertEquals("x", t.getLemma().getValue());	
		        	posid++;
		        }
		    }  
        }
    }
}
package de.unidue.langtech.bachelor.dlawatsch.readers;

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


public class IslandicCorpusReaderTest
{

    @Test
    public void testReaderExample()throws Exception
    {
        CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
                IslandicCorpusReader.class,
                IslandicCorpusReader.PARAM_SOURCE_LOCATION, "src/test/resources/reader/",
                IslandicCorpusReader.PARAM_PATTERNS, "IslandicCorpusTest.txt"
        );


        for (JCas jcas : new JCasIterable(reader)) {
            assertEquals("Dies ist ein Test : Ich schreibe Bachelor Arbeit Junit Tests ", jcas.getDocumentText());
                
            assertEquals("is", jcas.getDocumentLanguage());
                
            int posid = 1;
		    for (Sentence se : JCasUtil.select(jcas, Sentence.class)) {
		        for(Token t : JCasUtil.selectCovered(jcas, Token.class, se)){		        	
		        	System.out.println(t.getCoveredText() + " POS: " + t.getPos().getPosValue());
		        	assertEquals(String.valueOf(posid), t.getPos().getPosValue());		        		
		        	posid++;
		        }
		    }  
        }
    }
}
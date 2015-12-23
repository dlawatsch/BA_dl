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
import de.unidue.langtech.bachelor.reader.NKJPReader;


public class PolnishReaderTest
{

    @Test
    public void testReaderExample()throws Exception
  {     
        CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
                NKJPReader.class,
                NKJPReader.PARAM_SOURCE_LOCATION, "src/main/resources/Binaries/polnish/010-2-000000001/",
                NKJPReader.PARAM_PATTERNS, "ann_words.xml"
        );


        for (JCas jcas : new JCasIterable(reader)) {
            int posid = 1;
            System.out.println(jcas.getDocumentText());
		    for (Sentence se : JCasUtil.select(jcas, Sentence.class)) {
		    	 System.out.println("sentence: " + se.getCoveredText());
		        for(Token t : JCasUtil.selectCovered(jcas, Token.class, se)){		        	
		        	System.out.println(t.getCoveredText() + " POS: " + t.getPos().getPosValue());
		        	posid++;
		        }		       
		    }  
        }
    }
}
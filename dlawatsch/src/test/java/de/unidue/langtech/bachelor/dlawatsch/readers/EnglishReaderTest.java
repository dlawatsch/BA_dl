package de.unidue.langtech.bachelor.dlawatsch.readers;

import static org.junit.Assert.assertEquals;

import org.apache.uima.cas.Type;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.resources.MappingProvider;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unidue.langtech.bachelor.reader.BNCReader;
import de.unidue.langtech.bachelor.reader.SloveneReader;

public class EnglishReaderTest {
    @Test
    public void testReaderExample()throws Exception
  {     
        CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
                BNCReader.class,
                BNCReader.PARAM_SOURCE_LOCATION, "src/test/resources/reader/",
                BNCReader.PARAM_PATTERNS, "bnctest.xml"
        );
        
        MappingProvider posMappingProvider = new MappingProvider();
		posMappingProvider
				.setDefault(
						MappingProvider.LOCATION,
						"classpath:/de/tudarmstadt/ukp/dkpro/"
        + "core/api/lexmorph/tagset/en-c5-pos.map");
		posMappingProvider.setDefault(MappingProvider.BASE_TYPE,
				POS.class.getName());
		posMappingProvider.setDefault("tagger.tagset", "default");
		posMappingProvider.setOverride(MappingProvider.LOCATION,
				"classpath:/de/tudarmstadt/ukp/dkpro/"
				        + "core/api/lexmorph/tagset/en-c5-pos.map");
		posMappingProvider.setOverride(MappingProvider.LANGUAGE, "pl");
		posMappingProvider.setOverride("en-c5-pos.map", "en-c5-pos.map");
		 
        

        String s = "The World Health Organisation projects 40 million infections by the year 2000 . ";
        String tags[] = {"AT0", "NN1", "NN1", "NN1", "NN2-VVZ", "CRD", "CRD", "NN2", "PRP", "AT0", "NN1", "CRD", "PUN"};
        String coarseTags[] = {"ART", "NN", "NN", "NN", "O", "CARD", "CARD", "NN", "PP", "ART", "NN", "CARD", "PUNC"};

        for (JCas jcas : new JCasIterable(reader)) {
			posMappingProvider.configure(jcas.getCas());

            int posid = 0;
		    for (Sentence se : JCasUtil.select(jcas, Sentence.class)) {
		    	 assertEquals(se.getCoveredText(), s);	
		    	 
		        for(Token t : JCasUtil.selectCovered(jcas, Token.class, se)){		        	
		        	assertEquals(t.getPos().getPosValue(), tags[posid]);
		        	
		        	Type posTag = posMappingProvider.getTagType(t.getPos().getPosValue());
		    		POS pos = (POS) jcas.getCas().createAnnotation(posTag, t.getBegin(), t.getEnd());
		    		assertEquals(pos.getClass().getSimpleName(), coarseTags[posid]);		
		            
		        	posid++;
		        }		       
		    }  
        }
    }
}

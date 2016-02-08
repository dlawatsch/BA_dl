package de.unidue.langtech.bachelor.dlawatsch.readers;

import static org.junit.Assert.assertEquals;

import org.apache.uima.cas.Type;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.resources.MappingProvider;
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
                NKJPReader.PARAM_SOURCE_LOCATION, "src/test/resources/reader/",
                NKJPReader.PARAM_PATTERNS, "polish.xml"
        );  
        
        MappingProvider posMappingProvider = new MappingProvider();
		posMappingProvider
				.setDefault(
						MappingProvider.LOCATION,
						"src/main/resources/POSMapping/is.map");
		posMappingProvider.setDefault(MappingProvider.BASE_TYPE,
				POS.class.getName());
		posMappingProvider.setDefault("tagger.tagset", "default");
		posMappingProvider.setOverride(MappingProvider.LOCATION,
				"src/main/resources/POSMapping/pl-ncp-simple.map");
		posMappingProvider.setOverride(MappingProvider.LANGUAGE, "pl");
		posMappingProvider.setOverride("pl-ncp-simple.map", "pl-ncp-simple.map");
		
		
		
        String s = "Zatrzasnął drzwi od mieszkania , dwa razy przekręcił klucz ";
        String tags[] = {"praet", "subst", "prep", "subst", "interp", "num", "subst", "praet", "subst"};
        String coarseTags[] = {"O", "N", "PP", "N", "PUNC", "CARD", "N", "O", "N"};
        
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
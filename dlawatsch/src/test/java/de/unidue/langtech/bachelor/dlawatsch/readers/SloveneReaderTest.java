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
import de.unidue.langtech.bachelor.reader.NKJPReader;
import de.unidue.langtech.bachelor.reader.SloveneReader;

public class SloveneReaderTest {
    @Test
    public void testReaderExample()throws Exception
  {     
        CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
                SloveneReader.class,
                SloveneReader.PARAM_SOURCE_LOCATION, "src/test/resources/reader/",
                SloveneReader.PARAM_PATTERNS, "slovene.xml"
        );

        MappingProvider posMappingProvider = new MappingProvider();
		posMappingProvider
				.setDefault(
						MappingProvider.LOCATION,
						"src/main/resources/POSMapping/sl-SI.map");
		posMappingProvider.setDefault(MappingProvider.BASE_TYPE,
				POS.class.getName());
		posMappingProvider.setDefault("tagger.tagset", "default");
		posMappingProvider.setOverride(MappingProvider.LOCATION,
				"src/main/resources/POSMapping/sl-SI.map");
		posMappingProvider.setOverride(MappingProvider.LANGUAGE, "sl-SI");
		posMappingProvider.setOverride("sl-SI.map", "sl-SI.map");
		
		
        String s = "Obrazec mora imeti tudi serijsko številko , tiskano ali ne , ki omogoča njegovo identifikacijo . ";
        String tags[] = {"Ncmsn", "Vmpr3s", "Vmpn", "Q", "Agpfsa", "Ncfsa", "Interp", "Agpnsa", "Cc", "Q", "Interp", "Cs", "Vmpr3s", "Ps3fsasm", "Ncfsa", "."};
        String coarseTags[] = {"NN", "V", "V", "PRT", "ADJ", "NN", "PUNC", "ADJ", "CONJ", "PRT", "PUNC", "CONJ", "V", "PR", "NN", "PUNC"};

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

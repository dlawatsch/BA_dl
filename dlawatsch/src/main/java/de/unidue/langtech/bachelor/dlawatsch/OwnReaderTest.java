package de.unidue.langtech.bachelor.dlawatsch;

import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.pipeline.SimplePipeline.iteratePipeline;
import static org.apache.uima.fit.util.JCasUtil.select;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.w3c.dom.Document;

import com.google.common.io.Files;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.Resource;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.imscwb.ImsCwbReader;
import de.tudarmstadt.ukp.dkpro.core.io.tei.TeiReader;
import de.tudarmstadt.ukp.dkpro.tc.api.io.TCReaderSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;

public class OwnReaderTest extends JCasResourceCollectionReader_ImplBase {
    
	//@Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);
        }   

    @Override
	public void getNext(JCas aJCas) throws IOException, CollectionException {
		Resource nextFile = nextFile();
    
        CollectionReaderDescription reader;
		try {
			reader = createReaderDescription(
			        ImsCwbReader.class,
					ImsCwbReader.PARAM_SOURCE_LOCATION, "/home/dominik/Dokumente/BA/" ,
			        ImsCwbReader.PARAM_PATTERNS, "*.txt",
					ImsCwbReader.PARAM_LANGUAGE, "de",
					ImsCwbReader.PARAM_SOURCE_ENCODING, "ISO-8859-15",
					ImsCwbReader.PARAM_READ_TOKEN, true,
					ImsCwbReader.PARAM_READ_LEMMA, false,
					ImsCwbReader.PARAM_READ_POS, true,
					ImsCwbReader.PARAM_READ_SENTENCES, false);
			int i = 0;
			for (JCas jcas : iteratePipeline(reader)) {
					System.out.println(jcas.getDocumentText());
					System.out.println("tryin");
			}	
		} catch (ResourceInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    	
    	
    	// TODO Auto-generated method stub
		System.out.println();


				
			
		
	}

	public boolean hasNext() throws IOException, CollectionException {
		return super.hasNext() ;
	}


}

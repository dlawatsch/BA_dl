package de.unidue.langtech.bachelor.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.uima.UIMAException;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.Resource;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.io.bincas.BinaryCasReader;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;

public class ReadBinJCasToJCasForModelGeneration extends JCasResourceCollectionReader_ImplBase{
    public static final String PARAM_BIN_LOCATION = "PARAM_BIN_LOCATION";
    @ConfigurationParameter(name = PARAM_BIN_LOCATION, mandatory = true, defaultValue ="TEST")
    protected String binLocation;
    
    public static final String PARAM_MAX_TOKEN_SIZE = "PARAM_MAX_TOKEN_SIZE";
    @ConfigurationParameter(name = PARAM_MAX_TOKEN_SIZE, mandatory = true, defaultValue = "5")
    protected String maximumTokenSize;
	
    List<Object> allBinaryFiles;

	static Resource currentFileName;
	static int tokencount;
	static int maxTokens;
	Random randomizer = new Random();
	
    public void initialize(UimaContext context)
            throws ResourceInitializationException
        {
            super.initialize(context);  
            List<Object> binaryFileList = Arrays.asList(getResources().toArray());
            allBinaryFiles = new ArrayList<>(binaryFileList);
            tokencount = 0;
            maxTokens = Integer.parseInt(maximumTokenSize);
        }  
    
	public boolean hasNext() throws IOException, CollectionException {
		if(tokencount >= maxTokens){
			System.out.println("ENDE");
			return false;
		}else return true;
	}
    
	@Override
	public void getNext(JCas aJCas) throws IOException, CollectionException {
		int index = randomizer.nextInt(allBinaryFiles.size());
		Resource file = (Resource) allBinaryFiles.get(index);
		allBinaryFiles.remove(index);
		System.out.println(file.getPath());
		
		
		CollectionReader reader;
		try {
			reader = CollectionReaderFactory.createReader(
			        BinaryCasReader.class,
			        BinaryCasReader.PARAM_SOURCE_LOCATION, binLocation,
			        BinaryCasReader.PARAM_PATTERNS, file.getPath(),
			        BinaryCasReader.PARAM_TYPE_SYSTEM_LOCATION, "typesystem.bin");
	        JCas in = JCasFactory.createJCas();
	        
	        reader.getNext(in.getCas());
	        System.out.println(DocumentMetaData.get(in).getDocumentId());
	        DocumentMetaData metaIn = DocumentMetaData.get(in);
	        DocumentMetaData meta = DocumentMetaData.create(aJCas);
	        meta.setDocumentId(metaIn.getDocumentId());
	        in.
			for(Sentence sentence : JCasUtil.select(in, Sentence.class)){
	            TextClassificationSequence sequence = new TextClassificationSequence(in, sentence.getBegin(), sentence.getEnd());
	            sequence.addToIndexes(aJCas);
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
	        for (Sentence sentence : JCasUtil.select(in, Sentence.class)) {
	        	for (Token t : JCasUtil.selectCovered(in, Token.class, sentence)){
	        		tokencount++;
	        	}
	        }
	        aJCas = in;
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        		
	}
}

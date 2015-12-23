package de.unidue.langtech.bachelor.reader;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.w3c.dom.Element;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase.Resource;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;


public class LatinReader extends JCasResourceCollectionReader_ImplBase
		{
    /**
     * Input file
     */
    private String currentSentence;
    private int sentenceCount;
    static int currentDocument;
	static Object[] allDocuments;
	static Resource currentFileName;
	private List<String> lines;
	public static boolean reachedUpperBound;
    /*
     * initializes the reader
     */
    @Override
    public void initialize(UimaContext context)
        throws ResourceInitializationException
    {
        super.initialize(context);
        allDocuments = getResources().toArray();
        currentDocument = 0;
        reachedUpperBound = false;
    }
    
	public Progress[] getProgress() {
	    return new Progress[] { new ProgressImpl(currentDocument, allDocuments.length , "files") };
	}
	
    /*
     * true, if there is a next document, false otherwise
     */
	public boolean hasNext() throws IOException, CollectionException {
		if((currentDocument < allDocuments.length) && !reachedUpperBound){
			return true;
		}else{
			return false;
		}
	}
	

    /*
     * feeds the next document into the pipeline
     */
    @Override
    public void getNext(JCas jcas)
        throws IOException, CollectionException
    {
    	lines.clear();
		Resource nextFile = nextFile();
		System.out.println(nextFile.getLocation());

		lines = FileUtils.readLines(nextFile.getResource().getFile());
		String sentence = "";
        List<String> sentences = new ArrayList<String>();

        
		try{
				for(String line : lines){
					if(line.isEmpty()){
						sentences.add(sentence);
						sentence = "";
						continue;
					}
					sentence += line.trim() +"\n";
				}
				
				String documentText = "";
				List<String> cleanedSentences = new ArrayList<String>();
				List<String> allPos = new ArrayList<String>();
				List<String> allLemma = new ArrayList<String>();
				List<String> allWords = new ArrayList<String>();

				for(String sentenceB : sentences){
					String[] parts = sentenceB.split("\n");
					String actualSentence = "";
					
					for(String part : parts){
						String[] wordPlusPOS = part.split("\t");
							if(wordPlusPOS.length > 5){
			                documentText += wordPlusPOS[1] + " ";
			                allWords.add(wordPlusPOS[1]);
			                allLemma.add(wordPlusPOS[2]);
			                allPos.add(wordPlusPOS[wordPlusPOS.length-3]);
			                actualSentence += wordPlusPOS[1] + " ";
							}
					}
					cleanedSentences.add(actualSentence);	
				}

				jcas.setDocumentText(documentText);

				int sentenceBeginn = 0;
				int sentenceEnd = 0;
				
				for(String sentance : cleanedSentences){
					sentenceEnd += sentance.length();
					Sentence s = new Sentence(jcas, sentenceBeginn, sentenceEnd);
					sentenceBeginn += sentance.length();
					s.addToIndexes();
				}
				
				int wordBeginn = 0;
				int wordEnd = 0;
				int posCount = 0;
				
		        for (Sentence se : JCasUtil.select(jcas, Sentence.class)) {

		        	
		            while(wordEnd < se.getEnd()){
		            	
		            	String word = allWords.get(posCount);
		        	
		        		wordEnd += word.length();
		        		Token token = new Token(jcas, wordBeginn, wordEnd);
		        		wordBeginn += word.length()+1;
		        		wordEnd++;      		        		

		                
				        POS pos = new POS(jcas);
				        pos.setPosValue(allPos.get(posCount));
				        pos.addToIndexes();
				        
				        Lemma lemma = new Lemma(jcas);
				        lemma.setValue(allLemma.get(posCount));
				        lemma.addToIndexes();
				        
				        token.setPos(pos);
				        token.setLemma(lemma);
				        token.addToIndexes();
				        
		                
		        		posCount++;
		        	}

		        }   
	        	jcas.setDocumentLanguage("la");
	        	DocumentMetaData meta = DocumentMetaData.create(jcas);
	        	meta.setDocumentId(nextFile.getLocation());
		}catch (Exception e){
			e.printStackTrace();
		}
		currentDocument++;
    }
}
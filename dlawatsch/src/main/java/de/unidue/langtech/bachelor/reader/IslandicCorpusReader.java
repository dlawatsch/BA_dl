package de.unidue.langtech.bachelor.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
/**
 * Reader for the Icelandic MIM-Gold Corpus
 */

public class IslandicCorpusReader extends JCasResourceCollectionReader_ImplBase
		{


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
     * true, if there is a next document and upper bound has not been reached, 
     * false otherwise
     * the upperboundchecker class checks if the upper bound is reached
     * and changes the boolean in this class 
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
    	Resource nextFile = nextFile();
		lines = FileUtils.readLines(nextFile.getResource().getFile());
		System.out.println(nextFile.getLocation());
		String sentence = "";
        List<String> sentences = new ArrayList<String>();
        
        /*
         * The corpus consists of one word plus its pos tag 
         * per line and empty lines split sentences 
         */
		try{
				for(String line : lines){
					if(line.startsWith("\t")){
						sentences.add(sentence);
						sentence = "";
						continue;
					}
					sentence += line.trim() +"\n";
				}
				
				String documentText = "";
				List<String> cleanedSentences = new ArrayList<String>();
				List<String> allPos = new ArrayList<String>();
				List<String> allWords = new ArrayList<String>();
				
				/*
				 * Split the lines contained in sentences array 
				 * to extract word and POS tag
				 */
				for(String sentenceB : sentences){
					String[] parts = sentenceB.split("\n");
					String actualSentence = "";
					
					for(String part : parts){
						String[] wordPlusPOS = part.split("\t");
						if(wordPlusPOS.length == 2){
								//Whitespaces are needed for building correct sentences
				                documentText += wordPlusPOS[0] + " ";
				                allWords.add(wordPlusPOS[0]); 
				                
				                /* 
				                 * semicolons have to be handled in a special way
				                 * because otherwise they would cause errors later on
				                 */ 				                
				                if(wordPlusPOS[1].equals(",")){
				                	allPos.add("Interp");
				                	
				                }else if(wordPlusPOS[1].contains(",") && wordPlusPOS[1].length()>1){
				                	allPos.add(wordPlusPOS[1].replaceAll(",",""));
				                
				                }else{
				                	allPos.add(wordPlusPOS[1]);
				                }
				                actualSentence += wordPlusPOS[0] + " ";
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
				        
				        token.setPos(pos);
				        token.addToIndexes();  		        
				         
		        		posCount++;             		
		            }  

		        }   

	        	jcas.setDocumentLanguage("is");
	        	DocumentMetaData meta = DocumentMetaData.create(jcas);
	        	meta.setDocumentId(nextFile.getLocation());
		}catch (Exception e){
			e.printStackTrace();
		}
		currentDocument++;
    }
}
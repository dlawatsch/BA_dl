package de.unidue.langtech.bachelor.annotators;

import static org.junit.Assert.*;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.uima.UIMAException;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.JCasIterable;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unidue.langtech.bachelor.reader.IslandicCorpusReader;

public class Build400TokenJcasEachTest {
	
	static List<JCas> allJCas = new ArrayList<JCas>();
	static List<String> allWords = new ArrayList<String>();
	static List<String> allLemma = new ArrayList<String>();
	static List<String> allPOS = new ArrayList<String>();
	static List<String> allSentences = new ArrayList<String>();
	static int numberOfTokens;
	static String documentText;
	
	/*
	 * The test file has over 400 tokens and less than 800, so the outcome jcases schould be 2!
	 */
	@Test
	public void testBuildJcasSegmenter() throws Exception{
		/*
		/*
		 * Icelandic Reader was used because generating test files
		 * for this format is easier than for the other readers
		 */
		CollectionReaderDescription reader = CollectionReaderFactory.createReaderDescription(
				IslandicCorpusReader.class,
				IslandicCorpusReader.PARAM_SOURCE_LOCATION, "src/test/resources/reader/",
				IslandicCorpusReader.PARAM_PATTERNS, "Build400TokenJcasSegmenterTestFile.txt"
				);

		for(JCas jcas : new JCasIterable(reader)){			
			//here the Build 400 Jcas Segmenter starts
			String documentText;
			int numberOfTokens;
			
			numberOfTokens = 0;
			documentText = "";
			
			try {
				/*
				 * until 400 words have been processed or the document end is reached
				 * the sentences are stored in an array as well as pos and lemmas to
				 * annotate them to a new jcas afterwards.
				 */
	        	for (Sentence sentance : JCasUtil.select(jcas, Sentence.class)) {
					
					if(numberOfTokens < 400 || sentance.getEnd() < jcas.getDocumentText().length()){						
						documentText+= sentance.getCoveredText();					
						allSentences.add(sentance.getCoveredText());

				        for (Token t : JCasUtil.selectCovered(jcas, Token.class, sentance)) {
				        	numberOfTokens++;
				        	allWords.add(t.getCoveredText());	
						    allPOS.add(t.getPos().getPosValue());    					        					                 					             
				        }
				        sentance.getCoveredText();					
					}

					/*
					 * if 400 tokens are reached they will be processed to a new jcas.
					 * the current sentence gets included as well to not lose data
					 */
					if(numberOfTokens >= 400 || sentance.getEnd() == jcas.getDocumentText().length()){
						documentText+= sentance.getCoveredText();					
						System.out.println(documentText);
						System.out.println(numberOfTokens);
						allSentences.add(sentance.getCoveredText());

				        for (Token t : JCasUtil.selectCovered(jcas, Token.class, sentance)) {		           
				        	allWords.add(t.getCoveredText());			        	
						        if(t.getLemma() != null){
						        	allLemma.add(t.getLemma().getCoveredText());					        	
						        }
						    allPOS.add(t.getPos().getPosValue());    					        					                 					             
				        }   
				        annotationProcess(documentText);
				        
				        /*
				         * all arrays are cleaned to be ready for the next document
				         */
						allWords.clear();
						allLemma.clear();
						allPOS.clear();
						allSentences.clear();
						documentText = "";
						numberOfTokens = 0;
					}
	        	}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		assertEquals(2, allJCas.size());
	}	
		
		/*
		 * a new jcas is created and annotated with the 400 tokens from above
		 */
		private void annotationProcess(String txt) {
			try {
				JCas out = JCasFactory.createJCas();
		    	DocumentMetaData meta = DocumentMetaData.create(out);
				UUID uniqueID = UUID.randomUUID();	
		    	meta.setDocumentId("FILE_" + String.valueOf(uniqueID));
		    	out.setDocumentLanguage("ISLANDIC");
		    	out.setDocumentText(txt);
		    	int sentenceBeginn = 0;
				int sentenceEnd = 0;
				
				for(String sentance : allSentences){
					sentenceEnd += sentance.length();
					Sentence s = new Sentence(out, sentenceBeginn, sentenceEnd);
					sentenceBeginn += sentance.length();
					s.addToIndexes();
				}

				int wordBeginn = 0;
				int wordEnd = 0;
				int posCount = 0;
				
		        for (Sentence se : JCasUtil.select(out, Sentence.class)) {
		        	while(wordEnd < se.getEnd()){
		            	
		            	String word = allWords.get(posCount);
		        	
		        		wordEnd += word.length();
		        		Token token = new Token(out, wordBeginn, wordEnd);
		        		wordBeginn += word.length()+1;
		        		wordEnd++;       		        		    		        		                          
              
				        POS pos = new POS(out);
				        pos.setPosValue(allPOS.get(posCount));
				        pos.addToIndexes(out);
				        
				        token.setPos(pos);

				        token.addToIndexes(out);  
		        
		        		posCount++;             		
		            }  
		        }
				
				allJCas.add(out);
				
			} catch (UIMAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}			
	}


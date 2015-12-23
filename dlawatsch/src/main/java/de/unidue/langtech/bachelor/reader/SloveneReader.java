package de.unidue.langtech.bachelor.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.tudarmstadt.ukp.dkpro.core.api.io.JCasResourceCollectionReader_ImplBase;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;


public class SloveneReader extends JCasResourceCollectionReader_ImplBase{
    //to determine the amount of all documents some variables are needed here
    static int currentDocument;
	static Element rootElement;
	static Object[] allDocuments;
	static Resource currentFileName;
	List<String> allWords = new ArrayList<String>();
	List<String> allLemma = new ArrayList<String>();
	List<String> allPOS = new ArrayList<String>();
	List<String> allSentences = new ArrayList<String>();
	public static boolean reachedUpperBound;
    /*
     * initializes the reader
     */
    //@Override
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
	
	public boolean hasNext() throws IOException, CollectionException {
		if((currentDocument < allDocuments.length) && !reachedUpperBound){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void getNext(JCas jcas) throws IOException, CollectionException {
		// TODO Auto-generated method stub
		Resource nextFile = nextFile();
		System.out.println(nextFile.getLocation());
		currentFileName = nextFile;
		String documentText = "";
		try {
			//A document builder is needed to process the XML/TML file
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();			
			builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(nextFile.getInputStream());   
			
        	DocumentMetaData meta = DocumentMetaData.create(jcas);
        	meta.setDocumentId(nextFile.getLocation());
        	jcas.setDocumentLanguage("sl");
			rootElement = document.getDocumentElement();

	        //getting all sentences
			NodeList sentencesContainer = rootElement.getElementsByTagName("s");
			for(int i = 0; i < sentencesContainer.getLength(); i++){
				String sentence = buildSentences(sentencesContainer.item(i));
				allSentences.add(sentence);
				documentText += sentence;
			}			
			annotationProcess(jcas, documentText);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		currentDocument++;
	}

	private String buildSentences(Node sentence) {
		//getting all segments of a single sentence
		NodeList allNodesFromCurrentSentenceNodes = sentence.getChildNodes();
		String cleanedSentence = "";
		
		//iterates over all segments of a sentence
		for(int i = 0; i < allNodesFromCurrentSentenceNodes.getLength(); i++){
			Node current = allNodesFromCurrentSentenceNodes.item(i);
			if(current.getNodeName().equals("w")){
					String lemma = getAttributeString(current, "lemma");
					String pos = getAttributeString(current, "ana");
					String word = current.getTextContent();
					allWords.add(word);
					allPOS.add(pos);
					allLemma.add(lemma);
					cleanedSentence += word + " ";
				}
				else if(current.getNodeName().equals("pc")){
					String pos = getAttributeString(current, "ctag");
					String word = current.getTextContent();
					allWords.add(word);
	                if(pos.equals(",")){
	                	allPOS.add("Interp");
	                }else if(pos.contains(",") && pos.length()>1){
	                	allPOS.add(pos.replaceAll(",",""));
	                }else{
	                	allPOS.add(pos);
	                }
					allLemma.add(word);
					cleanedSentence += word + " ";
				}
			}
		return cleanedSentence;
	}

	private String getAttributeString(Node item, String attributename) {
		Element element = (Element) item;
		Attr attrName = element.getAttributeNode(attributename);
		return attrName.getTextContent();
	}
	
	private void annotationProcess(JCas jcas, String documentText) {
		jcas.setDocumentText(documentText);
		//System.out.println(documentText);
		int sentenceBeginn = 0;
		int sentenceEnd = 0;
		
		for(String sentance : allSentences){
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
                
                Lemma lemma = new Lemma(jcas);
                lemma.setValue(allLemma.get(posCount));
                lemma.addToIndexes();
                
		        POS pos = new POS(jcas);
		        pos.setPosValue(allPOS.get(posCount));
		        pos.addToIndexes();
		        
		        token.setPos(pos);
		        token.setLemma(lemma);
		        token.addToIndexes();  	        
                
        		posCount++;                		
            }

        }
	}
}

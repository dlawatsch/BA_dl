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
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;

public class NKJPReader extends JCasResourceCollectionReader_ImplBase{
    //to determine the amount of all documents some variables are needed here
    static int currentDocument;
	static Element rootElement;
	static Object[] allDocuments;
	static Resource currentFileName;
	String documentText = "";
	List<String> allWords = new ArrayList<String>();
	List<String> allLemma = new ArrayList<String>();
	List<String> allPOS = new ArrayList<String>();
	List<String> allSentences = new ArrayList<String>();

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
        }   
    
	public Progress[] getProgress() {
	    return new Progress[] { new ProgressImpl(currentDocument, allDocuments.length , "files") };
	}
	
	public boolean hasNext() throws IOException, CollectionException {
		return super.hasNext() ;
	}
	
	@Override
	public void getNext(JCas jcas) throws IOException, CollectionException {
		// TODO Auto-generated method stub
		Resource nextFile = nextFile();
		currentFileName = nextFile;
		
		try {
			//A document builder is needed to process the XML/TML file
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();			
			builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(nextFile.getInputStream());   
			
        	DocumentMetaData meta = DocumentMetaData.create(jcas);
        	meta.setDocumentId(nextFile.getLocation());
        	jcas.setDocumentLanguage("pl");
			rootElement = document.getDocumentElement();

	        //getting all sentences
			NodeList sentencesContainer = rootElement.getElementsByTagName("s");
			for(int i = 0; i < sentencesContainer.getLength(); i++){
				String sentence = buildSentences(sentencesContainer.item(i));
				allSentences.add(sentence);
				documentText += sentence;
			}			
			annotationProcess(jcas);
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private String buildSentences(Node sentence) {
		//getting all segments of a single sentence
		NodeList allNodesFromCurrentSentenceNodes = sentence.getChildNodes();
		String cleanedSentence = "";
		
		//iterates over all segments of a sentence
		for(int i = 0; i < allNodesFromCurrentSentenceNodes.getLength(); i++){
			
			//each segment contains nodes with the word, lemma and POS but need to be iterated seperatly
			NodeList sentenceSegmentNodes = allNodesFromCurrentSentenceNodes.item(i).getChildNodes();
			for(int j = 0; j < sentenceSegmentNodes.getLength(); j++){
				Node current = sentenceSegmentNodes.item(j);
				
				//actual words are nodes in "fs" xml types
				if(current.getNodeName().equals("fs")){
					NodeList currentWordNodeList = current.getChildNodes();
															
					//iterating over actual annotations
					for(int z = 0; z < currentWordNodeList.getLength(); z++){
						Node c = currentWordNodeList.item(z);
						
						//had problems with casting to Element type. workaround: 
						//http://stackoverflow.com/questions/21170909/error-org-apache-xerces-dom-deferredtextimpl-cannot-be-cast-to-org-w3c-dom-elem
						if(currentWordNodeList.item(z).getNodeType() == Node.ELEMENT_NODE){
							NodeList annotation = c.getChildNodes();
							String attribute = getAttributeString(currentWordNodeList.item(z), "name");
							
							if(attribute.equals("orth")){
								for(int o = 0; o < annotation.getLength(); o++){
									if(testNodeString(annotation.item(o).getNodeName())){
										String orthographic = annotation.item(o).getTextContent();	
										allWords.add(orthographic);
										cleanedSentence += orthographic + " ";
									}
								}
							}
							
							if(attribute.equals("base")){
								for(int o = 0; o < annotation.getLength(); o++){
									if(testNodeString(annotation.item(o).getNodeName())){
										String base = annotation.item(o).getTextContent();
										allLemma.add(base);
									}
								}
							}
							
							if(attribute.equals("ctag")){
								for(int o = 0; o < annotation.getLength(); o++){
									if(annotation.item(o).getNodeName().equals("symbol")){
										String POS = getAttributeString ((Element) annotation.item(o), "value");
										allPOS.add(POS);
									}
								}
							}
						}
					}	
				}
			}
		}
		return cleanedSentence;
	}
	

	private boolean testNodeString(String string) {
		if(string.equals("string")){
			return true;
		}else return false;
	}

	private String getAttributeString(Node item, String attributename) {
		Element element = (Element) item;
		Attr attrName = element.getAttributeNode(attributename);
		return attrName.getTextContent();
	}
	
	private void annotationProcess(JCas jcas) {
		jcas.setDocumentText(documentText);
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
        	TextClassificationSequence sequence = new TextClassificationSequence(jcas, se.getBegin(), se.getEnd());
            sequence.addToIndexes();  
            
            while(wordEnd < se.getEnd()){
            	
            	String word = allWords.get(posCount);
        	
        		wordEnd += word.length();
        		Token token = new Token(jcas, wordBeginn, wordEnd);
        		wordBeginn += word.length()+1;
        		wordEnd++;        		        		    		
        		
                TextClassificationUnit unit = new TextClassificationUnit(jcas, token.getBegin(), token.getEnd());
                unit.setSuffix(token.getCoveredText());
                unit.addToIndexes();
                
                Lemma lemma = new Lemma(jcas);
                lemma.setValue(allLemma.get(posCount));
                lemma.addToIndexes();
                
		        POS pos = new POS(jcas);
		        pos.setPosValue(allPOS.get(posCount));
		        pos.addToIndexes();
		        
		        token.setPos(pos);
		        token.setLemma(lemma);
		        token.addToIndexes();  
		        
		        
                TextClassificationOutcome outcome = new TextClassificationOutcome(jcas, token.getBegin(), token.getEnd());
                outcome.setOutcome(pos.getPosValue());
                outcome.addToIndexes();   
                
        		posCount++;             		
            }  
        }
	}
}

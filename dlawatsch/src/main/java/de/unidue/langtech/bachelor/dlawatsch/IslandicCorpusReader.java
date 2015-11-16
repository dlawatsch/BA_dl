package de.unidue.langtech.bachelor.dlawatsch;

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
import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationOutcome;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationSequence;
import de.tudarmstadt.ukp.dkpro.tc.api.type.TextClassificationUnit;


public class IslandicCorpusReader extends JCasResourceCollectionReader_ImplBase
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
    }
    
	public Progress[] getProgress() {
	    return new Progress[] { new ProgressImpl(currentDocument, allDocuments.length , "files") };
	}
	
    /*
     * true, if there is a next document, false otherwise
     */
	public boolean hasNext() throws IOException, CollectionException {
		return super.hasNext() ;
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
		String sentence = "";
        List<String> sentences = new ArrayList<String>();
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
				for(String sentenceB : sentences){
					String[] parts = sentenceB.split("\n");
					for(String part : parts){
						String[] wordPlusPOS = part.split("\t");
							if(wordPlusPOS.length == 2){
							int end = wordPlusPOS[0].length();
							Token t = new Token (jcas, 0, end);
			                TextClassificationUnit unit = new TextClassificationUnit(jcas, t.getBegin(), t.getEnd());
			                
			                // will add the token content as a suffix to the ID of this unit 
			                unit.setSuffix(t.getCoveredText());
			               // List<POS> posList = JCasUtil.selectCovered(jcas, POS.class, unit);
//			                for (POS pos : posList){
//			                	System.out.println(pos.getClass().getSimpleName());
//			                }
			               // System.out.println(t.getCoveredText());
			                unit.addToIndexes();
			                TextClassificationOutcome outcome = new TextClassificationOutcome(jcas, t.getBegin(), t.getEnd());
			                outcome.setOutcome(wordPlusPOS[1]);
			                outcome.addToIndexes();
			                documentText += wordPlusPOS[0] + " ";
							}
					}
				}
				jcas.setDocumentText(documentText.trim());
				//System.out.println(jcas.getDocumentText());
			
		}catch (Exception e){
			e.printStackTrace();
		}
    }
}
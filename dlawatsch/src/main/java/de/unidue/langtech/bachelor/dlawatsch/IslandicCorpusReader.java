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
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;


public class IslandicCorpusReader extends JCasCollectionReader_ImplBase
		{
    /**
     * Input file
     */
    public static final String PARAM_INPUT_FILE = "InputFile";
    @ConfigurationParameter(name = PARAM_INPUT_FILE, mandatory = true)
    private String inputFile;
    String documentText;
    private String[] sentences;
    private String currentSentence;
    private int sentenceCount;

    /*
     * initializes the reader
     */
    @Override
    public void initialize(UimaContext context)
        throws ResourceInitializationException
    {
        super.initialize(context);
        try {
            URL resourceUrl = ResourceUtils.resolveLocation(inputFile);

            documentText = FileUtils.readFileToString(new File(resourceUrl.toURI())); 
            String[] parts = documentText.split("\\t\\t\\t*");
                for(String element : parts){
                	System.out.println(element);
                	System.out.println("!!!!!!!!!!!!!!!!!!!sentence end!");
                		String[] wordPlusPOS = element.split("\\n");
	                	for(int i = 1; i < wordPlusPOS.length-2; i+=2){
	                		if(wordPlusPOS[i].length() > 1){
	                		System.out.println("Token: " + wordPlusPOS[i] + " POS: " + wordPlusPOS[i+1] + " !!");
	                		}
	                	}
                }
            
        } 
        catch (IOException e) {
        	e.printStackTrace();
        }
        catch (URISyntaxException ex) {
        	ex.printStackTrace();
        }	    
        System.out.println(inputFile.length());
        System.out.println("test");
//        	documentText = FileUtils.readLines(inputFile);
//        	for(String out : documentText){
//        		System.out.println(out);
//        	}
//        	sentences = documentText.split("");
		System.out.println(documentText);
		sentenceCount = 0;
    }
    
    /*
     * true, if there is a next document, false otherwise
     */
    public boolean hasNext()
        throws IOException, CollectionException
    {
        return sentenceCount < sentences.length;
    }

    /*
     * feeds the next document into the pipeline
     */
    @Override
    public void getNext(JCas jcas)
        throws IOException, CollectionException
    {
//        List<String> tupel = new ArrayList<String>();
//
//        // read all lines that belong to one tupel (language code + sentence)
//        // an empty line separates tupel
//        String next = null;
//        while (hasNext() && !(next = lines.get(currentLine)).isEmpty()) {
//            tupel.add(next);
//            currentLine++;
//        }
//
//        // add gold standard value as annotation
//        // the first line is the language code
//        GoldLanguage goldLanguage = new GoldLanguage(jcas);
//        goldLanguage.setLanguage(tupel.get(0));
//        goldLanguage.addToIndexes();
//
//        // add actual text of the document
//        // we will add each word as own token and then set the document text
//
//        String documentText = "";
//        for (int i = 1; i < tupel.size(); i++) {
//            String word = tupel.get(i);
//            documentText += word;
//
//            // add the token annotated as own type
//            int start = documentText.length() - word.length();
//            int end = documentText.length();
//            Token t = new Token(jcas, start, end);
//            t.addToIndexes();
//
//            // append space as separator for next token
//            documentText += " ";
//        }
//
//        jcas.setDocumentText(documentText.trim());
//
//        currentLine++;
    }

    /*
     * informs the pipeline about the current progress
     */
    public Progress[] getProgress()
    {
        return new Progress[] { new ProgressImpl(sentenceCount, sentences.length, "sentences") };
    }
}
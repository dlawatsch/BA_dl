package de.unidue.langtech.bachelor.dlawatsch;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils;

public class IslandicTest {
	public static void main(String[] args) {
        try {
            URL resourceUrl = ResourceUtils.resolveLocation("/home/dominik/Dokumente/BA/blog.txt");

            for (String line : FileUtils.readLines(new File(resourceUrl.toURI()))) {
                String[] parts = line.split("\n");

                for(String element : parts){
                	String[] wordPlusPOS = element.split("\t");
                	for(String x : wordPlusPOS){
                		System.out.println(x);
                	}
                }
//                if (parts.length < 2) {
//                    throw new IOException("Wrong file format in line: " + line);
//                }
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }
        catch (URISyntaxException ex) {
        	ex.printStackTrace();
        }	
	}
}	

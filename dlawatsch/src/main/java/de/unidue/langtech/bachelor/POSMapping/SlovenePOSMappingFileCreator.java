package de.unidue.langtech.bachelor.POSMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;

public class SlovenePOSMappingFileCreator {
	/*
	 * Load the created frequency distributions
	 * that have been created at the baseline processing
	 * and get all different pos tags and do the mapping.
	 * Results are presented on the console and have to be saved manually
	 */
	
	public static List<String> adj = new ArrayList<String>();
	public static List<String> adv = new ArrayList<String>();
	public static List<String> card = new ArrayList<String>();
	public static List<String> conj = new ArrayList<String>();
	public static List<String> nn = new ArrayList<String>();
	public static List<String> np = new ArrayList<String>();
	public static List<String> pp = new ArrayList<String>();
	public static List<String> pr = new ArrayList<String>();
	public static List<String> prt = new ArrayList<String>();
	public static List<String> punc = new ArrayList<String>();
	public static List<String> v = new ArrayList<String>();
	public static List<String> o = new ArrayList<String>();
	public static void main(String[] args) {
		File outputFile = new File("/home/dominikl/Dokumente/BA/CORPORA/LANGUAGES/SLOVENE/FREQUENCIES/100000_FREQUENCIES");
        FrequencyDistribution<String> loadedFd = new FrequencyDistribution<String>();
        try {
			loadedFd.load(outputFile);
			System.out.println(loadedFd.getB());
			for(String a : loadedFd.getKeys()){
				analysePOS(a);
			}
			
			List<String> allMapping = new ArrayList<String>();
			allMapping.addAll(adj);
			allMapping.addAll(adv);
			allMapping.addAll(card);
			allMapping.addAll(conj);
			allMapping.addAll(nn);
			allMapping.addAll(np);
			allMapping.addAll(pp);
			allMapping.addAll(pr);
			allMapping.addAll(prt);
			allMapping.addAll(punc);
			allMapping.addAll(v);
			allMapping.addAll(o);
			
			for(String a : allMapping){
				System.out.println(a);
			}
			//String posMapping = analysePOS(pos);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void analysePOS(String pos) {
		// TODO Auto-generated method stub
		if(pos.toLowerCase().startsWith("n")){
			//proper noun
			if(pos.toLowerCase().startsWith("np")){
				np.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NP");
			}
			//common noun
			else{
				 nn.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN");
			}
			
		}else if(pos.toLowerCase().startsWith("a")){
			//adjective
			 	adj.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADJ");
			
		}else if(pos.toLowerCase().startsWith("s")){
			//adposition
		 	pp.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PP");
		
	}else if(pos.toLowerCase().startsWith("r")){
			//adverb
			adv.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADV");
			
		}else if(pos.toLowerCase().startsWith("p")){
			//pronoun
			pr.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PR");
			
		}else if(pos.toLowerCase().startsWith("m")){
			//numeric
			card.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD");
			
		}
		else if(pos.toLowerCase().startsWith("q")){
			//particle
			prt.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PRT");			
		}
		
		else if(pos.toLowerCase().startsWith("v")){
			//verb
			 v.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.V");
			
		}
		else if(pos.toLowerCase().startsWith("c")){
			//conjunction
			conj.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CONJ");
		
		}else if(pos.startsWith("y")  || pos.startsWith("x")){
			//foreign word or unclassified
			o.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.O");
			
		}else if(pos.toLowerCase().equals("Interp") || pos.length()==1 || pos.contains(".") || pos.contains("(") || pos.contains(")") || pos.contains("!") || pos.contains("?") || pos.contains("-") || pos.contains("»") || pos.contains(":")){
			//punctuation
			punc.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PUNC");
		}	
	}
}

//# Catch-all rule
//*=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.O
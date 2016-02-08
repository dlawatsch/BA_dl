package de.unidue.langtech.bachelor.POSMapping;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;

/*
 * Load the created frequency distributions
 * that have been created at the baseline processing
 * and get all different pos tags and do the mapping.
 * Results are presented on the console and have to be saved manually
 */
public class IcelandicPOSMappingFileCreator {
	public static List<String> adj = new ArrayList<String>();
	public static List<String> adv = new ArrayList<String>();
	public static List<String> art = new ArrayList<String>();
	public static List<String> card = new ArrayList<String>();
	public static List<String> conj = new ArrayList<String>();
	public static List<String> nn = new ArrayList<String>();
	public static List<String> np = new ArrayList<String>();
	public static List<String> pr = new ArrayList<String>();
	public static List<String> punc = new ArrayList<String>();
	public static List<String> v = new ArrayList<String>();
	public static List<String> o = new ArrayList<String>();
	public static void main(String[] args) {
		File outputFile = new File("/home/dominikl/Dokumente/BA/CORPORA/LANGUAGES/ISLANDIC/FREQUENCIES/100000_FREQUENCIES");
        FrequencyDistribution<String> loadedFd = new FrequencyDistribution<String>();
        try {
			loadedFd.load(outputFile);
			for(String a : loadedFd.getKeys()){
				analysePOS(a);
			}
			punc.add("INTERP=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PUNC");
			
			List<String> allMapping = new ArrayList<String>();
			allMapping.addAll(adj);
			allMapping.addAll(adv);
			allMapping.addAll(art);
			allMapping.addAll(card);
			allMapping.addAll(conj);
			allMapping.addAll(nn);
			allMapping.addAll(np);
			allMapping.addAll(pr);
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
			if(pos.toLowerCase().endsWith("s")){
				np.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NP");
				if(pos.toLowerCase().contains("þ")){					
					np.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NP");
				}
			}
			//common noun
			else{
				 nn.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN");
					if(pos.toLowerCase().contains("þ")){					
						nn.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.NN");
					}
			}
			
		}else if(pos.toLowerCase().startsWith("l")){
			//adjective
			 	adj.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADJ");
				if(pos.toLowerCase().contains("þ")){					
					adj.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADJ");
				}
			
		}else if(pos.toLowerCase().startsWith("a")){
			//adverb
			adv.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADV");
			if(pos.toLowerCase().contains("þ")){					
				adv.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ADV");
			}
		}else if(pos.toLowerCase().startsWith("f")){
			//pronoun
			pr.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PR");
			if(pos.toLowerCase().contains("þ")){					
				pr.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PR");
			}
		}else if(pos.toLowerCase().startsWith("g")){
			//article
			art.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ART");
			if(pos.toLowerCase().contains("þ")){					
				art.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.ART");
			}
		}else if(pos.toLowerCase().startsWith("t")){
			//numeric
			card.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD");
			if(pos.toLowerCase().contains("þ")){					
				card.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CARD");
			}
		}
		else if(pos.toLowerCase().startsWith("s")){
			//verb
			 v.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.V");
				if(pos.toLowerCase().contains("þ")){					
					v.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.V");
				}
		}
		else if(pos.toLowerCase().startsWith("c")){
			//conjunction
			conj.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CONJ");
			if(pos.toLowerCase().contains("þ")){					
				conj.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.CONJ");
			}
		}else if(pos.toLowerCase().startsWith("e") || pos.startsWith("x")){
			//foreign word or unclassified
			o.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.O");
			if(pos.toLowerCase().contains("þ")){					
				o.add(pos.replaceAll("þ", "XX")+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.O");
			}
		}else if(pos.toLowerCase().equals("Interp") || pos.length()==1 || pos.contains(".") || pos.contains("(") || pos.contains(")") || pos.contains("!") || pos.contains("?") || pos.contains("-") || pos.contains("»") || pos.contains(":")){
			//punctuation
			punc.add(pos+"=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.PUNC");
		}
	}
}

//# Catch-all rule
//*=de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.O
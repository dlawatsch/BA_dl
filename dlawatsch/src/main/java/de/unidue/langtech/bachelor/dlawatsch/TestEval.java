package de.unidue.langtech.bachelor.dlawatsch;



import java.util.Collection;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;



//simple Baseline solution, which annotates the detected document creation time to each timex event
public class TestEval extends JCasAnnotator_ImplBase{

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		System.out.println("test");
	}
}

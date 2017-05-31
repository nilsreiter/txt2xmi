package de.unistuttgart.ims.creta.main.txt2xmi;

import java.text.BreakIterator;
import java.util.Locale;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.factory.AnnotationFactory;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

public class SegmentAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		BreakIterator bi = BreakIterator.getWordInstance(Locale.GERMANY);

		bi.setText(jcas.getDocumentText());
		int start = bi.first();
		for (int end = bi.next(); end != BreakIterator.DONE; start = end, end = bi.next()) {
			if (!jcas.getDocumentText().substring(start, end).matches("^\\s*$"))
				AnnotationFactory.createAnnotation(jcas, start, end, Token.class);
		}

		int currentIndex = 0;
		int nextNewLine = jcas.getDocumentText().indexOf("\n", currentIndex);
		while (nextNewLine > 0) {
			AnnotationFactory.createAnnotation(jcas, currentIndex, nextNewLine, Sentence.class);
			currentIndex = nextNewLine + 1;
			nextNewLine = jcas.getDocumentText().indexOf("\n", currentIndex);
		}
	}

}

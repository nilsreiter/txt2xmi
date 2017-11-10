package de.unistuttgart.ims.creta.main.txt2xmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class DictionaryAnnotator extends JCasAnnotator_ImplBase {

	final static public String PARAM_TARGET_CLASS = "Target Class";
	final static public String PARAM_DICTIONARY_FILE = "Dictionary File";

	@ConfigurationParameter(name = PARAM_TARGET_CLASS)
	String targetClassName;

	@ConfigurationParameter(name = PARAM_DICTIONARY_FILE)
	String dictionaryFileUri;

	Class<? extends Annotation> targetClass;

	Type targetType;

	File dictionaryUri;

	List<String> dictionary;

	@Override
	public void initialize(final UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		// targetClass = TypeParameterUtil.getClass(targetClassName);
		try {
			dictionaryUri = new File(dictionaryFileUri);
			dictionary = IOUtils.readLines(new FileInputStream(dictionaryUri));
			System.err.println("Dictionary has " + dictionary.size() + " entries.");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new ResourceInitializationException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResourceInitializationException(e);
		}
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		targetType = aJCas.getCas().getTypeSystem().getType(targetClassName);
		System.err.println(targetType.getName());
		String text = aJCas.getDocumentText();
		for (String s : dictionary) {
			Pattern p = Pattern.compile(s);
			Matcher m = p.matcher(text);
			while (m.find()) {
				AnnotationFS a = aJCas.getCas().createAnnotation(targetType, m.start(), m.end());
				aJCas.addFsToIndexes(a);
				// Annotation a = AnnotationFactory.createAnnotation(aJCas,
				// m.start(), m.end(), targetClass);
				System.err.println("Annotating " + a.getCoveredText() + " as " + targetClass);
			}
		}
	}

}

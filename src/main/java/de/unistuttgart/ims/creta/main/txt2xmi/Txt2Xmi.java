package de.unistuttgart.ims.creta.main.txt2xmi;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;

import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;

public class Txt2Xmi {

	public static void main(String[] args) throws Exception {
		Options options = CliFactory.parseArguments(Options.class, args);

		File[] dictionaryFiles;
		if (options.getDictionaryDirectory() != null)
			dictionaryFiles = options.getDictionaryDirectory().listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith("txt");
				}
			});
		else
			dictionaryFiles = new File[] {};
		System.err.println("Found " + dictionaryFiles.length + " dictionary file(s): " + dictionaryFiles.toString());

		CollectionReaderDescription crd = CollectionReaderFactory.createReaderDescription(TextReader.class,
				TextReader.PARAM_SOURCE_LOCATION, options.getInput() + File.separator + "*.txt");

		AggregateBuilder b = new AggregateBuilder();
		b.add(AnalysisEngineFactory.createEngineDescription(SegmentAnnotator.class));
		for (File f : dictionaryFiles) {
			String name = "webanno.custom." + f.getName().replace(".txt", "");
			b.add(AnalysisEngineFactory.createEngineDescription(DictionaryAnnotator.class,
					DictionaryAnnotator.PARAM_DICTIONARY_FILE, f.getAbsolutePath(),
					DictionaryAnnotator.PARAM_TARGET_CLASS, name));
		}
		b.add(AnalysisEngineFactory.createEngineDescription(XmiWriter.class, XmiWriter.PARAM_TARGET_LOCATION,
				options.getOutput(), XmiWriter.PARAM_OVERWRITE, true));
		SimplePipeline.runPipeline(crd, b.createAggregateDescription());

	}

	public interface Options {
		@Option()
		File getInput();

		@Option()
		File getOutput();

		@Option(defaultToNull = true)
		File getDictionaryDirectory();

	}
}

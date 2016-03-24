package com.beust.doclipse.doclet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.generama.ConfigurableDocletTagFactory;
import org.generama.GeneramaException;
import org.generama.QDoxCapableMetadataProvider;
import org.picocontainer.Startable;
import org.xdoclet.JavaSourceProvider;
import org.xdoclet.ModelCheckerTagFactory;
import org.xdoclet.QDoxPropertyExpander;

import com.beust.doclipse.builder.ClassManager;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;

public class DefaultQDoxCapableMetadataProvider  implements QDoxCapableMetadataProvider, Startable{
	private ClassManager classManager;
	private JavaClass javaClass;
	private File javaFile;
	
	public File getJavaFile() {
		return javaFile;
	}
	public JavaClass getJavaClass() {
		return javaClass;
	}
	public DefaultQDoxCapableMetadataProvider(File javaFile,QDoxPropertyExpander expander,ClassManager classManager) {
		this(javaFile,expander);
		this.javaFile=javaFile;
		this.classManager=classManager;
		this.getMetadata();
	}

	private final ConfigurableDocletTagFactory docletTagFactory;

	private QDoxPropertyExpander expander;

	private File singleSourceOrDirectory;

	private JavaSourceProvider urlProvider;

	private Boolean verbose;

	/**
	 * Convenience constructor for testing. If singleSourceOrDirectory is a
	 * directory, all sources in it will be parsed.
	 */
	public DefaultQDoxCapableMetadataProvider(final File singleSourceOrDirectory) {
		if (!singleSourceOrDirectory.exists()) {
			throw new IllegalArgumentException(
					singleSourceOrDirectory.getAbsolutePath() + " doesn't exist! No Java sources to parse.");
		}
		this.singleSourceOrDirectory = singleSourceOrDirectory;
		this.docletTagFactory = new DocletTagFactoryImpl();
	}

	/**
	 * Convenience constructor for testing. If singleSourceOrDirectory is a
	 * directory, all sources in it will be parsed.
	 */
	public DefaultQDoxCapableMetadataProvider(final File singleSourceOrDirectory, QDoxPropertyExpander expander) {
		if (!singleSourceOrDirectory.exists()) {
			throw new IllegalArgumentException(
					singleSourceOrDirectory.getAbsolutePath() + " doesn't exist! No Java sources to parse.");
		}
		this.singleSourceOrDirectory = singleSourceOrDirectory;
		this.docletTagFactory = new ExpanderConfigurableDocletTagFactorySub(expander);
	}

	/**
	 * be verbose by default
	 */
	public DefaultQDoxCapableMetadataProvider(JavaSourceProvider fileProvider) {
		this(fileProvider, Boolean.TRUE);
	}

	/**
	 * Main constructor. Gives fine control over what sources to parse.
	 */
	public DefaultQDoxCapableMetadataProvider(JavaSourceProvider fileProvider, Boolean verbose) {
		this.urlProvider = fileProvider;
		this.verbose = verbose;
		this.docletTagFactory = new DocletTagFactoryImpl();
	}

	/**
	 * be verbose by default
	 */
	public DefaultQDoxCapableMetadataProvider(JavaSourceProvider fileProvider, QDoxPropertyExpander expander) {
		this(fileProvider, expander, Boolean.TRUE);
	}

	/**
	 * Main constructor, with extends property expander.
	 */
	public DefaultQDoxCapableMetadataProvider(JavaSourceProvider fileProvider, QDoxPropertyExpander expander, Boolean verbose) {
		this.urlProvider = fileProvider;
		this.expander = expander;
		this.verbose = verbose;
		this.docletTagFactory = new ExpanderConfigurableDocletTagFactorySub(expander);
	}

	/**
	 * be werbose by default.
	 */
	public DefaultQDoxCapableMetadataProvider(final URL singleSource) {
		this(singleSource, Boolean.TRUE);
	}

	/**
	 * Convenience constructor for testing.
	 */
	public DefaultQDoxCapableMetadataProvider(final URL singleSource, Boolean verbose) {
		this(new JavaSourceProvider() {
			public String getEncoding() {
				return "ISO-8859-1";
			}

			public Collection getURLs() {
				return Collections.singleton(singleSource);
			}
		}, verbose);
	}

	private void addSourcesFromJavaSourceProvider(JavaDocBuilder builder) throws IOException {
		Collection urls = urlProvider.getURLs();
		for (Iterator iterator = urls.iterator(); iterator.hasNext();) {
			URL next = (URL) iterator.next();
			builder.addSource(next);
		}
	}

	public ConfigurableDocletTagFactory getDocletTagFactory() {
		return docletTagFactory;
	}

	/* For testing only */
	QDoxPropertyExpander getExpander() {
		return this.expander;
	}
	@Override
	public Collection getMetadata() throws GeneramaException {
		if(this.javaClass!=null){
			List<JavaClass> lt=new ArrayList<JavaClass>();
			lt.add(javaClass);
			return lt;
		}
		this.javaClass=this.classManager.getJavaClassByFile(this.javaFile);
		if(this.javaClass!=null){
			List<JavaClass> lt=new ArrayList<JavaClass>();
			lt.add(javaClass);
			return lt;
		}
		Collection coll= getMetadatas();
		Iterator lterator=coll.iterator();
		if(lterator.hasNext()){
			this.javaClass=(JavaClass) lterator.next();
		}
		if(this.javaClass!=null)
		classManager.save(this);
		return coll;
	}
	/**
	 * @return a sorted Collection of {@link JavaClass}.
	 */
	public Collection getMetadatas() {

		try {
			ModelCheckerTagFactory modelChecker = new ModelCheckerTagFactory(docletTagFactory);
			JavaDocBuilder builder = new JavaDocBuilder(modelChecker);
			if (urlProvider != null) {
				builder.setEncoding(urlProvider.getEncoding());
				addSourcesFromJavaSourceProvider(builder);
			} else {
				if (singleSourceOrDirectory.isDirectory()) {
					builder.addSourceTree(singleSourceOrDirectory);
				} else {
					builder.addSource(singleSourceOrDirectory);
				}
			}
			modelChecker.validateModel();
			List result = Arrays.asList(builder.getClasses());
			Collections.sort(result);
			return result;
		} catch (IOException e) {
			throw new RuntimeException("Couldn't parse Java Sources", e);
		}
	}

	public String getOriginalFileName(Object metadata) {
		String result;
		if (metadata instanceof JavaClass) {
			JavaClass javaClass = (JavaClass) metadata;
			result = javaClass.getName() + ".java";
		} else {
			result = "";
		}
		return result;
	}

	public String getOriginalPackageName(Object metadata) {
		if (metadata instanceof JavaClass) {
			JavaClass javaClass = (JavaClass) metadata;
			return javaClass.getPackage().getName();
		}
		return "";
	}

	public void start() {
	}

	public void stop() {
		if (verbose.booleanValue()) {
			docletTagFactory.printUnknownTags();
		}
	}

}

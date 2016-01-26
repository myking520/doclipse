package com.beust.doclipse.builder.plugin;

import java.io.File;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.TemplateEngine;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;
import org.xdoclet.QDoxMetadataProvider;
import org.xdoclet.tools.PropertiesQDoxPropertyExpander;

import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.preferences.template.TemplateElement;

public abstract class AbstractPluginBuilder {
	public QDoxPlugin build(DoclipseProject doclipseProject, TemplateElement element){
		
		String input = element.getText();
		TemplateElement outputElement = element.getChildrenByKind(TemplateElement.CPE_EXPORT);
		if (outputElement == null) {
			throw new RuntimeException("no out put dir");
		}
		String outFileName=null;
		int s = input.lastIndexOf(File.separatorChar);
		if (s != -1) {
			outFileName=input.substring(s + 1);
		} else {
			outFileName= input;
		}
		FileWriterMapper fileWriterMapper=new FileWriterMapper(outputElement.getText(),outFileName);
		PropertiesQDoxPropertyExpander expander = new PropertiesQDoxPropertyExpander();
		Properties props = new Properties();
		props.setProperty("value", "props-test-value");
		expander.addProperties("props", props);
		QDoxMetadataProvider metadataProvider = new QDoxMetadataProvider(new File(input), expander);
		return this.build(metadataProvider, fileWriterMapper);
	}
	public abstract QDoxPlugin build(QDoxCapableMetadataProvider metadataProvider,
			WriterMapper writerMapper);
}

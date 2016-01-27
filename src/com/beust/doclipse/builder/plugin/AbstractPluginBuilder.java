package com.beust.doclipse.builder.plugin;

import java.io.File;
import java.util.Properties;

import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;
import org.xdoclet.QDoxMetadataProvider;
import org.xdoclet.tools.PropertiesQDoxPropertyExpander;

import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.preferences.template.TemplateElement;

public abstract class AbstractPluginBuilder {
	public QDoxPlugin build(DoclipseProject doclipseProject, TemplateElement java,TemplateElement importTemplete){
		String importTempleteName=importTemplete.getText();
		TemplateElement outputElement = importTemplete.getChildrenByKind(TemplateElement.CPE_EXPORT);
		if (outputElement == null) {
			throw new RuntimeException("no out put dir");
		}
		String outFileName=null;
		int s = importTempleteName.lastIndexOf(File.separatorChar);
		if (s != -1) {
			outFileName=importTempleteName.substring(s + 1);
		} else {
			outFileName= importTempleteName;
		}
		s = outFileName.lastIndexOf(".");
		if (s != -1) {
			outFileName=outFileName.substring(0,s);
		} 
		FileWriterMapper fileWriterMapper=new FileWriterMapper(outputElement.getText(),outFileName);
		PropertiesQDoxPropertyExpander expander = new PropertiesQDoxPropertyExpander();
		Properties props = new Properties();
//		props.setProperty("value", "props-test-value");
		expander.addProperties("props", props);
		QDoxMetadataProvider metadataProvider = new QDoxMetadataProvider(doclipseProject.getProject().getFile(java.getText()).getLocation().toFile(), expander);
		return this.build(metadataProvider, fileWriterMapper,importTemplete.getText());
	}
	public abstract QDoxPlugin build(QDoxCapableMetadataProvider metadataProvider,
			WriterMapper writerMapper,String temlate);
}

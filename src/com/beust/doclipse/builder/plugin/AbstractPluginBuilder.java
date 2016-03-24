package com.beust.doclipse.builder.plugin;

import java.io.File;
import java.util.Properties;

import org.eclipse.ui.console.MessageConsoleStream;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;
import org.xdoclet.QDoxMetadataProvider;
import org.xdoclet.tools.PropertiesQDoxPropertyExpander;

import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.builder.ClassManager;
import com.beust.doclipse.console.ConsoleFactory;
import com.beust.doclipse.doclet.DefaultQDoxCapableMetadataProvider;
import com.beust.doclipse.preferences.template.TemplateElement;

public abstract class AbstractPluginBuilder {
	public QDoxPlugin build(DoclipseProject doclipseProject, TemplateElement java,TemplateElement importTemplete,ClassManager classManager){
		String importTempleteName=importTemplete.getText();
		TemplateElement outputElement = importTemplete.getChildrenByKind(TemplateElement.CPE_EXPORT);
		if (outputElement == null) {
			throw new RuntimeException("no out put dir");
		}

		int lastsp=outputElement.getText().lastIndexOf(File.separatorChar);
		String outdir=outputElement.getText().substring(0, lastsp+1);
		String outFile=outputElement.getText().substring(lastsp+1);
		MessageConsoleStream message=ConsoleFactory.getMessageConsole().newMessageStream();
		message.println("output:\n"+outputElement.getText());
		FileWriterMapper fileWriterMapper=new FileWriterMapper(outdir,outFile);
		PropertiesQDoxPropertyExpander expander = new PropertiesQDoxPropertyExpander();
		Properties props = new Properties();
//		props.setProperty("value", "props-test-value");
		expander.addProperties("props", props);
//		QDoxMetadataProvider metadataProvider = new QDoxMetadataProvider(doclipseProject.getProject().getFile(java.getText()).getLocation().toFile(), expander);
		DefaultQDoxCapableMetadataProvider metadataProvider = new DefaultQDoxCapableMetadataProvider(doclipseProject.getProject().getFile(java.getText()).getLocation().toFile(), expander,classManager);
		return this.build(metadataProvider, fileWriterMapper,importTemplete.getText(),classManager);
	}
	public abstract QDoxPlugin build(QDoxCapableMetadataProvider metadataProvider,
			WriterMapper writerMapper,String temlate,ClassManager classManager);
}

package org.xdoclet.l2;

import java.io.File;
import java.util.Properties;

import org.xdoclet.QDoxMetadataProvider;
import org.xdoclet.tools.PropertiesQDoxPropertyExpander;

import com.beust.doclipse.builder.plugin.FileWriterMapper;
import com.beust.doclipse.builder.plugin.NormalQDoxPlugin;
import com.beust.doclipse.builder.plugin.PluginManager;
import com.beust.doclipse.builder.plugin.jelly.JellyTemplateEngine;
import com.beust.doclipse.preferences.template.TemplateElement;

public class Main {
	public static void main(String[] args) {
		File file=new File("D:/runtime-EclipseApplication/A/src/D.java");
		FileWriterMapper fileWriterMapper=new FileWriterMapper(null,null);
		PropertiesQDoxPropertyExpander expander = new PropertiesQDoxPropertyExpander();
		Properties props = new Properties();
		props.setProperty("value", "props-test-value");
		expander.addProperties("props", props);
		
		QDoxMetadataProvider metadataProvider = new QDoxMetadataProvider(new File("D:/eclipseplugin/doclipse/test/org/xdoclet/l1/QDoxPropertyExpanderDummyPlugin.jelly"), expander);
		NormalQDoxPlugin qp=new NormalQDoxPlugin(new JellyTemplateEngine(file), metadataProvider, fileWriterMapper);
		qp.start();
	}
}

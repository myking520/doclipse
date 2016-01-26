package org.xdoclet.l1;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import org.generama.JellyTemplateEngine;
import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.FileWriterMapper;
import org.generama.defaults.QDoxPlugin;
import org.xdoclet.QDoxMetadataProvider;
import org.xdoclet.tools.PropertiesQDoxPropertyExpander;


public class Main {
	public static void main(String[] args) {
		Main m=new Main();
		m.excute();
	}
	public void excute(){
		QDoxPlugin pl=new QDoxPropertyExpanderDummyPlugin(new JellyTemplateEngine(), this.getQDoxMetadataProvider(), new FileWriterMapper());
		pl.setMultioutput(true);
		pl.setMergedir("D:/eclipseplugin/doclipse/test/org/xdoclet/l1/classes1");
		pl.start();
	}
	private QDoxMetadataProvider getQDoxMetadataProvider(){
      PropertiesQDoxPropertyExpander expander = new PropertiesQDoxPropertyExpander();
      Properties props = new Properties();
      props.setProperty("value", "props-test-value");
      expander.addProperties("props", props);
      URL reader = getResourceRelativeToThisPackage("classes");
      QDoxMetadataProvider qp=  new QDoxMetadataProvider(new File("test/org/xdoclet/l1/classes/Dummy.java"), expander);
      return qp;
	}

	protected URL getResourceRelativeToThisPackage(String resourceName) {
		String className = Main.class.getName();
		String packageName = className.substring(0, className.lastIndexOf('.'));
		String resourcePath = "/" + packageName.replace('.', '/') + "/" + resourceName;
		URL resource = Main.class.getResource(resourcePath);
		return resource;
	}

}
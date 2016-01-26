package com.beust.doclipse.builder.plugin.jelly;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.XMLOutput;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXContentHandler;
import org.dom4j.io.XMLWriter;
import org.generama.GeneramaException;
import org.generama.TemplateEngine;
import org.generama.jelly.GeneramaJellyContext;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JellyTemplateEngine implements TemplateEngine {
	private File temlate;
	public JellyTemplateEngine(File temlate) {
		super();
		this.temlate = temlate;
	}
	@Override
	public void generate(Writer out, Map contextObjects, String encoding, Class pluginClass) throws GeneramaException {
		 JellyContext context = new GeneramaJellyContext();
	        Set keys = contextObjects.keySet();
	        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
	            String key = (String) iterator.next();
	            Object value = contextObjects.get(key);
	            context.setVariable(key, value);
	        }

	        OutputFormat format = OutputFormat.createPrettyPrint();
	        format.setEncoding(encoding);
	        final XMLWriter xmlWriter = new XMLWriter(out, format);
	        xmlWriter.setEscapeText(false);
	        // Create the output
	        XMLOutput xmlOutput = new XMLOutput();

	        SAXContentHandler saxHandler = new SAXContentHandler();

	        xmlOutput.setContentHandler(saxHandler);
	        xmlOutput.setLexicalHandler(saxHandler);

	        try {
	            xmlOutput.startDocument();
	            context.runScript(temlate, xmlOutput);
	            xmlOutput.endDocument();
	            xmlWriter.write(saxHandler.getDocument());
	            xmlWriter.flush();
	            xmlWriter.close();
	        } catch (SAXException e) {
	            throw new GeneramaException("Exception occurred when running Jelly", e);
	        } catch (JellyException e) {
	            throw new GeneramaException("Exception occurred when running Jelly", e);
	        } catch (Exception e) {
	            throw new GeneramaException("Exception occurred when running Jelly", e);
	        }
	}

}

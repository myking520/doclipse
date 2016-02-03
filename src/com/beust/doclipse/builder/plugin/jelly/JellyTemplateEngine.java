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

import com.beust.doclipse.builder.ClassManager;
import com.beust.doclipse.builder.plugin.AbstractTemplateEngine;

import java.io.File;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author myking520
 *
 */
public class JellyTemplateEngine extends AbstractTemplateEngine {
	public JellyTemplateEngine(File temlate,ClassManager classManager) {
		super(temlate,classManager);
	}

	@Override
	public void generate(Writer out, Map contextObjects) {
		JellyContext context = new GeneramaJellyContext();
		Set keys = contextObjects.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Object value = contextObjects.get(key);
			context.setVariable(key, value);
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		final XMLWriter xmlWriter = new XMLWriter(out, format);
		xmlWriter.setEscapeText(false);
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

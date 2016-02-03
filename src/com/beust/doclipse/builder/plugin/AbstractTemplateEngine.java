package com.beust.doclipse.builder.plugin;

import java.io.File;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.eclipse.ui.console.MessageConsoleStream;
import org.generama.GeneramaException;
import org.generama.TemplateEngine;

import com.beust.doclipse.console.ConsoleFactory;

public abstract class AbstractTemplateEngine implements TemplateEngine {
	protected File temlate;
	public AbstractTemplateEngine(File temlate) {
		super();
		this.temlate = temlate;
	}
	@Override
	public void generate(Writer out, Map contextObjects, String encoding, Class pluginClass) throws GeneramaException {
		Collection collection=(Collection) contextObjects.get("metadata");
		contextObjects.put("javaClass", collection.iterator().next());
		try {
			this.generate(out, contextObjects);
		} catch (Exception e) {
			MessageConsoleStream message=ConsoleFactory.getMessageConsole().newMessageStream();
			PrintStream ps=new PrintStream(message);
			e.printStackTrace(ps);
		}
	}
	public abstract void generate(Writer out, Map contextObjects);

}

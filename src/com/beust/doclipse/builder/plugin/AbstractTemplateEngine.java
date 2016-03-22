package com.beust.doclipse.builder.plugin;

import java.io.File;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.eclipse.ui.console.MessageConsoleStream;
import org.generama.GeneramaException;
import org.generama.TemplateEngine;

import com.beust.doclipse.builder.ClassManager;
import com.beust.doclipse.console.ConsoleFactory;

public abstract class AbstractTemplateEngine implements TemplateEngine {
	protected File temlate;
	protected ClassManager classManager;
	public AbstractTemplateEngine(File temlate,ClassManager classManager) {
		super();
		this.temlate = temlate;
		this.classManager=classManager;
	}
	@Override
	public void generate(Writer out, Map contextObjects, String encoding, Class pluginClass) throws GeneramaException {
		contextObjects.put("project", classManager);
		contextObjects.put("javaClass",  contextObjects.remove("metadata"));
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

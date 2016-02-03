package com.beust.doclipse.builder.plugin;

import java.io.File;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.generama.GeneramaException;
import org.generama.TemplateEngine;

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
		this.generate(out, contextObjects);
	}
	public abstract void generate(Writer out, Map contextObjects);

}

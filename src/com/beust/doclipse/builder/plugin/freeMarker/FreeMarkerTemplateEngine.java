package com.beust.doclipse.builder.plugin.freeMarker;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import org.generama.GeneramaException;
import org.generama.TemplateEngine;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTemplateEngine implements TemplateEngine {
	private File temlate;
	private Configuration cfg = new Configuration();

	public FreeMarkerTemplateEngine(File temlate) {
		super();
		this.temlate = temlate;
	}

	@Override
	public void generate(Writer out, Map contextObjects, String encoding, Class pluginClass) throws GeneramaException {
		try {
			cfg.setDirectoryForTemplateLoading(temlate.getParentFile());
			Template template = cfg.getTemplate(temlate.getName());
			template.process(contextObjects, out);
			out.flush();
		} catch (Exception e) {
			throw new GeneramaException("Exception occurred when running FreeMarker", e);
		}

	}

}

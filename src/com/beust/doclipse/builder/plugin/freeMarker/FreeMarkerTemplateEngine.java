package com.beust.doclipse.builder.plugin.freeMarker;

import java.io.File;
import java.io.Writer;
import java.util.Map;

import org.generama.GeneramaException;

import com.beust.doclipse.builder.ClassManager;
import com.beust.doclipse.builder.plugin.AbstractTemplateEngine;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTemplateEngine  extends AbstractTemplateEngine {
	private Configuration cfg = new Configuration();

	public FreeMarkerTemplateEngine(File temlate,ClassManager classManager) {
		super(temlate, classManager);
	}


	@Override
	public void generate(Writer out, Map contextObjects) {
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

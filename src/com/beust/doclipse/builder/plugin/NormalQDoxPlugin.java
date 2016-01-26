package com.beust.doclipse.builder.plugin;

import org.generama.QDoxCapableMetadataProvider;
import org.generama.TemplateEngine;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;

public class NormalQDoxPlugin extends QDoxPlugin {

	public NormalQDoxPlugin(TemplateEngine templateEngine, QDoxCapableMetadataProvider metadataProvider,
			WriterMapper writerMapper) {
		super(templateEngine, metadataProvider, writerMapper);
	}

}

package com.beust.doclipse.builder.plugin.freeMarker;

import java.io.File;

import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;

import com.beust.doclipse.builder.ClassManager;
import com.beust.doclipse.builder.plugin.AbstractPluginBuilder;
import com.beust.doclipse.builder.plugin.NormalQDoxPlugin;

public class FreeMarkerBuilder extends AbstractPluginBuilder {

	@Override
	public QDoxPlugin build(QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper, String temlate,ClassManager classManager) {
		File file=new File(temlate);
		return new NormalQDoxPlugin(new FreeMarkerTemplateEngine(file,classManager), metadataProvider, writerMapper);
	}

}

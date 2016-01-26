package com.beust.doclipse.builder.plugin.jelly;

import java.io.File;

import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;

import com.beust.doclipse.builder.plugin.AbstractPluginBuilder;
import com.beust.doclipse.builder.plugin.NormalQDoxPlugin;

public class JellyPluginBuilder extends AbstractPluginBuilder {
	@Override
	public QDoxPlugin build(QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper) {
		File file=new FIle("D:/runtime-EclipseApplication/A/src/D.java");
		return new NormalQDoxPlugin(new JellyTemplateEngine(file), metadataProvider, writerMapper);
	}

}

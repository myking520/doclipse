package com.beust.doclipse.builder.plugin.jelly;

import java.io.File;

import org.generama.QDoxCapableMetadataProvider;
import org.generama.WriterMapper;
import org.generama.defaults.QDoxPlugin;

import com.beust.doclipse.builder.plugin.AbstractPluginBuilder;
import com.beust.doclipse.builder.plugin.NormalQDoxPlugin;

/**
 * @author myking520
 *
 */
public class JellyPluginBuilder extends AbstractPluginBuilder {
	@Override
	public QDoxPlugin build(QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper,String temlate) {
		File file=new File(temlate);
		return new NormalQDoxPlugin(new JellyTemplateEngine(file), metadataProvider, writerMapper);
	}

}

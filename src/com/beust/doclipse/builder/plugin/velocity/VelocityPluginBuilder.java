package com.beust.doclipse.builder.plugin.velocity;

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
public class VelocityPluginBuilder extends AbstractPluginBuilder {
	@Override
	public QDoxPlugin build(QDoxCapableMetadataProvider metadataProvider, WriterMapper writerMapper,String temlate) {
		File file=new File(temlate);
		return new NormalQDoxPlugin(new VelocityTemplateEngine(file), metadataProvider, writerMapper);
	}

}

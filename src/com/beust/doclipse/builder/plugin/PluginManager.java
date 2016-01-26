package com.beust.doclipse.builder.plugin;

import java.util.HashMap;
import java.util.Map;

import org.generama.defaults.QDoxPlugin;

import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.builder.plugin.jelly.JellyPluginBuilder;
import com.beust.doclipse.preferences.template.TemplateElement;

public class PluginManager {
	private final static Map<String,AbstractPluginBuilder> plugins=new HashMap<String,AbstractPluginBuilder>();
	static{
		plugins.put(TemplateElement.ENGINE_JELLY, new JellyPluginBuilder());
		System.setProperty("file.encoding", "UTF-8");
	}
	public final static QDoxPlugin getQDoxPlugin(DoclipseProject doclipseProject, TemplateElement element){
		TemplateElement engine=element.getChildrenByKind(TemplateElement.CPE_ENGINE);
		AbstractPluginBuilder builder=plugins.get(engine.getText());
		return builder.build(doclipseProject, element);
	}
}

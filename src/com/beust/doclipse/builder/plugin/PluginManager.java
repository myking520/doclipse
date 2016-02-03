package com.beust.doclipse.builder.plugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.console.MessageConsoleStream;

import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.builder.ClassManager;
import com.beust.doclipse.builder.plugin.freeMarker.FreeMarkerBuilder;
import com.beust.doclipse.builder.plugin.jelly.JellyPluginBuilder;
import com.beust.doclipse.builder.plugin.velocity.VelocityPluginBuilder;
import com.beust.doclipse.console.ConsoleFactory;
import com.beust.doclipse.preferences.template.TemplateElement;

/**
 * @author myking520
 *
 */
public class PluginManager {
	private final static Map<String,AbstractPluginBuilder> plugins=new HashMap<String,AbstractPluginBuilder>();
	static{
		plugins.put(TemplateElement.ENGINE_JELLY, new JellyPluginBuilder());
		plugins.put(TemplateElement.ENGINE_FREEMARKER, new FreeMarkerBuilder());
		plugins.put(TemplateElement.ENGINE_VELOCITY, new VelocityPluginBuilder());
		System.setProperty("file.encoding", "UTF-8");
	}
	public final static void process(DoclipseProject doclipseProject, TemplateElement element,ClassManager classManager){
		MessageConsoleStream message=ConsoleFactory.getMessageConsole().newMessageStream();
		message.println("javaFile:\n"+element.getText());
		for(int i=0;i<element.getChildren().size();i++){
			TemplateElement importTemplete=	element.getChildren().get(i);
			TemplateElement engine=importTemplete.getChildrenByKind(TemplateElement.CPE_ENGINE);
			AbstractPluginBuilder builder=plugins.get(engine.getText());
			message.println("engine:\n"+engine.getText());
			message.println("Templete:\n"+importTemplete.getText());
			classManager.add2bBuild(builder.build(doclipseProject, element,importTemplete,classManager));
		}
	}
}

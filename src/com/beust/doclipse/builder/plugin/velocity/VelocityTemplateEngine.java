package com.beust.doclipse.builder.plugin.velocity;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.generama.GeneramaException;
import org.generama.TemplateEngine;

/**
 * @author myking520
 *
 */
public class VelocityTemplateEngine implements TemplateEngine {
	private File temlate;
	public VelocityTemplateEngine(File temlate) {
		super();
		this.temlate = temlate;
	}
	@Override
	public void generate(Writer out, Map contextObjects, String encoding, Class pluginClass) throws GeneramaException {
		VelocityEngine ve = new VelocityEngine();  
		  Properties properties = new Properties();  
		  properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, temlate.getParentFile().getAbsolutePath());  
      try {  
          ve.init(properties);  
        
          Template t = ve.getTemplate(temlate.getName(), "UTF-8");  
          VelocityContext context = new VelocityContext();  
          t.merge(context, out);  
          out.flush();  
      } catch (Exception e) {  
          throw new RuntimeException("模版转化错误!",e);  
      }  
	}

}

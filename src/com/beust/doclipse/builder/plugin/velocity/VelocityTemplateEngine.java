package com.beust.doclipse.builder.plugin.velocity;

import java.io.File;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.beust.doclipse.builder.plugin.AbstractTemplateEngine;

/**
 * @author myking520
 *
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine {
	public VelocityTemplateEngine(File temlate) {
		super(temlate);
	}

	@Override
	public void generate(Writer out, Map contextObjects) {
		VelocityEngine ve = new VelocityEngine();
		Properties properties = new Properties();
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, temlate.getParentFile().getAbsolutePath());
		try {
			ve.init(properties);
			Template t = ve.getTemplate(temlate.getName(), "UTF-8");
			VelocityContext context = new VelocityContext();
			Set keys = contextObjects.keySet();
			for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				Object value = contextObjects.get(key);
				context.put(key, value);
			}
			t.merge(context, out);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException("模版转化错误!", e);
		}
	}

}

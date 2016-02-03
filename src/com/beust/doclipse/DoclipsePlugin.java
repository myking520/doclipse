package com.beust.doclipse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.beust.doclipse.tag.Tag;

/**
 * @ The main plugin class to be used in the desktop.
 */
public class DoclipsePlugin extends AbstractUIPlugin {
	public static Map<IProject, DoclipseProject> projects = new HashMap<IProject, DoclipseProject>();
	// The shared instance.
	private static DoclipsePlugin plugin;
	// Resource bundle.
	private ResourceBundle resourceBundle;
	/**
	 * The constructor.
	 */
	public DoclipsePlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("com.beust.doclipse.DoclipsePluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}

	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static DoclipsePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = DoclipsePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}


	public static DoclipseProject getDoclipseProject(IProject project) {
		DoclipseProject doclipseProject = projects.get(project);
		if (doclipseProject == null) {
			doclipseProject = new DoclipseProject(project);
			projects.put(project, doclipseProject);
		}
		return doclipseProject;
	}

	public static DoclipseProject getDoclipseProject() {
		IProject project = DoclipseProject.getCurrentProject();
		return getDoclipseProject(project);
	}
	



}

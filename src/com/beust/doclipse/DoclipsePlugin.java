package com.beust.doclipse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.beust.doclipse.preferences.Preferences;
import com.beust.doclipse.tag.Tag;

/**
 * The main plugin class to be used in the desktop.
 */
public class DoclipsePlugin extends AbstractUIPlugin {
	/**
	*
	*/

	protected void initializeDefaultPreferences(IPreferenceStore store) {
		Preferences.initializeDefaultPreferences(store);
	}

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
		initTags();
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

	//////////////////

	static private Map m_externalTags = new HashMap();
	static private Map m_internalTags = new HashMap();
	static private Map m_allTags = new HashMap();
	public static  Map<IProject,DoclipseProject> projects=new HashMap<IProject,DoclipseProject>();
	private void initTags() {
		// Retrieve the external directory
		String directory = Preferences.getExternalDirectory();

		// Retrieve the external checked files from the IPreferenceStore
		Map extFiles = Preferences.getExternalCheckedFiles();
		DefinitionFile[] files = Utils.readDirectory(directory, extFiles);
		setExternalTags(parseTags(files));

		// Retrieve the internal checked files from the IPreferenceStore
		Map intFiles = Preferences.getInternalCheckedFiles();
		DefinitionFile[] internalFiles = Utils.readInternalFiles(intFiles);
		setInternalTags(parseTags(internalFiles));
		refreshAllTags();
	}
	private static void refreshAllTags() {
		m_allTags = new HashMap();
		putAll(m_allTags, m_internalTags);
		putAll(m_allTags, m_externalTags);
	}

	private static void putAll(Map to, Map from) {
		to.putAll(from);
	}

	static public Map getTags() {
		IProject project=DoclipseProject.getCurrentProject();
		DoclipseProject doclipseProject=projects.get(project);
		if(doclipseProject==null){
			doclipseProject=new DoclipseProject(project);
			projects.put(project, doclipseProject);
		}
		return m_allTags;//doclipseProject.getAllTags();
	}

	static public void setExternalTags(Map tags) {
		m_externalTags = tags;
		refreshAllTags();
	}

	static private void setInternalTags(Map tags) {
		putAll(m_internalTags, tags);
	}

	static public Tag getTag(String tagName) {
		return (Tag) m_allTags.get(tagName);
	}

	/**
	 * @return
	 */
	public static Map parseTags(DefinitionFile[] files) {
		Map result = new HashMap();
		for (int i = 0; i < files.length; i++) {
			Map tags = files[i].getTags();
			for (Iterator it = tags.keySet().iterator(); it.hasNext();) {
				String name = (String) it.next();
				Tag tag = (Tag) tags.get(name);
				result.put(name, tag);
			}
		}
		return result;
	}
	/**
	 * @param definitionFiles
	 */
	public static void updateExternalTags(DefinitionFile[] definitionFiles) {
		m_externalTags = parseTags(definitionFiles);
		refreshAllTags();
	}
	public static void updateInternalTags(DefinitionFile[] definitionFiles) {
		setInternalTags(parseTags(definitionFiles));
		refreshAllTags();
	}
}
package com.beust.doclipse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.internal.resources.ProjectPreferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.beust.doclipse.tag.Tag;

public class DoclipseProject {

	// Where the external files are stored
	private static final String EXTERNAL_DIRECTORY = "PreferenceDirectory";
	// Where the checked internal directories are stored
	private static final String INTERNAL_CHECKED_FILES = "InternalCheckedFiles";
	// Where the checked external directories are stored
	private static final String EXTERNAL_CHECKED_FILES = "ExternalCheckedFiles";
	// Whether we surround values with tdouble quotes
	private static final String SURROUND_WITH_DOUBLE_QUOTES = "SurroundWithDoubleQuotes";
	// Whether we put spaces around equal signes
	private static final String SPACES_AROUND_EQUAL_SIGNS = "SpacesAroundEqualSigns";

	private static final String ALL = "<all>";
	private static final String hasinit = "init";
	private Map externalTags = new HashMap();
	private Map internalTags = new HashMap();
	private Map allTags = new HashMap();
	private IProject project;
	private Preferences preferences;

	public Preferences getPreferences() {
		return preferences;
	}

	public DoclipseProject(IProject project) {
		super();
		this.project = project;
		this.initTags();
	}

	/**
	 * @return 当前项目
	 */
	public static IProject getCurrentProject() {
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		IProject project = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();

			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			} else if (element instanceof PackageFragmentRootContainer) {
				IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
				project = jProject.getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject = ((IJavaElement) element).getJavaProject();
				project = jProject.getProject();
			}
		}
		if (project == null) {
			project = getProjectByEditorInput();
		}
		return project;
	}

	/**
	 * @return 根据当前编辑器获得工程
	 */
	private static IProject getProjectByEditorInput() {
		IEditorInput input = Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
				.getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			return file.getProject();
		}
		return null;
	}

	public Map getAllTags() {
		return allTags;
	}

	private void initTags() {
		try {
			this.loadProjectPreferences();
			String directory = this.preferences.get(EXTERNAL_DIRECTORY, "");
			Map extFiles = getCheckedFiles(EXTERNAL_CHECKED_FILES);
			DefinitionFile[] files = Utils.readDirectory(directory, extFiles);
			externalTags = parseTags(files);
			Map intFiles = getCheckedFiles(INTERNAL_CHECKED_FILES);
			DefinitionFile[] internalFiles = Utils.readInternalFiles(intFiles);
			internalTags = (parseTags(internalFiles));
			refreshAllTags();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initProjectPreferences() {
		String checkedFiles = "";
//		String checkedFiles = "ejbgen.xml hibernate.xml";
		String[] checkedFilesArray = { };
//		String[] checkedFilesArray = { "ejbgen.xml", "hibernate.xml", };
		this.preferences.put(SPACES_AROUND_EQUAL_SIGNS, "True");
		this.preferences.put(SURROUND_WITH_DOUBLE_QUOTES, "True");
		this.preferences.put(INTERNAL_CHECKED_FILES, checkedFiles);
		Map internalFiles = new HashMap();
		for (int i = 0; i < checkedFilesArray.length; i++) {
			internalFiles.put(checkedFilesArray[i], checkedFilesArray[i]);
		}
		saveCheckedFiles(internalFiles, INTERNAL_CHECKED_FILES);
		saveCheckedFiles(null, EXTERNAL_CHECKED_FILES);
	}

	private void saveCheckedFiles(Map files, String key) {
		StringBuffer sb = new StringBuffer();

		if (null != files) {
			for (Iterator it = files.values().iterator(); it.hasNext();) {
				sb.append(it.next()).append(" ");
			}
		} else {
			sb.append(ALL);
		}
		this.preferences.put(key, sb.toString());
	}

	private void refreshAllTags() {
		Map allTags = new HashMap();
		allTags.putAll(externalTags);
		allTags.putAll(internalTags);
		this.allTags = allTags;
	}

	private void loadProjectPreferences() {
		IEclipsePreferences eclipsePreferences = Platform.getPreferencesService().getRootNode();
		ProjectPreferences preferences = (ProjectPreferences) eclipsePreferences.node(ProjectScope.SCOPE);
		preferences = (ProjectPreferences) preferences.node(project.getName());
		try {
			if (!preferences.nodeExists(DoclipsePlugin.class.getName())) {
				this.preferences = (ProjectPreferences) preferences.node(DoclipsePlugin.class.getName());
				this.initProjectPreferences();
				this.preferences.flush();
			} else {
				this.preferences = (ProjectPreferences) preferences.node(DoclipsePlugin.class.getName());
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	private Map parseTags(DefinitionFile[] files) {
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

	private Map getCheckedFiles(String key) {
		String strResult = (String) preferences.get(key, null);
		Map result = null;
		if (!ALL.equals(strResult)) {
			result = new HashMap();
			StringTokenizer st = new StringTokenizer(strResult);
			while (st.hasMoreElements()) {
				String f = st.nextToken();
				result.put(f, f);
			}
		}

		return result;
	}

	public boolean surroundWithDoubleQuotes() {
		boolean result = this.preferences.getBoolean(SURROUND_WITH_DOUBLE_QUOTES, false);
		return result;
	}

	public boolean insertSpacesAroundEqual() {
		boolean result = this.preferences.getBoolean(SPACES_AROUND_EQUAL_SIGNS, false);
		return result;
	}

	public void saveSurroundWithDoubleQuotes(boolean f) {
		preferences.putBoolean(SURROUND_WITH_DOUBLE_QUOTES, f);
	}

	public void saveInsertSpacesAroundEqual(boolean f) {
		preferences.putBoolean(SPACES_AROUND_EQUAL_SIGNS, f);
	}

	public void updateInternalTags(DefinitionFile[] definitionFiles) {
		setInternalTags(parseTags(definitionFiles));
		refreshAllTags();
	}

	public void setInternalTags(Map tags) {
		internalTags=new HashMap();
		internalTags.putAll(tags);
	}

	public void saveInternalCheckedFiles(Map files) {
		saveCheckedFiles(files, INTERNAL_CHECKED_FILES);
	}

	public Map getInternalCheckedFiles() {
		return getCheckedFiles(INTERNAL_CHECKED_FILES);
	}

	public void saveExternalCheckedFiles(Map files) {
		saveCheckedFiles(files, EXTERNAL_CHECKED_FILES);
	}

	public void saveExternalDirectory(String dir) {
		preferences.put(EXTERNAL_DIRECTORY, dir);
	}

	public String getExternalDirectory() {
		return preferences.get(EXTERNAL_DIRECTORY, "");
	}

	public Map getExternalCheckedFiles() {
		return getCheckedFiles(EXTERNAL_CHECKED_FILES);
	}

	public Tag getTag(String name) {
		return (Tag) this.allTags.get(name);
	}

	/**
	 * @param definitionFiles
	 */
	public  void updateExternalTags(DefinitionFile[] definitionFiles) {
		externalTags = parseTags(definitionFiles);
		refreshAllTags();
	}
	

}

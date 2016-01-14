package com.beust.doclipse.preferences;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.beust.doclipse.DefinitionFile;
import com.beust.doclipse.DoclipsePlugin;
import com.beust.doclipse.Preferences;
import com.beust.doclipse.Utils;


/**
 * Preference page for the plug-in.
 *
 * @author Cedric Beust, Jun 11, 2004
 * 
 */
public class InternalFilesPage 
  extends DoclipsePreferencePage 
  implements IWorkbenchPreferencePage 
{
  private Table m_internalTable = null;
  private DefinitionFile[] m_internalDefinitionFiles = null;
  // What files were stored in the preferences
  private Map m_storedInternalFiles = null;

  protected Control createContents(Composite parent) {
//    Composite result = parent;
    Composite result = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    result.setLayout(gl);
    
    //
    // Internal group
    //
    {
//      Group group = createGroup(result, "Internal files");
      m_internalTable = createTable(result, 0);
      initTable(m_internalTable);
    }

    initializeValues();
    
    return result;
  }

  public void init(IWorkbench workbench) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
public void statusChanged(IStatus status) {
	// TODO Auto-generated method stub
	
}

private void refreshInternalTable(Map checkedFiles) {
    m_internalDefinitionFiles = Utils.readInternalFiles(null);
    refreshTable(m_internalTable, m_internalDefinitionFiles, checkedFiles);
  }
  
  private void refreshInternalDirectory() {
    //
    // Tell the plug-in to reread the internal files
    //
    Map fileNames = getCheckedFiles(m_internalTable);
    m_internalDefinitionFiles = Utils.readInternalFiles(fileNames);
    DoclipsePlugin.updateInternalTags(m_internalDefinitionFiles);    

    //
    // Save the current values to the IPreferenceStore
    //
    Preferences.saveInternalCheckedFiles(fileNames);
  }  

  private void initializeValues() {
    String dir = null;
    
    if (isPlugin()) {
      m_storedInternalFiles = Preferences.getInternalCheckedFiles(); 
      refreshInternalTable(m_storedInternalFiles);
    }
    
  }
  
  protected void refreshDirectories() {
    refreshInternalDirectory();
  }

  protected void performApply() {
    refreshDirectories();
  }
  
  public boolean performOk() {
    refreshDirectories();
    return true;
  }
  
  public static void main(String[] argv) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout( new RowLayout());

    Control c = new InternalFilesPage().createContents(shell);
    
    shell.pack();
    shell.open();
    while( !shell.isDisposed())
    {
      if(!display.readAndDispatch()) 
      display.sleep();
    }
    display.dispose();
  }  
}

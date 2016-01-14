package com.beust.doclipse.preferences;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
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
public class ExternalFilesPage 
  extends DoclipsePreferencePage 
  implements IWorkbenchPreferencePage 
{
  private Table m_externalTable = null;
  private DefinitionFile[] m_externalDefinitionFiles = null;
  private Map m_storedExternalFiles = null;  
  private Text m_directoryTF = null;
  
  protected Control createContents(Composite parent) {
//    Composite result = parent;
  Composite result = new Composite(parent, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    result.setLayout(gl);
    {
//      Group group = createGroup(result, "External files");
      Composite group = result;
      GridData gd = new GridData(GridData.FILL_BOTH);
      gd.grabExcessVerticalSpace = true;
      group.setLayoutData(gd);

      m_directoryTF = createButtonTextBrowse(group);
      Control reread = createRereadDirectory(group);
      
      m_externalTable = createTable(group, 0);
      initTable(m_externalTable);
    }
    
    initializeValues();
    
    return result;
  }

  public void init(IWorkbench workbench) {
    // TODO Auto-generated method stub
    
  }
  
  protected void refreshDirectories() {
    refreshExternalDirectory();
  }

  private void setDirectory(String directory) {
    m_directoryTF.setText(directory);
//    m_directoryTF.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
    refreshExternalTable(m_storedExternalFiles);
  }

  protected Text createButtonTextBrowse(Composite parent) {
    Text result = null;
    final Composite container = new Composite(parent, SWT.NULL);
    GridLayout gl = new GridLayout(3, false);
    container.setLayout(gl);
    {
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.verticalAlignment = GridData.CENTER;
      gd.grabExcessHorizontalSpace = true;
      container.setLayoutData(gd);
    }

    {
      Label l = new Label(container, SWT.NONE);
      l.setText("Directory:");
      GridData gd = new GridData();
      gd.verticalAlignment = GridData.BEGINNING;
      l.setLayoutData(gd);
    }
    
    {
      result = new Text(container, SWT.BORDER);
      GridData gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.grabExcessHorizontalSpace = true;
      result.setLayoutData(gd);
    }
    
    {
      Button b = new Button(container, SWT.PUSH);
//      GridData gd = new GridData(GridData.FILL_BOTH);
//      gd.horizontalAlignment = GridData.END;
//      b.setLayoutData(gd);
      b.setText("Browse...");
      b.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event event) {
          DirectoryDialog fd = new DirectoryDialog(container.getShell());
          String file = fd.open();
          if (null != file) setDirectory(file);
        }
      });
    }
    
    return result;
  }  

  private void refreshExternalTable(Map checkedFiles) {
    m_externalDefinitionFiles = 
      Utils.readDirectory(m_directoryTF.getText(), null);
    refreshTable(m_externalTable, m_externalDefinitionFiles, checkedFiles);
  }

  private void initializeValues() {
    String dir = null;
    
    if (isPlugin()) {
      m_directoryTF.setText(Preferences.getExternalDirectory());
      m_storedExternalFiles = Preferences.getExternalCheckedFiles(); 
      refreshExternalTable(m_storedExternalFiles);
    }
    else {
      m_directoryTF.setText("d:/t/doclipse");
    }
    
  }
  
  protected void refreshExternalDirectory() {
    //
    // Tell the plug-in to reread the external files
    //
    Map fileNames = getCheckedFiles(m_externalTable);
    m_externalDefinitionFiles = Utils.readDirectory(m_directoryTF.getText(), fileNames);
    DoclipsePlugin.updateExternalTags(m_externalDefinitionFiles);    

    //
    // Save the current values to the IPreferenceStore
    //
    Preferences.saveExternalCheckedFiles(fileNames);
    Preferences.saveExternalDirectory(m_directoryTF.getText());
  }
  
  private Control createRereadDirectory(Composite parent) {    
    Button result = new Button(parent, SWT.PUSH);
    GridData gd = new GridData();
    gd.horizontalAlignment = GridData.CENTER;
    result.setLayoutData(gd);
    result.setText("Reread this directory");
    result.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        refreshExternalTable(getCheckedFiles(m_externalTable));
      }
    });  
    
    return result;
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

    Control c = new ExternalFilesPage().createContents(shell);
    
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

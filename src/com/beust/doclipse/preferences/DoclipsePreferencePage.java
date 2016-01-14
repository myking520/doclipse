package com.beust.doclipse.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.wizards.IStatusChangeListener;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.PropertyPage;

import com.beust.doclipse.DefinitionFile;
import com.beust.doclipse.DoclipsePlugin;


/**
 * This class
 *
 * @author Cedric Beust, Jul 19, 2004
 * 
 */
abstract public class DoclipsePreferencePage extends PropertyPage implements IStatusChangeListener {
  protected Group createGroup(Composite parent, String title) {
    Group result = new Group(parent, SWT.SHADOW_ETCHED_IN);
    result.setText(title);
    GridLayout gd1 = new GridLayout(1, true);
    GridData ld = new GridData(GridData.FILL_BOTH);
    ld.grabExcessVerticalSpace = true;
    ld.grabExcessHorizontalSpace = true;
    result.setLayout(gd1);
    result.setLayoutData(ld);
    return result;
  }
  
  @Override
public void statusChanged(IStatus status) {
	  setValid(!status.matches(IStatus.ERROR));
		StatusUtil.applyToStatusLine(this, status);
	
}

protected void initTable(Table table) {
    TableColumn tc = new TableColumn(table, SWT.NONE);
    tc.setText("File name");
    tc.setWidth(100);
    tc = new TableColumn(table, SWT.NONE);
    tc.setText("Description");
    tc.setWidth(250);
    table.setHeaderVisible(true);
    table.setLinesVisible(true);    
  }
    
  protected Table createTable(Composite group, int heightHint) {
    final Table result;
    Composite parent = new Composite(group, SWT.NONE);
    {
      GridLayout layout = new GridLayout(2, false);
      parent.setLayout(layout);
      GridData gd = new GridData(GridData.FILL_BOTH);
      gd.grabExcessVerticalSpace = true;
      parent.setLayoutData(gd);
    }
    
    // Table and buttons
    {
      result = new Table(parent, SWT.MULTI | SWT.CHECK | SWT.H_SCROLL | SWT.V_SCROLL);
//      TableViewer tv = new TableViewer(result);
      GridData gd = new GridData(GridData.FILL_BOTH);
//      gd.grabExcessVerticalSpace = true;
      if (0 != heightHint) gd.heightHint = heightHint;
      result.setLayoutData(gd);
    }
    
    // Buttons on the side
    {
      Composite c = new Composite(parent, SWT.NONE);
      GridData gd = new GridData();
      gd.verticalAlignment = GridData.BEGINNING;
      c.setLayoutData(gd);

      RowData data = new RowData();
      RowLayout layout = new RowLayout(SWT.VERTICAL);
      layout.marginTop = 0;
      layout.pack = false;
      layout.justify = false;
      c.setLayout(layout);
      Button selectAllB = new Button(c, SWT.PUSH);
      selectAllB.setText("Select all");
      selectAllB.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event event) {
          selectTable(result, true);
        }
      });
      Button deselectAllB = new Button(c, SWT.PUSH);
      deselectAllB.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event event) {
          selectTable(result, false);
        }
      });
      deselectAllB.setText("Deselect all");
//      Button viewFileB = new Button(c, SWT.PUSH);
//      viewFileB.setText("View file");
    }

    return result;
  }

  protected void selectTable(Table table, boolean select) {
    int n = table.getItemCount();
    for (int i = 0; i < n; i++) {
      TableItem ti = table.getItem(i);
      ti.setChecked(select);
    }
  }

  protected boolean isPlugin() {
    return null != DoclipsePlugin.getDefault();
  }

  protected void refreshTable(Table table, DefinitionFile[] files, Map checkedFiles) {
    table.removeAll();
    if (null != files) {
      for (int i = 0; i < files.length; i++) {
        DefinitionFile df = files[i];
        TableItem ti = new TableItem(table, SWT.NONE);
        ti.setText(new String[] { df.getFileName(), df.getDescription() });
        ti.setChecked(null == checkedFiles || null != checkedFiles.get(df.getFileName()));
      }
    }
  }

  protected Map getCheckedFiles(Table table) {
    Map result = new HashMap();
    
    int n= table.getItemCount();
    for (int i = 0; i < n; i++) {
      TableItem ti = table.getItem(i);
      if (ti.getChecked()) {
        String fileName = ti.getText(0);
        result.put(fileName, fileName);
      }
    }
    
    return result;
  }
  
  abstract protected void refreshDirectories();
  
}

package com.beust.doclipse.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.service.prefs.BackingStoreException;

import com.beust.doclipse.DoclipseProject;

/**
 * Preference page for the plug-in.
 *
 * @author Cedric Beust, Jun 11, 2004
 * 
 */
public class MainPage extends DoclipsePreferencePage implements IWorkbenchPreferencePage {
	private Button m_surroundWithDoubleQuotesB = null;
	private Button m_insertSpacesB = null;

	protected Control createContents(Composite parent) {
		super.createContents(parent);
		Composite result = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		result.setLayout(gl);
		m_surroundWithDoubleQuotesB = new Button(result, SWT.CHECK);
		m_surroundWithDoubleQuotesB.setText("Surround values with double quotes");
		m_insertSpacesB = new Button(result, SWT.CHECK);
		m_insertSpacesB.setText("Insert spaces around the equal sign");
		initializeValues();
		return result;
	}

	private void initializeValues() {
		if (isPlugin()) {
			m_surroundWithDoubleQuotesB.setSelection(doclipseProject.surroundWithDoubleQuotes());
			m_insertSpacesB.setSelection(doclipseProject.insertSpacesAroundEqual());
		}
	}

	private Label createLabel(Composite parent, String text) {
		Label result = new Label(parent, SWT.NONE);
		result.setText(text);

		return result;
	}

	private Button createPushButton(Composite parent, String text) {
		Button result = new Button(parent, SWT.PUSH);
		result.setText(text);

		return result;
	}

	private Text createTextField(Composite parent) {
		return new Text(parent, SWT.SINGLE);
	}

	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] argv) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout());

		Control c = new MainPage().createContents(shell);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void savePreferences() {
		//
		// Save the current values to the IPreferenceStore
		//
		doclipseProject.saveSurroundWithDoubleQuotes(m_surroundWithDoubleQuotesB.getSelection());
		doclipseProject.saveInsertSpacesAroundEqual(m_insertSpacesB.getSelection());
	}

	protected void performApply() {
		savePreferences();
	}

	public boolean performOk() {
		savePreferences();
		try {
			doclipseProject.getPreferences().flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	protected void refreshDirectories() {

	}

}

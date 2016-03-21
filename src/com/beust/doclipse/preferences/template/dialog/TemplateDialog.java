package com.beust.doclipse.preferences.template.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.beust.doclipse.preferences.template.TemplateElement;

public class TemplateDialog extends Dialog {
	private Text input;
	private Combo engine;
	private Text output;
	private TemplateElement javaElement;
	private TemplateElement engineElement;
	private final static String[] engines = new String[] { TemplateElement.ENGINE_JELLY, TemplateElement.ENGINE_FREEMARKER,TemplateElement.ENGINE_VELOCITY };

	public TemplateDialog(Shell parentShell, TemplateElement javaElement, TemplateElement engineElement) {
		super(parentShell);
		this.javaElement = javaElement;
		this.engineElement=engineElement;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite contents = (Composite) super.createDialogArea(parent);
		((GridLayout) contents.getLayout()).numColumns = 3;
		contents.setSize(500, 500);
		Label label = new Label(contents, SWT.NONE);
		label.setText("javaFile");
		Text text=new Text(contents, SWT.SINGLE | SWT.BORDER);
		text.setEnabled(false);
		text.setText(javaElement.getText());
		label = new Label(contents, SWT.NONE);
	
		label = new Label(contents, SWT.NONE);
		label.setText("template");
		input = new Text(contents, SWT.SINGLE | SWT.BORDER);
		input.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		if (engineElement != null) {
			input.setText(engineElement.getText());
		}
		input.setEnabled(false);
		Button btn = new Button(contents, SWT.NONE);
		btn.setText("browse");
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fdialog = new FileDialog(Display.getCurrent().getActiveShell());
				String file = fdialog.open();
				input.setText(file);
				input.setToolTipText(file);
			}
		});
		label = new Label(contents, SWT.NONE);
		label.setText("engine");
		engine = new Combo(contents, SWT.NONE);
		engine.setItems(engines);
		TemplateElement element = this.getChildren(TemplateElement.CPE_ENGINE);
		if (element != null) {
			engine.select(this.getEngine(element.getText()));
		}
		label = new Label(contents, SWT.NONE);
		label = new Label(contents, SWT.NONE);
		label.setText("output");
		output = new Text(contents, SWT.SINGLE | SWT.BORDER);
		output.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

		output.setEnabled(false);
		element = this.getChildren(TemplateElement.CPE_EXPORT);
		if (element != null) {
			output.setText(element.getText());
		}
		btn = new Button(contents, SWT.NONE);
		btn.setText("browse");
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fdialog = new FileDialog(Display.getCurrent().getActiveShell(),SWT.SAVE);
				String file = fdialog.open();
				output.setText(file);
				output.setToolTipText(file);
			}
		});
		return contents;
	}

	private int getEngine(String engine) {
		for (int i = 0; i < engines.length; i++) {
			if (engines[i].equals(engine)) {
				return i;
			}
		}
		return -1;
	}

	private TemplateElement getChildren(int kind) {
		if(engineElement==null){
			return null;
		}
		for (int i = 0; i < engineElement.getChildren().size(); i++) {
			TemplateElement telement = engineElement.getChildren().get(i);
			if (telement.getKind() == kind) {
				return telement;
			}
		}
		return null;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 375);
	}

	@Override
	protected void okPressed() {
		if (input.getText() == null || input.getText().length() == 0) {
			MessageDialog messageDialog = new MessageDialog(Display.getCurrent().getActiveShell(), "error!", null,
					"template error !", MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
			messageDialog.open();
			return;
		}
		if (engine.getText() == null || engine.getText().length() == 0) {
			MessageDialog messageDialog = new MessageDialog(Display.getCurrent().getActiveShell(), "error!", null,
					"engine error !", MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
			messageDialog.open();
			return;
		}
		if (output.getText() == null || output.getText().length() == 0) {
			MessageDialog messageDialog = new MessageDialog(Display.getCurrent().getActiveShell(), "error!", null,
					"output error !", MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
			messageDialog.open();
			return;
		}
		
		if (engineElement == null) {
			engineElement = new TemplateElement();
			engineElement.setKind(TemplateElement.CPE_IMPORT);
			javaElement.getChildren().add(engineElement);
		}
		engineElement.setText(input.getText());
		TemplateElement element = this.getChildren(TemplateElement.CPE_ENGINE);
		if (element == null) {
			element = new TemplateElement();
			element.setKind(TemplateElement.CPE_ENGINE);
			engineElement.getChildren().add(element);
		}
		element.setText(engine.getText());
		element = this.getChildren(TemplateElement.CPE_EXPORT);
		if (element == null) {
			element = new TemplateElement();
			element.setKind(TemplateElement.CPE_EXPORT);
			engineElement.getChildren().add(element);
		}
		element.setText(output.getText());
		super.okPressed();
	}

}

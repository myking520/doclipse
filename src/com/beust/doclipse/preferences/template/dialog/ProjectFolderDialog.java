package com.beust.doclipse.preferences.template.dialog;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
public class ProjectFolderDialog extends Dialog implements Listener {
	private ProjectAndContainerGroup resourceGroup;
	protected ProjectFolderDialog(Shell parentShell) {
		super(parentShell);
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite contents = (Composite) super.createDialogArea(parent);
		resourceGroup = new ProjectAndContainerGroup(parent, this, "fileName", "folder");
		return contents;
	}
	@Override
	public void handleEvent(Event event) {
		
	}
	IPath path=null;
	public IPath getPath(){
		return path;
	}
	@Override
	protected void okPressed() {
		if (!resourceGroup.areAllValuesValid()) {
			MessageDialog messageDialog = new MessageDialog(Display.getCurrent().getActiveShell(), "error!", null,
					resourceGroup.getProblemMessage(), MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
			messageDialog.open();
			return;
		}
		String resourceName = resourceGroup.getResource();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus result = workspace.validateName(resourceName, IResource.FILE);
		if (!result.isOK()) {
			MessageDialog messageDialog = new MessageDialog(Display.getCurrent().getActiveShell(), "error!", null,
					result.getMessage(), MessageDialog.ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);
			messageDialog.open();
			return;
		}
		this.path=resourceGroup.getContainerFullPath().append(resourceGroup.getResource());
		super.okPressed();
	}
	

}

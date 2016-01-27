package com.beust.doclipse.preferences.template;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

/**
 * @author myking520
 *
 */
public class ResourcesSelectionDialog extends FilteredResourcesSelectionDialog {

	public ResourcesSelectionDialog(Shell shell, boolean multi, IContainer container, int typesMask,ViewerFilter... viewerFilters) {
		super(shell, multi, container, typesMask);
		if(viewerFilters!=null){
			for(int i=0;i<viewerFilters.length;i++){
				super.addListFilter(viewerFilters[i]);
			}
		}
	}
	protected void fillViewMenu(IMenuManager menuManager) {
		super.fillViewMenu(menuManager);
		menuManager.removeAll();
		
	}
	
}

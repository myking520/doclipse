package com.beust.doclipse.preferences.template;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;

public class TemplateLabelProvider implements ILabelProvider{

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		
	}

	@Override
	public Image getImage(Object element) {
		if(element instanceof TemplateElement){
			TemplateElement e=(TemplateElement) element;
			if(e.getKind()==IClasspathEntry.CPE_PROJECT){
				return Workbench.getInstance().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
			}
		}
		return  null;
	}
	@Override
	public String getText(Object element) {
		if(element instanceof TemplateElement){
			TemplateElement e=(TemplateElement) element;
			return e.getText();
		}
		return  String.valueOf(element);
	}

}

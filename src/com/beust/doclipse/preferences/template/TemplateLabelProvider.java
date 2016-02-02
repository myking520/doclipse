package com.beust.doclipse.preferences.template;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.JavaElementLabels;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;

/**
 * @author myking520
 *
 */
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
			if(e.getKind()==TemplateElement.CPE_PROJECT){
				
				return Workbench.getInstance().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
			}
			if(e.getKind()==TemplateElement.CPE_PACKAGE){
				if((e.getText()==null||e.getText().length()==0)){
					return JavaPluginImages.DESC_OBJS_EMPTY_PACKAGE.createImage();
				}else{
					return  JavaPluginImages.DESC_OBJS_PACKAGE.createImage();
				}
			}
			if(e.getKind()==TemplateElement.CPE_JAVA){
				return JavaPluginImages.DESC_WIZBAN_JAVA_ATTACH.createImage();
			}
		}
		return  null;
	}
	@Override
	public String getText(Object element) {
		if(element instanceof TemplateElement){
			TemplateElement e=(TemplateElement) element;
			if((e.getText()==null||e.getText().length()==0)){
				return JavaElementLabels.DEFAULT_PACKAGE;
			}
			if(e.getKind()==TemplateElement.CPE_JAVA){
				int s=e.getText().lastIndexOf(IPath.SEPARATOR);
				if(s!=-1){
					return e.getText().substring(s+1);
				}
			}
			
			return e.getText();
		}
		return  String.valueOf(element);
	}

}

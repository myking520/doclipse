package com.beust.doclipse.launch.tester;

import javax.management.RuntimeErrorException;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;

import com.beust.doclipse.DoclipsePlugin;
import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.builder.DoclipseNature;
import com.beust.doclipse.preferences.template.TemplateElement;

public class PropertyTester1 extends PropertyTester {

	public PropertyTester1() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		
		if("isDoclipse".equals(property)){
			return this.hasTemplet(receiver);
		}
		if("hasDoclipse".equals(property)){
			try {
				return DoclipseProject.getCurrentProject().getNature(DoclipseNature.NATURE_ID)!=null;
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	
	}
	private boolean hasTemplet(Object receiver){
		if(receiver instanceof IAdaptable){
			IAdaptable adaptable=(IAdaptable) receiver;
			IJavaElement javaElement=adaptable.getAdapter(IJavaElement.class);
			if(javaElement!=null){
				String text=javaElement.getResource().getProjectRelativePath().toString();
				if(javaElement.getElementType()==IJavaElement.PACKAGE_FRAGMENT){
					text=javaElement.getResource().getFullPath().makeRelative().toString();
				}
				DoclipseProject doclipseProject=DoclipsePlugin.getDoclipseProject(javaElement.getJavaProject().getProject());
				TemplateElement element=	doclipseProject.getTemplateElementProvider().getElementRoot().getByText(text);
				return element!=null;
			}
		}
		return false;
	}
	
}

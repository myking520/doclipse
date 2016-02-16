package com.beust.doclipse.launch.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProjectNature;
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
			if(receiver instanceof IAdaptable){
				IAdaptable adaptable=(IAdaptable) receiver;
				IJavaElement javaElement=adaptable.getAdapter(IJavaElement.class);
				if(javaElement!=null){
					try {
						IProjectNature nature=javaElement.getJavaProject().getProject().getNature(DoclipseNature.NATURE_ID);
						if(nature==null){
							return false;
						}
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return false;
					}
					String text=javaElement.getResource().getProjectRelativePath().toString();
					if(javaElement.getElementType()==IJavaElement.PACKAGE_FRAGMENT){
						text=javaElement.getResource().getFullPath().makeRelative().toString();
					}
					DoclipseProject doclipseProject=DoclipsePlugin.getDoclipseProject(javaElement.getJavaProject().getProject());
					TemplateElement element=	doclipseProject.getTemplateElementProvider().getElementRoot().getByText(text);
					return element!=null;
				}
			}
		}
		return false;
	
	}
	
	
}

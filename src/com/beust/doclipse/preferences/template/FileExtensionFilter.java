package com.beust.doclipse.preferences.template;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FileExtensionFilter extends ViewerFilter{
	private String extension;
	private TemplatePage templatePage;
	public FileExtensionFilter(String extension,TemplatePage templatePage) {
		super();
		this.extension = extension;
		this.templatePage=templatePage;
	}
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof IFile){
			IFile file=(IFile)element;
			if(templatePage.getDoclipseProject().getTemplateElementProvider().getElementRoot().getByText(file.getFullPath().toString())!=null){
				return false;
			}
			return extension.equals(file.getFileExtension());
		}
		return false;
	}

}

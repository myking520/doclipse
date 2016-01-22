package com.beust.doclipse.preferences.template;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class FileExtensionFilter extends ViewerFilter{
	private String extension;
	
	public FileExtensionFilter(String extension) {
		super();
		this.extension = extension;
	}
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if(element instanceof IFile){
			IFile file=(IFile)element;
			return extension.equals(file.getFileExtension());
		}
		return false;
	}

}

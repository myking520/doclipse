package com.beust.doclipse.shortcut;

import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.builder.DoclipseBuilder;

public class DoclipseLaunchShortcut implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		DoclipseBuilder builder=new DoclipseBuilder();
		if(selection instanceof TreeSelection){
			TreeSelection tree=(TreeSelection)selection;
			Iterator lt=tree.iterator();
			while(lt.hasNext()){
				Object obj=lt.next();
				IResource res=null;
				if(obj instanceof IJavaElement){
					res=((IJavaElement)obj).getResource();
				}
				try {
					res.accept(builder);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			builder.buildTemplete();
			DoclipseProject.getCurrentProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		if(!(editor instanceof CompilationUnitEditor)){
			return;
		}
		DoclipseBuilder builder=new DoclipseBuilder();
		CompilationUnitEditor compilationUnitEditor=(CompilationUnitEditor) editor;
		FileEditorInput fileEditorInput=(FileEditorInput) compilationUnitEditor.getEditorInput();
		try {
			fileEditorInput.getFile().accept(builder);
			builder.buildTemplete();
			DoclipseProject.getCurrentProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

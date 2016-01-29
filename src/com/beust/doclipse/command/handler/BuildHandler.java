package com.beust.doclipse.command.handler;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.builder.DoclipseBuilder;

public class BuildHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection=	HandlerUtil.getCurrentSelection(event);
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
					res.accept(new DoclipseBuilder());
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			DoclipseProject.getCurrentProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

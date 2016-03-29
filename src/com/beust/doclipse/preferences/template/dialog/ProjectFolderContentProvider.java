package com.beust.doclipse.preferences.template.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.internal.ide.misc.ContainerContentProvider;

public class ProjectFolderContentProvider extends ContainerContentProvider{
	  @Override
		public Object[] getChildren(Object element) {
	        if (element instanceof IWorkspace) {
	            // check if closed projects should be shown
	            IProject[] allProjects = ((IWorkspace) element).getRoot()
	                    .getProjects();
	           
	            ArrayList accessibleProjects = new ArrayList();
	            for (int i = 0; i < allProjects.length; i++) {
	                if (allProjects[i].isOpen()&&allProjects[i].getName().equals("A")) {
	                    accessibleProjects.add(allProjects[i]);
	                }
	            }
	            return accessibleProjects.toArray();
	        } else if (element instanceof IContainer) {
	            IContainer container = (IContainer) element;
	            if (container.isAccessible()) {
	                try {
	                    List children = new ArrayList();
	                    IResource[] members = container.members();
	                    for (int i = 0; i < members.length; i++) {
	                        if (members[i].getType() != IResource.FILE) {
	                            children.add(members[i]);
	                        }
	                    }
	                    return children.toArray();
	                } catch (CoreException e) {
	                    // this should never happen because we call #isAccessible before invoking #members
	                }
	            }
	        }
	        return new Object[0];
	    }

}

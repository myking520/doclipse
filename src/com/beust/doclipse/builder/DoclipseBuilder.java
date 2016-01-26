package com.beust.doclipse.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.generama.defaults.QDoxPlugin;

import com.beust.doclipse.DoclipsePlugin;
import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.builder.plugin.PluginManager;
import com.beust.doclipse.preferences.template.TemplateElement;

public class DoclipseBuilder extends IncrementalProjectBuilder implements IResourceVisitor ,IResourceDeltaVisitor{
	public static final String BUILDER_ID = "com.beust.doclipse.builder.DoclipseBuilder";

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) {
			this.fullBuild(monitor);
		} else if (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD
				|| kind == IncrementalProjectBuilder.AUTO_BUILD) {
			incrementalBuild(monitor);
		} else if (kind == IncrementalProjectBuilder.CLEAN_BUILD) {
			clean(monitor);
		}
		getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
		return null;
	}

	private void incrementalBuild(IProgressMonitor monitor) throws CoreException {
		final IResourceDelta delta = getDelta(getProject());
		if (delta != null) {
			delta.accept(this);
		} else {
			fullBuild(monitor);
		}
	}

	private void fullBuild(IProgressMonitor monitor) throws CoreException {
		getProject().accept(this);
	}

	protected void clean(IProgressMonitor monitor) throws CoreException {

	}
	public boolean visit(IResource resource) throws CoreException {
		if (resource == null) {
			return false;
		}
		final String ext = resource.getFileExtension();
		System.out.println(resource.getFullPath().toString());
		if (!"java".equals(ext)) {
			return true;
		}
		this.buildResource(resource);
		return true;

	}
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		return this.visit(delta.getResource());
	}
	private void buildResource(IResource resource){
		DoclipseProject doclipseProject=DoclipsePlugin.getDoclipseProject(this.getProject());
		TemplateElement javaFile= doclipseProject.getTemplateElementProvider().getElementRoot().getByText(resource.getFullPath().toString());
		if(javaFile==null){
			return;
		}
		TemplateElement importTemplete=javaFile.getChildrenByKind(TemplateElement.CPE_IMPORT);
		QDoxPlugin qoxPlugin=PluginManager.getQDoxPlugin(doclipseProject, importTemplete);
		qoxPlugin.start();
	}

}

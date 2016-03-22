package com.beust.doclipse.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.generama.defaults.QDoxPlugin;

import com.beust.doclipse.DoclipsePlugin;
import com.beust.doclipse.DoclipseProject;
import com.beust.doclipse.builder.plugin.PluginManager;
import com.beust.doclipse.preferences.template.TemplateElement;

/**
 * @author myking520
 *
 */
public class DoclipseBuilder extends IncrementalProjectBuilder implements IResourceVisitor ,IResourceDeltaVisitor{
	public static final String BUILDER_ID = "com.beust.doclipse.builder.DoclipseBuilder";
	private static Map<Project,ClassManager> classManagers=new HashMap<Project,ClassManager>(){
		@Override
		public ClassManager get(Object key) {
			ClassManager cm= super.get(key);
			if(cm==null){
				Project project=(Project)key;
				cm=new ClassManager(JavaCore.create(project));
				this.put((Project) key, cm);
			}
			return cm;
		}
		
	};
	public ClassManager getClassManager(IProject project){
		return classManagers.get(project);
	}
	public DoclipseBuilder(){
	}
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) {
			this.fullBuild(monitor);
			this.buildTemplete(null);
		} else if (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD
				|| kind == IncrementalProjectBuilder.AUTO_BUILD) {
			incrementalBuild(monitor);
			this.buildTemplete(null);
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
		IProject project=resource.getProject();
		DoclipseProject doclipseProject=DoclipsePlugin.getDoclipseProject(project);
		TemplateElement javaFile= doclipseProject.getTemplateElementProvider().getElementRoot().getByText(resource.getProjectRelativePath().toString());
		if(javaFile==null){
			return;
		}
		ClassManager classManager=classManagers.get(project);
		PluginManager.process(doclipseProject, javaFile,classManager);
		
	}

	/**
	 * 最终调用
	 * 
	 */
	public void buildTemplete(IProject project){
		if(project==null){
			project=this.getProject();
			if(project==null){
				return ;
			}
		}
		ClassManager classManager=classManagers.remove(project);
		
	}
}

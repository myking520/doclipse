package com.beust.doclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.generama.defaults.QDoxPlugin;
import org.xdoclet.tools.PropertiesQDoxPropertyExpander;

import com.beust.doclipse.builder.plugin.DefaultQDoxCapableMetadataProvider;
import com.thoughtworks.qdox.model.JavaClass;

public class ClassManager {
	//类,JavaClass
	private Map<String,JavaClass> javaClass=new HashMap<String,JavaClass>();

	//路劲,JavaClass
	private Map<String,JavaClass> fileJavas=new HashMap<String,JavaClass>();
	private List<QDoxPlugin> tobeBuild=new ArrayList<QDoxPlugin>();
	private IJavaProject javaProject;
	
	public ClassManager(IJavaProject javaProject) {
		super();
		this.javaProject = javaProject;
	}
	public List<QDoxPlugin> getTobeBuild() {
		
		return tobeBuild;
	}
	public JavaClass getJavaClassByName(String className){
		return javaClass.get(className);
	}
	public JavaClass getJavaClassByFile(File file){
		return fileJavas.get(file.getAbsolutePath());
	}
	public JavaClass getJavaClass(String className){
		PropertiesQDoxPropertyExpander expander = new PropertiesQDoxPropertyExpander();
		Properties props = new Properties();
		expander.addProperties("props", props);
		try {
			IType type=	javaProject.findType(className);
			if(type==null){
				throw new ClassNotFoundException(className);
			}
			IJavaElement javaelement=type.getParent();
			DefaultQDoxCapableMetadataProvider metadataProvider = new DefaultQDoxCapableMetadataProvider(javaelement.getResource().getLocation().toFile(), expander,this);
			return metadataProvider.getJavaClass();
		} catch (Exception e) {
			throw new RuntimeException(className,e);
		}
	}
	public void save(DefaultQDoxCapableMetadataProvider metadataProvider){
		this.javaClass.put(metadataProvider.getJavaClass().getFullyQualifiedName(), metadataProvider.getJavaClass());
		this.fileJavas.put(metadataProvider.getJavaFile().getAbsolutePath(), metadataProvider.getJavaClass());
	}
	public void add2bBuild(QDoxPlugin builder){
		this.tobeBuild.add(builder);
	}
	public Map<String, JavaClass> getJavaClass() {
		return javaClass;
	}
}

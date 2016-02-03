package com.beust.doclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.generama.defaults.QDoxPlugin;

import com.beust.doclipse.builder.plugin.DefaultQDoxCapableMetadataProvider;
import com.thoughtworks.qdox.model.JavaClass;

public class ClassManager {
	//类,JavaClass
	private Map<String,JavaClass> javaClass=new HashMap<String,JavaClass>();

	//路劲,JavaClass
	private Map<String,JavaClass> fileJavas=new HashMap<String,JavaClass>();
	private List<QDoxPlugin> tobeBuild=new ArrayList<QDoxPlugin>();
	public List<QDoxPlugin> getTobeBuild() {
		return tobeBuild;
	}
	public JavaClass getJavaClassByName(String className){
		return javaClass.get(className);
	}
	public JavaClass getJavaClassByFile(File file){
		return fileJavas.get(file.getAbsolutePath());
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

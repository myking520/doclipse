package com.beust.doclipse.builder.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.generama.GeneramaException;
import org.xdoclet.QDoxMetadataProvider;
import org.xdoclet.QDoxPropertyExpander;

import com.beust.doclipse.builder.ClassManager;
import com.beust.doclipse.builder.DoclipseBuilder;
import com.thoughtworks.qdox.model.JavaClass;

public class DefaultQDoxCapableMetadataProvider extends QDoxMetadataProvider{
	private ClassManager classManager;
	private JavaClass javaClass;
	private File javaFile;
	public File getJavaFile() {
		return javaFile;
	}
	public JavaClass getJavaClass() {
		return javaClass;
	}
	public DefaultQDoxCapableMetadataProvider(File javaFile,QDoxPropertyExpander expander,ClassManager classManager) {
		super(javaFile,expander);
		this.javaFile=javaFile;
		this.classManager=classManager;
		this.getMetadata();
	}
	@Override
	public Collection getMetadata() throws GeneramaException {
		if(this.javaClass!=null){
			List<JavaClass> lt=new ArrayList<JavaClass>();
			lt.add(javaClass);
			return lt;
		}
		Collection coll= super.getMetadata();
		Iterator lterator=coll.iterator();
		if(lterator.hasNext()){
			this.javaClass=(JavaClass) lterator.next();
		}
		if(this.javaClass!=null)
		classManager.save(this);
		return coll;
	}

}

package com.beust.doclipse.preferences.template;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;

import org.eclipse.core.internal.resources.ProjectPreferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import com.alibaba.fastjson.JSON;
import com.beust.doclipse.DoclipseProject;

/**
 * @author myking520
 *
 */
public class TemplateElementProvider {
	TemplateElement elementRoot=new TemplateElement();
	private  ProjectPreferences preferences;
	private IFile file;
	public  TemplateElementProvider(IProject project){
		IEclipsePreferences eclipsePreferences = Platform.getPreferencesService().getRootNode();
		ProjectPreferences preferences = (ProjectPreferences) eclipsePreferences.node(ProjectScope.SCOPE);
		preferences = (ProjectPreferences) preferences.node(project.getName());
		try {
			if(!preferences.nodeExists(TemplateElementProvider.class.getName())){
				this.preferences= (ProjectPreferences) preferences.node(TemplateElementProvider.class.getName());
				this.saveOrUpdate();
			}else{
				this.preferences= (ProjectPreferences) preferences.node(TemplateElementProvider.class.getName());
			}
			file=project.getFile("doclipse");
			try{  
		        if(!file.exists()) {
		        	 java.io.File systemFile = file.getLocation().toFile();  
		        	 if(!systemFile.exists()){
		        		 file.create(new ByteArrayInputStream(new byte[0]), false, null);
		        		 file.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
		        	 }
		        }
		    }catch(Exception e){  
		        e.printStackTrace();  
		    }  
			this.read();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public TemplateElement getElementRoot() {
		return elementRoot;
	}

	public void saveOrUpdate(){
		 InputStream input = new ByteArrayInputStream(JSON.toJSONString(elementRoot,true).getBytes());
		try {
			file.setContents(input, IResource.FORCE, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void read(){
		InputStream input=null;
		try {
			input=file.getContents();
			BufferedReader br=new BufferedReader(new InputStreamReader(input));
			 StringBuilder sb = new StringBuilder();   
			 String linestr=null;
			 while((linestr=br.readLine())!=null){
				 sb.append(linestr);
			 }
			 if(sb.length()>0){
				 elementRoot= JSON.parseObject(sb.toString(), TemplateElement.class);
			 }
		} catch (CoreException | IOException e1) {
			e1.printStackTrace();
		}finally{
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}

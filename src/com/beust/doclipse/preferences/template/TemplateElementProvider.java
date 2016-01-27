package com.beust.doclipse.preferences.template;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.internal.resources.ProjectPreferences;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

import com.beust.doclipse.DoclipseProject;

/**
 * @author myking520
 *
 */
public class TemplateElementProvider {
	TemplateElement elementRoot=new TemplateElement();
	private  ProjectPreferences preferences;
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
			this.read();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public TemplateElement getElementRoot() {
		return elementRoot;
	}

	public void saveOrUpdate(){
		ByteArrayOutputStream bo=new ByteArrayOutputStream();
		 
		try {
			XMLEncoder en=new XMLEncoder(bo);
			en.writeObject(elementRoot);
			en.flush();
			this.preferences.put("elements", new String(bo.toByteArray()));
			this.preferences.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	private void read(){
		if(preferences.get("elements", null)==null){
			this.saveOrUpdate();
		}
		  ByteArrayInputStream bi=new ByteArrayInputStream(preferences.get("elements", null).getBytes());
		  ObjectInputStream oi;
		try {
			XMLDecoder de=new XMLDecoder(bi);
			elementRoot=(TemplateElement) de.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bi.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

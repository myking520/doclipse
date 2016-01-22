package com.beust.doclipse.preferences.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;

public class TemplateElement {
	public static final String SOURCEATTACHMENT= "sourcepath"; 
	public static final String OUTPUT= "output"; 
	public static final String EXCLUSION= "exclusion"; 
	public static final String INCLUSION= "inclusion";
	public static final int CPE_PROJECT = 2;
	public static final int CPE_PACKAGE = 3;
	public static final int CPE_JAVA = 4;
	private IResource resource;
	private int kind;
	private String text;
	private List<TemplateElement> children=new ArrayList<>();
	public List<TemplateElement> getChildren() {
		return children;
	}
	public void setChildren(List<TemplateElement> children) {
		this.children = children;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	
	public TemplateElement() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TemplateElement(IResource resource) {
		super();
		this.resource = resource;
	}
	public TemplateElement(String text){
		this.text=text;
	}
	public String getText() {
		if(resource!=null){
			return resource.getName();
		}
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public IResource getResource() {
		return resource;
	}
	public void setResource(IResource resource) {
		this.resource = resource;
	}
	
}

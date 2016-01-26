package com.beust.doclipse.preferences.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TemplateElement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SOURCEATTACHMENT= "sourcepath"; 
	public static final String OUTPUT= "output"; 
	public static final String EXCLUSION= "exclusion"; 
	public static final String INCLUSION= "inclusion";
	public static final int CPE_PROJECT = 2;//工程
	public static final int CPE_PACKAGE = 3;//包
	public static final int CPE_JAVA = 4;//类
	public static final int CPE_IMPORT = 5;//输入
	public static final int CPE_EXPORT = 6;//输出
	public static final int CPE_ENGINE = 7;//引擎
	private int kind;
	private String text;
	private List<TemplateElement> children=new ArrayList<>();
	public final static String ENGINE_JELLY="jelly";
	public final static String ENGINE_FREEMARKER="FreeMarker";
	public final static String ENGINE_VELOCITY="Velocity";
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
	}
	public TemplateElement(String text){
		this.text=text;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public TemplateElement getByText(String text){
		if(this.text.equals(text)){
			return this;
		}
		for(int i=0;i<this.children.size();i++){
			TemplateElement tmel=this.children.get(i);
			if(tmel.getText().equals(text)){
				return tmel;
			}
			tmel=tmel.getByText(text);
			if(tmel!=null){
				return tmel;
			}
		}
		return null;
	}
	public boolean remove(TemplateElement element){
		for(int i=0;i<this.children.size();i++){
			TemplateElement tmel=this.children.get(i);
			if(tmel.equals(element)){
				this.children.remove(i);
				return true;
			}
			if(tmel.remove(element)){
				return true;
			}
		}
		return false;
	}
	public TemplateElement getChildren(String text){
		for(int i=0;i<this.children.size();i++){
			TemplateElement tmel=this.children.get(i);
			if(tmel.getText().equals(text)){
				return tmel;
			}
		}
		return null;
	}
	public TemplateElement getParent(TemplateElement text){
		for(int i=0;i<this.children.size();i++){
			TemplateElement tmel=this.children.get(i);
			if(tmel.equals(text)){
				return this;
			}
			tmel=tmel.getParent(text);
			if(tmel!=null){
				return tmel;
			}
		}
		return null;
	}
	public TemplateElement getChildrenByKind(int kind){
		for(int i=0;i<this.children.size();i++){
			TemplateElement tmel=this.children.get(i);
			if(tmel.getKind()==kind){
				return tmel;
			}
		}
		return null;
	}
}

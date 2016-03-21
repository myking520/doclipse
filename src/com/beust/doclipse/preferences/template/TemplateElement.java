package com.beust.doclipse.preferences.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author myking520
 *
 */
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
	@JSONField(name="k")
	private int kind;
	@JSONField(name="t")
	private String text;
	@JSONField(name="u")
	private long updateTime;
	@JSONField(name="i")
	private String id;
	@JSONField(name="p")
	private String parentId;
	@JSONField(name="c")
	private List<TemplateElement> children=new ArrayList<>();
	public final static String ENGINE_JELLY="jelly";
	public final static String ENGINE_FREEMARKER="FreeMarker";
	public final static String ENGINE_VELOCITY="Velocity";
	public long getUpdateTime() {
		return updateTime;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
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
		if(this.text==null){
			return null;
		}
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

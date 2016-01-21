package com.beust.doclipse.tag;

import java.io.StringReader;

import com.beust.doclipsen.javacc.DoclipseParser;

/**
 * @author myking520
 *
 */
public class TagFragmentJavaCC implements ITagFragment {
	private DoclipseParser tagFragmentJavaCC;
	static final private String BLANKS = "* \t\n\r";
	public TagFragmentJavaCC(String s){
		tagFragmentJavaCC =new DoclipseParser(new StringReader(s));
		try {
			tagFragmentJavaCC.start();
		} catch (Exception e) {
		}catch(Error error){
		}
		if(this.completesOnTagName()){
			if(s!=null&&s.length()>0&&this.isSeparator(s.charAt(s.length()-1))){
				tagFragmentJavaCC.completeOn=DoclipseParser.completeOnAttributeName;
				tagFragmentJavaCC.fragment="";
			}
		}
	}
	private boolean isSeparator(char c) {
		return isBlank(c) || c == '=';
	}
	private boolean isBlank(char c) {
		return -1 != BLANKS.indexOf(c);
	}
	@Override
	public String getFragment() {
		return tagFragmentJavaCC.fragment;
	}

	@Override
	public String getTagName() {
		return tagFragmentJavaCC.tagName;
	}

	@Override
	public String getAttributeName() {
		return this.completesOnAttributeValue()?tagFragmentJavaCC.fragment:null;
	}
	@Override
	public boolean completesOnTagName() {
		return tagFragmentJavaCC.completeOn==DoclipseParser.completeOnTagName;
	}
	public boolean completesOnAttributeValue(){
		return tagFragmentJavaCC.completeOn==DoclipseParser.completeOnAttributeValue;
	}
	@Override
	public boolean completesOnAttribute() {
		return tagFragmentJavaCC.completeOn==DoclipseParser.completeOnAttributeName;
	}
	

}

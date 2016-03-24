package com.beust.doclipse.doclet;

public class NamedParam {
	private String name;
	private String value;
	public NamedParam(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	
}

package com.beust.doclipse.tag;

/**
 * A convenience class to make parsing of tags easier.
 *
 * @author Cedric Beust, Jun 17, 2004
 * 
 */
public class TagFragmentJavaCC implements ITagFragment {
	private String m_string = null;
	private int m_index = 0;
	private String m_tagName = null;
	private String m_attributeName = null;
	private String m_eq = null;
	private String m_attributeValue = null;
	private boolean m_completesOnAttribute = false;
	private boolean m_completesOnTagName = true;
	private String m_fragment = "";
	static final private String BLANKS = "* \t\n\r";

	public TagFragmentJavaCC(String string) {
		m_string = string;
		parseFragment();
	}

	private void parseFragment() {
	}

	private void setCompletes(boolean tag, boolean attribute) {
		m_completesOnTagName = tag;
		m_completesOnAttribute = attribute;
	}

	public String getTagName() {
		return m_tagName;
	}

	public String getAttributeName() {
		return m_attributeName;
	}

	public boolean completesOnTagName() {
		return m_completesOnTagName;
	}

	public boolean completesOnAttribute() {
		return m_completesOnAttribute;
	}

	public String getFragment() {
		return m_fragment;
	}

}

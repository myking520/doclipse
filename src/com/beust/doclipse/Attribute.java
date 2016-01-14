package com.beust.doclipse;


/**
 * This class
 *
 * @author Cedric Beust, Jun 11, 2004
 * 
 */
public class Attribute {
  private String m_name = null;
  private String m_doc = null;
  private String[] m_allowed = new String[0];
  private boolean m_optional = true;
  
  public Attribute(String name, String doc) {
    init(name, true, null, doc);
  }
  
  public Attribute(String name, boolean optional, String[] allowed, String doc) {
    init(name, optional, allowed, doc);
  }

  public Attribute(String name, boolean optional, String doc) {
    init(name, optional, null, doc);
  }

  private void init(String name, boolean optional, String[] values, String doc) {
    m_name = name;
    m_optional = optional;
    m_allowed = values;
    m_doc = doc;
  }
  
  public String[] getAllowed() {
    return m_allowed;
  }
  
  public String getName() {
    return m_name;
  }

  /**
   * @return
   */
  public boolean isOptional() {
    return m_optional;
  }

  public String getDoc() {
    return m_doc;
  }

}

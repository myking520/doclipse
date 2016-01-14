package com.beust.doclipse.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.beust.doclipse.Attribute;


/**
 * This class
 *
 * @author Cedric Beust, Jun 11, 2004
 * 
 */
public class Tag {
  //
  // Targets for the tags as specified in the XML definition file
  // 
  private boolean m_isClass = false;
  private boolean m_isMethod = false;
  private boolean m_isConstructor = false;
  private boolean m_isField = false;
  
  private String m_name = null;
  private String m_doc = null;
  // Map<String attributeName, Attribute>
  private Map m_attributes = new HashMap();
  
  public Tag(String name, String doc) {
    m_name = name;
    m_doc = doc;
  }
  
  public void setClass(boolean f){ 
    m_isClass = f;
  }
  
  public boolean isClass() {
    return m_isClass;
  }

  public void addAttribute(Attribute a) {
    m_attributes.put(a.getName(), a);
  }
  
  public String getName() {
    return m_name;
  }
  
  public Collection getAttributes() {
    return m_attributes.values();
  }
  
  public Attribute getAttribute(String attributeName) {
    return (Attribute) m_attributes.get(attributeName);
  }

  public List getRequiredAttributes() {
    List result = new ArrayList();
    
    for (Iterator it = m_attributes.values().iterator(); it.hasNext(); ) {
      Attribute a = (Attribute) it.next();
      if (! a.isOptional()) {
        result.add(a);
      }
    }
    
    return result;
  }

  /**
   * @return
   */
  public String getDoc() {
    return m_doc;
  }

  /**
   * @return Returns the isConstructor.
   */
  public boolean isConstructor() {
    return m_isConstructor;
  }
  /**
   * @param isConstructor The isConstructor to set.
   */
  public void setConstructor(boolean isConstructor) {
    m_isConstructor = isConstructor;
  }
  /**
   * @return Returns the isField.
   */
  public boolean isField() {
    return m_isField;
  }
  /**
   * @param isField The isField to set.
   */
  public void setField(boolean isField) {
    m_isField = isField;
  }
  /**
   * @return Returns the isMethod.
   */
  public boolean isMethod() {
    return m_isMethod;
  }
  /**
   * @param isMethod The isMethod to set.
   */
  public void setMethod(boolean isMethod) {
    m_isMethod = isMethod;
  }
}

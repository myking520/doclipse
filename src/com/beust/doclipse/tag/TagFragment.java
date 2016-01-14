package com.beust.doclipse.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * A convenience class to make parsing of tags easier.
 *
 * @author Cedric Beust, Jun 17, 2004
 * 
 */
public class TagFragment {
  private String m_string = null;
  private int m_index = 0;
  private String m_tagName = null;
  private String m_attributeName = null;
  private String m_eq = null;
  private String m_attributeValue = null;
  private boolean m_completesOnAttribute = false;
  private boolean m_completesOnTagName = true;
  
  // The string we are trying to complete
  private String m_fragment = "";
  
  static final private String BLANKS = "* \t\n\r";
  
  public TagFragment(String string) {
    m_string = string;
    parseFragment();
  }
  
  private void setCompletes(boolean tag, boolean attribute) {
    m_completesOnTagName = tag;
    m_completesOnAttribute = attribute;
  }
  
  private void parseFragment() {
    String s = m_string;
    int ind = s.length();
    boolean isEmpty = false;
    if (s.length() > 0) {
      ind = s.length() - 1;
      char c = s.charAt(ind);
      if (isSeparator(c)) {
        m_fragment = "";
        isEmpty = true;
      }
  
      List tokens = new ArrayList();
      String token = getNextToken();
      while (null != token) {
        tokens.add(0, token);
        token = getNextToken();
      }
      m_tagName = locateTagName();      
      String lastToken = tokens.get(0).toString();
      if (!isEmpty) m_fragment = lastToken;
      
      if (lastToken.startsWith("@")) {
        if (isEmpty) {
          setCompletes(false, true);
        }
        else {
          setCompletes(true, false);
        }
      }
      else if ("=".equals(lastToken)) {
        m_attributeName = tokens.get(1).toString();
        setCompletes(false, false);
      }
      else {
        String nextToLastToken = tokens.get(1).toString();
        if ("=".equals(nextToLastToken)) {
          if (isEmpty) {
            m_attributeName = "";
            setCompletes(false, true);
          }
          else {
            m_attributeName = tokens.get(2).toString();
            setCompletes(false, false);
          }
        }
        else {
          m_attributeName = lastToken;
          if (isEmpty) {
            setCompletes(false, false);
          }
          else {
            setCompletes(false, true);
          }
        }
      }
    }
  }
  
  private String locateTagName() {
    int ind = m_string.length() - 1;
    char c = m_string.charAt(ind);
    while (ind > 0 && c != '@') c = m_string.charAt(--ind);
    StringTokenizer st = new StringTokenizer(m_string.substring(ind));
    String result = st.nextToken();
    
    return result;
  }
  
  private String strip(String s) {
    int start = 0;
    char c = s.charAt(start);
    while (isSeparator(c)) c = s.charAt(++start);
    
    int end = s.length() - 1;
    c = s.charAt(end);
    while (isSeparator(c)) c = s.charAt(--end);
    
    String result = s.substring(start, end + 1);
    return result;
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
  
  private boolean isSeparator(char c) {
    return isBlank(c) || c == '=';
  }
  
  private boolean isBlank(char c) {
    return -1 != BLANKS.indexOf(c);
  }
  
  private boolean isReserved(char c) {
    return c == '=';
  }
  
  private char nextChar() {
    char result = m_string.charAt(m_index);
    return result;
  }
  
  private boolean eof() {
    return m_index >= m_string.length();
  }
  
  private void skipBlanks() {
    if (! eof()) {
      char c = m_string.charAt(m_index);
      while (! eof() && (isBlank(c))) {
        m_index++;
        if (! eof()) c = m_string.charAt(m_index);
      }
    }
  }
  
  private boolean isIdentifier(char c) {
    return Character.isJavaIdentifierPart(c) || c == ':' || c == '.' || c == '=';
  }
  
  private boolean isCursorOnSeparator() {
    int index = m_index;
    if (m_index >= m_string.length()) {
      index = m_string.length() - 1;
    }
    return (index > 0 && isSeparator(m_string.charAt(index)));
  }
  
  private String getNextToken() {
    String result = null;
    
    skipBlanks();
    
    int start = m_index;
    int end = m_index;
    if (! eof()) {
      char c = nextChar();
      
      if (isReserved(c)) {
        end = ++ m_index;
      }
      
      else {
        while (! eof() && ! isBlank(c) && ! isReserved(c)) {
          m_index++;
          if (! eof()) c = nextChar();
        }
        end = m_index;
      }
      
      if (eof()) end = m_string.length();
     result = m_string.substring(start, end);
    }
    
    return result;
  }
  
  private static void ppp(String s) {
    System.out.println("[TagFragment] " + s);
  }
  
  public static void main(String[] argv) {
//    String s = "a=b";
    String[] strings = {
        "", "@ej",  "@ej ", "@ej a",
        "@ej a ", "@ej a =", "@ej a = ", "@ej a = b",
        "@ej a = \"b\"", "@ej a = b ",
    };
    
    String[] fragments = {
        "", "@ej", "", "a",
        "", "", "", "b",
        "\"b\"", ""
    };
    
    boolean[] completesOnAttribute = {
        false, false, true, true,
        false, false, false, false,
        false, true
    };
    
    boolean[] completesOnTagName= {
        true, true, false, false,
        false, false, false, false,
        false, false
    };    
    
    String[] attributeNames = {
        "", "", "", "a",
        "a", "a", "a", "a",
        "a", ""
    };
    
    for (int i = 0; i < fragments.length; i++) {
      ppp("TEST " + i);
      TagFragment tf = new TagFragment(strings[i]);
      assert tf.completesOnAttribute() == completesOnAttribute[i]
        : i + " Complete on attribute : found " + tf.completesOnAttribute() + " but expected " + completesOnAttribute[i];
      assert tf.completesOnTagName() == completesOnTagName[i]
        : i + " Complete on tagName : found " + tf.completesOnTagName() + " but expected " + completesOnTagName[i];
      assert tf.getFragment().equals(fragments[i]) 
        : i + " Wrong fragment : found '" + tf.getFragment() +  "' but expected '" + fragments[i] + "'";
      
      String an = tf.getAttributeName() == null ? "" : tf.getAttributeName();
      assert an.equals(attributeNames[i]) 
      : i + " Wrong attributeName : found '" + an +  "' but expected '" + attributeNames[i] + "'";
    }
    
    ppp("ALL TESTS PASSED");
//    String s = "\n   check-exists-on-method=blah\n  foo=bar  * baz\n=\n  *   ced a = b ";
//    TagFragment tf = new TagFragment(s);
//    ppp(s + "\n" + tf.getFragment() + " " + tf.completesOnAttribute());
  }

}

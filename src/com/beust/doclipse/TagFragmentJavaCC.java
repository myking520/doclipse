package com.beust.doclipse;

import java.io.StringReader;

//import com.beust.doclipse.parser.DoclipseParser;
//import com.beust.doclipse.parser.ParseException;
//import com.beust.doclipse.parser.TokenMgrError;


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
  
  // The string we are trying to complete
  private String m_fragment = "";
  
  static final private String BLANKS = "* \t\n\r";
//  static private DoclipseParser m_parser = null;
  
  public TagFragmentJavaCC(String string) {
    m_string = string;
    parseFragment();
  }
  
  private void parseFragment() {
//    StringReader sr = new StringReader(m_string);
//    DoclipseParser parser = new DoclipseParser(new StringReader(m_string));
////    parser.ReInit(sr);
////    dp.ReInit(sr);
//    try {
//      parser.Start();
//      //
//      // If the last character is not a space, we are completing on that
//      // last word, otherwise we are completing on the next word.
//      if (' ' == m_string.charAt(m_string.length() - 1)) {
//        m_fragment = "";
//        if (parser.completeOnTagName) {
//          setCompletes(false, true);
//        }
//        else if (parser.completeOnAttributeName) {
//          setCompletes(false, false);
//        }
//        else {
//          setCompletes(true, false);
//        }
//      }
//      else {
//        m_fragment = DoclipseParser.fragment.toString();
//        setCompletes(parser.completeOnTagName, parser.completeOnAttributeName);
//      }
//    }
//    catch(TokenMgrError ex) {
//      // ignore
//    }
//    catch (ParseException ex) {
//      // ignore
//    }
  }
  
  private void setCompletes(boolean tag, boolean attribute) {
    m_completesOnTagName = tag;
    m_completesOnAttribute = attribute;
  }
//  
//  private void parseFragment() {
//    String s = m_string;
//    int ind = s.length();
//    boolean isEmpty = false;
//    if (s.length() > 0) {
//      ind = s.length() - 1;
//      char c = s.charAt(ind);
//      if (isSeparator(c)) {
//        m_fragment = "";
//        isEmpty = true;
//      }
//  
//      List tokens = new ArrayList();
//      String token = getNextToken();
//      while (null != token) {
//        tokens.add(0, token);
//        token = getNextToken();
//      }
//      m_tagName = locateTagName();      
//      String lastToken = tokens.get(0).toString();
//      if (!isEmpty) m_fragment = lastToken;
//      
//      if (lastToken.startsWith("@")) {
//        if (isEmpty) {
//          setCompletes(false, true);
//        }
//        else {
//          setCompletes(true, false);
//        }
//      }
//      else if ("=".equals(lastToken)) {
//        m_attributeName = tokens.get(1).toString();
//        setCompletes(false, false);
//      }
//      else {
//        String nextToLastToken = tokens.get(1).toString();
//        if ("=".equals(nextToLastToken)) {
//          if (isEmpty) {
//            m_attributeName = "";
//            setCompletes(false, true);
//          }
//          else {
//            m_attributeName = tokens.get(2).toString();
//            setCompletes(false, false);
//          }
//        }
//        else {
//          m_attributeName = lastToken;
//          if (isEmpty) {
//            setCompletes(false, false);
//          }
//          else {
//            setCompletes(false, true);
//          }
//        }
//      }
//    }
//  }
//  
//  private String locateTagName() {
//    int ind = m_string.length() - 1;
//    char c = m_string.charAt(ind);
//    while (ind > 0 && c != '@') c = m_string.charAt(--ind);
//    StringTokenizer st = new StringTokenizer(m_string.substring(ind));
//    String result = st.nextToken();
//    
//    return result;
//  }
//  
//  private String strip(String s) {
//    int start = 0;
//    char c = s.charAt(start);
//    while (isSeparator(c)) c = s.charAt(++start);
//    
//    int end = s.length() - 1;
//    c = s.charAt(end);
//    while (isSeparator(c)) c = s.charAt(--end);
//    
//    String result = s.substring(start, end + 1);
//    return result;
//  }
//  
  public String getTagName() {
    return m_tagName;
  }  
//  
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
//  
//  private boolean isSeparator(char c) {
//    return isBlank(c) || c == '=';
//  }
//  
//  private boolean isBlank(char c) {
//    return -1 != BLANKS.indexOf(c);
//  }
//  
//  private boolean isReserved(char c) {
//    return c == '=';
//  }
//  
//  /**
//   * @return the next character but don't advance
//   */
//  private char nextChar() {
//    char result = m_string.charAt(m_index);
//    return result;
//  }
//  
//  private boolean eof() {
//    return m_index >= m_string.length();
//  }
//  
//  private void skipBlanks() {
//    if (! eof()) {
//      char c = m_string.charAt(m_index);
//      while (! eof() && (isBlank(c))) {
//        m_index++;
//        if (! eof()) c = m_string.charAt(m_index);
//      }
//    }
//  }
//  
//  private boolean isIdentifier(char c) {
//    return Character.isJavaIdentifierPart(c) || c == ':' || c == '.' || c == '=';
//  }
//  
//  private boolean isCursorOnSeparator() {
//    int index = m_index;
//    if (m_index >= m_string.length()) {
//      index = m_string.length() - 1;
//    }
//    return (index > 0 && isSeparator(m_string.charAt(index)));
//  }
//  
//  private String getNextToken() {
//    String result = null;
//    
//    skipBlanks();
//    
//    int start = m_index;
//    int end = m_index;
//    if (! eof()) {
//      char c = nextChar();
//      
//      if (isReserved(c)) {
//        end = ++ m_index;
//      }
//      
//      else if ('"' == c) {
//        m_index++;
//        start = end = m_index;
//        if (! eof()) {
//          c = nextChar();
//          while (! eof() && ! ('"' == c)) {
//            m_index++;
//            if (! eof()) c = nextChar();
//          }
//          end = m_index;
//        }
//        result = m_string.substring(start, end);
//      }
//      
//      else {
//        while (! eof() && ! isBlank(c) && ! isReserved(c)) {
//          m_index++;
//          if (! eof()) c = nextChar();
//        }
//        end = m_index;
//      }
//      
//      if (eof()) end = m_string.length();
//     result = m_string.substring(start, end);
//    }
//    
//    if (null != result && trim(result).length() == 0) {
//      result = null;
//    }
//    
//    if (null != result) {
//      assert -1 == result.indexOf(BLANKS) : "SEPARATOR IN TOKEN " + result;
//    }
//    
//    return result;
//  }
//  
//  private String trim(String s) {
//    int start = 0;
//    String result = s;
//    if (s.length() > 0) {
//      char c = s.charAt(start);
//      while (isBlank(c) && start < s.length() - 1) {
//        c = s.charAt(++start);
//      }
//      
//      int end = s.length() - 1;
//      c = s.charAt(end);
//      while (end > 0 && isBlank(c)) {
//        c = s.charAt(--end);
//      }
//      
//      result = s.substring(start, end > start ? end + 1 : s.length());
//    }
//    return result;
//  }
  
}

package com.beust.doclipse.tag;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import antlr.Token;
import antlr.TokenStreamException;

import com.beust.doclipse.parser.DoclipseLexer;


/**
 * Parse tags with ANTLR.
 *
 * @author Cedric Beust, Jul 19, 2004
 * 
 */
public class TagFragmentAntlr implements ITagFragment {
  private String m_string = null;
  private String m_attributeValue = null;
  private boolean m_completesOnAttribute = false;
  private boolean m_completesOnTagName = true;
  private String m_tagName = null;
  private String m_attributeName = null;
  
  // The string we are trying to complete
  private String m_fragment = "";
  

  public TagFragmentAntlr(String s) {
    m_string = s; 
    parseFragment();
  }
  
  private String nextToken() throws TokenStreamException {
    Token t = m_lexer.nextToken();
    StringBuffer sb = new StringBuffer();
    if ("\"".equals(t.getText())) {
      t = m_lexer.nextToken();
      String text = t.getText();
      while (! "\"".equals(text)) {
        sb.append(text);
          t = m_lexer.nextToken();
          text = t.getText();        
      }
    }
    else {
      sb.append(t.getText());
    }
    
    String result = sb.toString();
    if (Token.EOF_TYPE == t.getType()) {
      result = null;
    }
    return result;
  }
  
  DoclipseLexer m_lexer = null;
  
  private void parseFragment() {
    StringReader sr = new StringReader(m_string);
    m_lexer = new DoclipseLexer(sr);
    try {
      String t = nextToken();
            
      if (null != t) {
        m_tagName = t;
        m_fragment = m_tagName;
        t = nextToken();
      }
      
      while (null != t) {
        m_attributeValue = null;
        m_attributeName = t;
        m_fragment = m_attributeName;
        t = nextToken();
        if (null != t) {
          // should be an = sign, skip it
          t = nextToken();
          if (null != t) {
            m_attributeValue = t;
            m_fragment = m_attributeValue;
            t = nextToken();
          }
        }
      }
    }
    catch (TokenStreamException ex) {
      ex.printStackTrace();
    }
    
    //
    // Parsing finished, set the booleans
    //
    if (m_string.length() > 0) {
      int ind = m_string.length() - 1;
      char c = m_string.charAt(ind);
      boolean isSeparator = isSeparator(c);
      if (isSeparator) {
        m_fragment = "";
      }
      
      // @a b = c
      if (null != m_tagName && null != m_attributeName && null != m_attributeValue) {
        if (isSeparator) {
          setCompletes(false, true);
        }
        else {
          setCompletes(false, false);
        }
      }
      // @a b
      else if (null != m_tagName && null != m_attributeName && null == m_attributeValue) {
        if (isSeparator) {
          setCompletes(false, false);
        }
        else {
          setCompletes(false, true);
        }      
      }
      // @a
      else if (null != m_tagName && null == m_attributeName && null == m_attributeValue) {
        if (isSeparator) {
          setCompletes(false, true);
        }
        else {
          setCompletes(true, false);
        }          
      }
    }
    
  }
  
  private boolean isSeparator(char c) {
    return isBlank(c) || c == '=';
  }

  static final private String BLANKS = "* \t\n\r";
  private boolean isBlank(char c) {
    return -1 != BLANKS.indexOf(c);
  }

  private void setCompletes(boolean tag, boolean attribute) {
    m_completesOnTagName = tag;
    m_completesOnAttribute = attribute;
  }
  
  public String getFragment() {
    return m_fragment;
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
  
  private static void ppp(String s) {
    System.out.println("[TagFragment] " + s);
  }
  
  public static void main(String[] argv) {
//    String s = "a=b";
    String[] strings = {
        "", "@ej",  "@ej ", "@ej a",
        "@ej a ", "@ej a =", "@ej a = ", "@ej a = b",
        "@ej a = \"b\"", "@ej a = b ",
        "@ej e = \"a b c\" ",
        "@ej\n  *  e = \"a b c\"\n  * "
    };
    
    String[] fragments = {
        "", "@ej", "", "a",
        "", "", "", "b",
        "b", "",
        "",
        ""
    };
    
    boolean[] completesOnAttribute = {
        false, false, true, true,
        false, false, false, false,
        false, true,
        true,
        true
    };
    
    boolean[] completesOnTagName= {
        true, true, false, false,
        false, false, false, false,
        false, false,
        false,
        false
    };    
    
    String[] attributeNames = {
        "", "", "", "a",
        "a", "a", "a", "a",
        "a", "a",
        "e",
        "e"
    };
    
    for (int i = 0; i < fragments.length; i++) {
      ppp("TEST " + i);
      ITagFragment tf = TagFragmentFactory.newTagFragment(strings[i]);
      assert tf.completesOnTagName() == completesOnTagName[i]
                                                           : i + " Complete on tagName : found " + tf.completesOnTagName() + " but expected " + completesOnTagName[i];
      assert tf.getFragment().equals(fragments[i]) 
                                                           : i + " Wrong fragment : found '" + tf.getFragment() +  "' but expected '" + fragments[i] + "'";
      assert tf.completesOnAttribute() == completesOnAttribute[i]
        : i + " Complete on attribute : found " + tf.completesOnAttribute() + " but expected " + completesOnAttribute[i];
      
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

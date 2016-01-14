package com.beust.doclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;


/**
 * This class represents a DefinitionFile
 *
 * @author Cedric Beust, Jun 14, 2004
 * 
 */
public class DefinitionFile {
  private String m_fileName = null;
  private File m_directory = null;
  private String m_description = null;
  private Map m_tags = new HashMap();
  private boolean m_entireFileRead = false;

  public DefinitionFile(File xmlFile) 
    throws ParserConfigurationException, SAXException, IOException 
  {
    m_fileName = xmlFile.getName();
    m_directory = xmlFile.getParentFile();
    init(new FileInputStream(xmlFile), true /* lazy */);
  }
  
  public DefinitionFile(String fileName, InputStream xmlFile) 
    throws ParserConfigurationException, SAXException, IOException 
  {
    m_fileName = fileName;
    m_directory = null;
    init(xmlFile, true /* lazy */);
  }
   
  private void init(InputStream xmlFile, boolean lazy) 
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();    
    factory.setValidating(true);
    factory.setNamespaceAware(true);
    
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      ErrorHandler eh = null;
      builder.setErrorHandler(eh);
      Document document = builder.parse(xmlFile);
      NodeList nl = document.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        Node node = nl.item(i);
        if ("doclipse".equals(node.getNodeName())) {
          //
          // Parse tags
          //
          NodeList tags = node.getChildNodes();
          for (int j = 0; j < tags.getLength(); j++) {
            Node tag = tags.item(j);
            String nodeName = tag.getNodeName();
            if ("tag".equals(nodeName)) {
              parseTag(tag);
            }
            else if ("description".equals(nodeName)) {
              m_description = tag.getFirstChild().getNodeValue();
              
              // If lazy, we are only interested in the description file
              if (lazy) {
                m_entireFileRead = false;
//                ppp("NOT PARSING " + m_fileName);
                return;
              }
              else {
//                ppp("PARSING ENTIRE FILE " + m_fileName);
              }
            }
          }
        } // "doclipse".equals(node)
      }
      
      m_entireFileRead = true;
    }
    catch (DOMException ex) {
      Utils.showError(ex, m_fileName);
    }
    catch (ParserConfigurationException ex) {
      Utils.showError(ex, m_fileName);
    }
    catch (SAXException ex) {
      Utils.showError(ex, m_fileName);
    }
    catch (IOException ex) {
      Utils.showError(ex, m_fileName);
    }
  }
  
  private static void ppp(String s) {
    System.out.println("[DefinitionFile] " + s);
  }
  
  private void parseTag(Node tag) {
    NamedNodeMap tagAttributes = tag.getAttributes();
    if (null != tagAttributes) {
      Node tagName = tagAttributes.getNamedItem("name");
      Node tagTarget= tagAttributes.getNamedItem("target");
      Node tagDoc = tagAttributes.getNamedItem("doc");
      String doc = null != tagDoc ? tagDoc.getNodeValue() : null;
//    ppp("Adding tag " + tagName.getNodeValue());
      Tag theTag = new Tag(tagName.getNodeValue(), doc);

      // Parse the target attribute
      if (null != tagTarget) {
        String targets = tagTarget.getNodeValue();
        StringTokenizer st = new StringTokenizer(targets);
        while (st.hasMoreTokens()) {
          String t = st.nextToken();
          
          if ("class".equalsIgnoreCase(t)) theTag.setClass(true); 
          if("method".equalsIgnoreCase(t)) theTag.setMethod(true);
          if ("field".equalsIgnoreCase(t)) theTag.setField(true);
          if ("constructor".equalsIgnoreCase(t)) theTag.setConstructor(true);
        }
      }
      
      m_tags.put(theTag.getName(), theTag);
//      System.out.println(tagName.toString() + " " + (null != tagTarget ? tagTarget.toString() : ""));
      
      //
      // Parse attributes for this tag
      //
      NodeList attributes = tag.getChildNodes();
      for (int k = 0; k < attributes.getLength(); k++) {
        Node attribute = attributes.item(k);
        NamedNodeMap attributeAttributes = attribute.getAttributes();
        if (null != attributeAttributes) {
          
          // Read name
          Node attributeNode = attributeAttributes.getNamedItem("name");
          String attributeName = attributeNode.getNodeValue();
          
          // Read required
          Node requiredNode = attributeAttributes.getNamedItem("required");
          boolean isRequired = false;
          if (null != requiredNode) isRequired = "true".equalsIgnoreCase(requiredNode.getNodeValue());
                    
          // Read doc
          Node attributeDocNode = attributeAttributes.getNamedItem("doc");
          String attributeDocName = attributeDocNode != null ? attributeDocNode.getNodeValue() : null;
          
          // Read allowed
          Node nodeAllowed = attributeAttributes.getNamedItem("allowed");
          String[] allowed = parseMultiString(nodeAllowed);
          Attribute a = 
            new Attribute(attributeName, ! isRequired, allowed, attributeDocName);
          theTag.addAttribute(a);
        }
      }
    }    
  }
  
  private String[] parseMultiString(Node node) {
    List vResult = new ArrayList();
    
    if (null != node) {
      StringTokenizer st = new StringTokenizer(node.getNodeValue());
      while (st.hasMoreTokens()) {
        vResult.add(st.nextToken());
      }
    }
    
    String[] result = (String[]) vResult.toArray(new String[vResult.size()]);
    return result;
  }
  
  public String getFileName() {
    return m_fileName;
  }
  
  public String getDescription() {
    return m_description;
  }
  
  public Map getTags() {
    if (! m_entireFileRead) {
      InputStream is = null == m_directory ? 
          Utils.getInternalFile(m_fileName) : 
          Utils.getExternalFile(m_directory, m_fileName); 
      init(is, false /* not lazy this time */);
    }
    return m_tags;
  }
}

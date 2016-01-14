package com.beust.doclipse;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;


/**
 * This class represents a completion proposal for a tag..
 *
 * @author Cedric Beust, Jun 11, 2004
 * 
 */
public class TagCompletionProposal 
  extends DoclipseCompletionProposal
{
  private int m_offset = 0;
  private int m_length = 0;
  private String m_tag = null;

  public TagCompletionProposal(String tagName, String doc) {
    super(tagName, doc);
  }
  
  public void apply(IDocument document, char trigger, int offset) {
    // Locate the @
    m_offset = Utils.locateIndexAtSign(document, offset);
    m_length = offset - m_offset;
    apply(document);
    
  }  

  public void apply(IDocument document) {
    try {
      StringBuffer completion = new StringBuffer(getName());
      Tag tag = DoclipsePlugin.getTag(getName());
      
      assert null != tag : "Didn't find tag " + getName();
      
      //
      // It's a tag, the replacement must be made of the tag name
      // and also all the required attributes
      //
      List attributes = tag.getRequiredAttributes();
      for (Iterator it = attributes.iterator(); it.hasNext(); ) {
        Attribute a = (Attribute) it.next();
        // TODO:  use the indent preference instead of hardcoding
        // two spaces
        String eq = Preferences.insertSpacesAroundEqual() ? " = " : "=";  
        completion.append("\n *   " + a.getName() + eq);
      }
      document.replace(m_offset, m_length, completion.toString()); 
    } 
    catch (BadLocationException x) {
      // ignore
    }
  }
}

package com.beust.doclipse;

import org.eclipse.jface.text.IDocument;

import com.beust.doclipse.preferences.Preferences;


/**
 * This class represents a completion proposal for an attribute.
 *
 * @author Cedric Beust, Jun 17, 2004
 * 
 */
public class AttributeCompletionProposal
  extends DoclipseCompletionProposal
{
    public AttributeCompletionProposal(String tagName, String attributeName, String doc) {
      super(attributeName, doc);
    }
    
    public void apply(IDocument document, char trigger, int offset) {
      m_offset = Utils.locateIndexWordLeftOfCursor(document, offset);
      String eq = Preferences.insertSpacesAroundEqual() ? " = " : "=";  
      m_completion = getName() + eq;
      m_length = offset - m_offset;
      apply(document);
    }  

}

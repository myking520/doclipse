package com.beust.doclipse;

import org.eclipse.jface.text.IDocument;


/**
 * This class represents a completion proposal for an attribute value.
 *
 * @author Cedric Beust, Jun 19, 2004
 * 
 */
public class AttributeValueCompletionProposal
extends DoclipseCompletionProposal
{
    public AttributeValueCompletionProposal(String tagName, String value) {
      super(value, null /* no doc for attribute values */);
      m_completion = Preferences.surroundWithDoubleQuotes() ? "\"" + value + "\"" : value; 
    }
    
    public void apply(IDocument document, char trigger, int offset) {
      m_offset = Utils.locateIndexWordLeftOfCursor(document, offset);
      m_length = offset - m_offset;
      apply(document);      
    }  

}

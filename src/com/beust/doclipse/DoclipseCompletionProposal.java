package com.beust.doclipse;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

/**
 * Base class for all completion proposals implemented by Doclipse
 * (TagCompletionProposal, AttributeCompletionProposal and
 * AttributeValueCompletionProposal).
 *
 * @author Cedric Beust, Jun 17, 2004
 * 
 */
abstract public class DoclipseCompletionProposal
		// extends PositionBasedCompletionProposal
		implements IJavaCompletionProposal, ICompletionProposalExtension // ,
																			// ICompletionProposalExtension2
// extends JavaCompletionProposal
{
	private String m_name = null;
	private String m_doc = null;
	protected int m_offset = 0;
	protected int m_length = 0;
	protected String m_completion = null;

	public DoclipseCompletionProposal(String name, String doc) {
		m_name = name;
		m_doc = doc;
	}

	public void apply(IDocument document) {
		try {
			//
			// Bug in Eclipse? If length is 0, replace() will not
			// position the cursor at the end of the replaced text.
			// Workaround: create a completion of length 1 and
			// include the character left of the word in the
			// replacement.
			//
			if (0 == m_length) {
				m_offset--;
				m_length++;
				m_completion = document.getChar(m_offset) + m_completion;
			}
			document.replace(m_offset, m_length, m_completion);
		} catch (BadLocationException x) {
			// ignore
		}
	}

	public Point getSelection(IDocument document) {
		return null;
	}

	public String getName() {
		return m_name;
	}

	public String getAdditionalProposalInfo() {
		return m_doc;
	}

	public String getDisplayString() {
		return getName();
	}

	public Image getImage() {
		return null;
	}

	public IContextInformation getContextInformation() {
		IContextInformation result = new ContextInformation("Display string", "Info string");
		return result;
	}

	/**
	 * Place ourselves at the top of the list (before HTML elements and such).
	 * If somebody installed Doclipse, they will most likely want to see our
	 * completions first :-)
	 */
	public int getRelevance() {
		return 90;
	}

	/**
	 * Determine if the characters typed so far qualify to this proposal. If
	 * they don't, the popup will disappear, otherwise it will stay on and
	 * narrow down to the possible completions.
	 */
	public boolean isValidFor(IDocument document, int offset) {
		boolean result = false;
		String prefix = null;
		int atOffset = Utils.locateIndexWordLeftOfCursor(document, offset);
		try {
			prefix = document.get(atOffset, offset - atOffset);
			result = getName().startsWith(prefix);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	private static void ppp(String s) {
		System.out.println("[DoclipseCompletionProposal] " + s);
	}

	public int getContextInformationPosition() {
		return 0;
	}

	public char[] getTriggerCharacters() {
		return null;
	}

}

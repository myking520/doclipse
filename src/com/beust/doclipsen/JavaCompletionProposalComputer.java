package com.beust.doclipsen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.text.javadoc.JavadocContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

import com.beust.doclipse.Attribute;
import com.beust.doclipse.AttributeCompletionProposal;
import com.beust.doclipse.AttributeValueCompletionProposal;
import com.beust.doclipse.DoclipsePlugin;
import com.beust.doclipse.Utils;
import com.beust.doclipse.tag.ITagFragment;
import com.beust.doclipse.tag.Tag;
import com.beust.doclipse.tag.TagCompletionProposal;
import com.beust.doclipse.tag.TagFragmentFactory;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * 
 * @author dell
 *
 */
public class JavaCompletionProposalComputer implements IJavaCompletionProposalComputer {

	@Override
	public void sessionStarted() {

	}

	public JavaCompletionProposalComputer() {
		
	}

	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context,
			IProgressMonitor monitor) {
		Map tags = DoclipsePlugin.getDoclipseProject().getAllTags();
		List vResult = new ArrayList();
		int offset = context.getInvocationOffset();
		JavadocContentAssistInvocationContext javadoc = (JavadocContentAssistInvocationContext) context;
		IJavaElement element = null;
		try {
			element = javadoc.getCompilationUnit().getElementAt(offset);
		} catch (JavaModelException e) {
			e.printStackTrace();
			return new ArrayList<ICompletionProposal>();
		}
		m_document = context.getDocument();
		try {
			// Locate if we are on a method, a class, etc...
			
			// Let's see if we are trying to complete on a tag or on
			// an attribute. First, locate where the @ sign started.
			String pref = Utils.locateWordThatStartsWithAt(m_document, offset);
			if(pref==null){
				return new ArrayList<ICompletionProposal>();
			}
			//
			// Determine what the user is trying to complete on:
			// - tag name
			// - attribute name
			// - attribute value
			// TagFragment will do all the dirty parsing for us.
			//
			
			ITagFragment tf = TagFragmentFactory.newTagFragment(pref);
			String fragment = tf.getFragment();
			if (tf.completesOnTagName()) {
				for (Iterator it = tags.values().iterator(); it.hasNext();) {
					Tag tag = (Tag) it.next();
					String tagName = tag.getName();
					boolean starts = tagName.startsWith(fragment);
					boolean matchTarget = matchTarget(tag, element);
					if (starts && matchTarget) {
						vResult.add(new TagCompletionProposal(tag.getName(), tag.getDoc()));
					}
				}

			}
			else if (tf.completesOnAttribute()) {
				String tagName = tf.getTagName();
				Tag tag = (Tag) tags.get(tagName);
				if (null != tag) {
					Collection attributes = tag.getAttributes();
					for (Iterator it = attributes.iterator(); it.hasNext();) {
						Attribute a = (Attribute) it.next();
						String attributeName = a.getName();
						if(tf.hasAttr(attributeName)){
							continue;
						}
						String doc = a.getDoc();
						if (attributeName.startsWith(fragment)) {
							vResult.add(new AttributeCompletionProposal(tagName, attributeName, doc));
						}
					}
				}
			}

			else {
				String tagName = tf.getTagName();
				Tag tag = (Tag) tags.get(tagName);
				if (null != tag) {
					Attribute a = tag.getAttribute(tf.getAttributeName());
					if (null != a) {
						String[] allowed = a.getAllowed();
						for (int i = 0; i < allowed.length; i++) {
							vResult.add(new AttributeValueCompletionProposal(tagName, allowed[i]));
						}
					}
				}
			}
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
		return vResult;
	}

	@Override
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context,
			IProgressMonitor monitor) {
		List<IContextInformation> result = new ArrayList<IContextInformation>();
		Map tags = DoclipsePlugin.getDoclipseProject().getAllTags();

		int i = 0;
		for (Iterator it = tags.values().iterator(); it.hasNext();) {
			Tag tag = (Tag) it.next();
			result.add(new ContextInformation("Context", tag.getName()));
		}
		return result;
	}

	@Override
	public void sessionEnded() {

	}

	private IDocument m_document = null;
	/**
	 * 
	 * @param prefix
	 * @return true if the current prefix is a tag with an incomplete attribute
	 *         (e.g. "@ejbgen:entity ejb-n"
	 */
	private boolean isAttribute(String prefix) {
		return -1 != prefix.indexOf(" ") || -1 != prefix.indexOf("\n") || -1 != prefix.indexOf("\t")
				|| -1 != prefix.indexOf("*");
	}

	/**
	 * @return true if the given elementType matches the target of this tag
	 */
	private boolean matchTarget(Tag tag, IJavaElement element) {
		int elementType = element.getElementType();
		boolean isConstructor = element.getElementName().equals(element.getParent().getElementName());
		boolean result = elementType == IJavaElement.TYPE && tag.isClass()
				|| (!isConstructor && elementType == IJavaElement.METHOD && tag.isMethod())
				|| elementType == IJavaElement.FIELD && tag.isField() || isConstructor && tag.isConstructor();

		return result;
	}

	private static void ppp(String s) {
		JavaClass jc=null;
//		jc.getMethods()[0].getParameters()[0].getType().getGenericValue()
//		jc.getFields()[0].getTagByName("").getParameters()
//		jc.getFullyQualifiedName()
		JavaMethod jm=null;
		jm.getTagsByName("aa");
		System.out.println("[DoclipseJavaDocProcessor] " + s);
	}

	public String getErrorMessage() {
		return null;
	}

}

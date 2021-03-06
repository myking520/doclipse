package com.beust.doclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.xml.sax.SAXException;

/**
 * Helper methods for the Doclipse plug-in
 *
 * @author Cedric Beust, Jun 11, 2004
 * 
 */
public class Utils {
	static final private String SEPARATORS = "* \t\n\r";
	static final private String SEPARATORS_WITH_EQUAL = "* \t\n\r=";

	public static void main(String[] argv) {
	}

	public static void showError(Exception ex, String fileName) {
		showError(JavaPlugin.getActiveWorkbenchShell(), ex, fileName);
	}

	public static boolean isEmptyString(String s) {
		return null == s || "".equals(s);
	}

	private static void showError(Shell parent, Exception ex, String fileName) {
		MessageBox mb = new MessageBox(parent, SWT.ICON_ERROR);
		mb.setMessage(ex.getMessage());
		mb.setText("Error while parsing " + fileName);
		mb.open();
	}

	public static DefinitionFile[] readInternalFiles(Map fileNames) {
		
		List vResult = new ArrayList();
		for (int i = 0; i < Constants.INTERNAL_FILES.length; i++) {
			String file = Constants.INTERNAL_FILES[i];
			if (null == fileNames || (null != fileNames && null != fileNames.get(file))) {
				InputStream is = getInternalFile(file);
				try {
					DefinitionFile df = new DefinitionFile(file, is);
					vResult.add(df);
				} catch (ParserConfigurationException ex) {
					Shell parent = DoclipsePlugin.getDefault().getWorkbench().getDisplay().getActiveShell();
					showError(parent, ex, file);
				} catch (SAXException ex) {
					Shell parent = DoclipsePlugin.getDefault().getWorkbench().getDisplay().getActiveShell();
					showError(parent, ex, file);
				} catch (IOException ex) {
					Shell parent = DoclipsePlugin.getDefault().getWorkbench().getDisplay().getActiveShell();
					showError(parent, ex, file);
				}
			}
		}

		DefinitionFile[] result = (DefinitionFile[]) vResult.toArray(new DefinitionFile[vResult.size()]);

		return result;
	}

	/**
	 * Reread the definition files in the specified directory but only those in
	 * fileNames[]
	 */
	static public DefinitionFile[] readDirectory(String directory, Map fileNames) {
		List vResult = new ArrayList();

		if (null != directory && !"".equals(directory)) {
			File f = new File(directory);
			File[] files = f.listFiles();
			for (int i = 0; i < files.length; i++) {
				File thisFile = files[i];
				String fileName = thisFile.getName();
				if (null == fileNames && fileName.endsWith(".xml")
						|| (null != fileNames && null != fileNames.get(fileName))) {
					DefinitionFile df = parseXmlDefinition(thisFile);
					if (null != df) {
						vResult.add(df);
					}
				}
			}
		}

		DefinitionFile[] result = (DefinitionFile[]) vResult.toArray(new DefinitionFile[vResult.size()]);
		return result;
	}

	private static DefinitionFile parseXmlDefinition(File f) {
		DefinitionFile result = null;
		try {
			result = new DefinitionFile(f);
		} catch (ParserConfigurationException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (SAXException ex) {
			// ignore
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private static boolean isA(char c, String s) {
		return -1 != s.indexOf(c);
	}


	public static int locateIndexAtSign(IDocument document, int offset) {
		int result = offset;
		try {
			int line=document.getLineOfOffset(offset);
			
			outf:while(line>-1){
			IRegion rg=document.getLineInformation(line);
			line--;
			String str= document.get( rg.getOffset(),  rg.getLength());
			boolean hasStar=false;
			for(int i=0;i<str.length();i++){
				char c=str.charAt(i);
				if(c=='*'){
					if(hasStar){
						result=-1;
						break;
					}
					hasStar=true;
					continue;
				}
				if(Character.isWhitespace(c)){
					continue;
				}
				if(c=='@'){
					result=rg.getOffset()+i;
					break outf;
				}
				break;
			}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static int locateIndexWordLeftOfCursor(IDocument document, int offset) {

		int result = offset - 1;

		try {
			// We only do the backward search on the current line, which can
			// potentially
			// bug if somebody is doing "attribute = value" on two different
			// lines.
			// But well. Don't do dat.
			IRegion region = document.getLineInformationOfOffset(offset);
			int leftLimit = region.getOffset();
			char c = document.getChar(result);

			// // Skip spaces
			// while (result > leftLimit && isA(c, SEPARATORS)) {
			// c = document.getChar(--result);
			// }

			// Keep going backward until we hit the @ sign
			while (result > leftLimit && !isA(c, SEPARATORS_WITH_EQUAL)) {
				c = document.getChar(--result);
			}

			if (result == leftLimit) {
				result = offset - 1;
			} else {
				result++;
			}
		} catch (BadLocationException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		return result;
	}

	public static InputStream getInternalFile(String file) {
		InputStream result = Utils.class.getClassLoader().getResourceAsStream(Constants.DIRECTORY + file);
		return result;
	}

	/**
	 * @return the word left of cursor
	 */
	public static String locateWordThatStartsWithAt(IDocument document, int offset) throws BadLocationException {
		int off = locateIndexAtSign(document, offset);
		if (off == -1) {
			return null;
		}
		String result = document.get(off, offset - off);

		return result;
	}

	/**
	 * @param directory
	 * @param fileName
	 * @return
	 */
	public static InputStream getExternalFile(File directory, String fileName) {
		InputStream result = null;

		try {
			result = new FileInputStream(new File(directory, fileName));
		} catch (FileNotFoundException ex) {
			Utils.showError(ex, directory + "/" + fileName);
		}
		return result;
	}

}

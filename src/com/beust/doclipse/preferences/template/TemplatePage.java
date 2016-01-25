package com.beust.doclipse.preferences.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.internal.ui.wizards.IStatusChangeListener;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.CPListElementSorter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.TreeListDialogField;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

import com.beust.doclipse.DoclipseProject;

/**
 * 模版页
 * @author myking520
 *
 */
public class TemplatePage extends PropertyPage implements IStatusChangeListener {
	private TreeListDialogField<TemplateElement> treeListDialogField;
	private TemplateElementProvider templateElementProvider=null;

	public static String[] buttonLabels= new String[] {
			"add JavaFile",
			"add Template",
			"edit",
			"del"
		};
	@Override
	public void statusChanged(IStatus status) {
	}
	public TemplateElementProvider getTemplateElementProvider() {
		return templateElementProvider;
	}
	@Override
	protected Control createContents(Composite parent) {
		templateElementProvider=new TemplateElementProvider();
		TemplateAdapter adapter=new TemplateAdapter(this);
		treeListDialogField=new ElementsListDialogField<TemplateElement>(adapter, buttonLabels, new TemplateLabelProvider());
		List<TemplateElement> elements=new ArrayList<TemplateElement>();
		TemplateElement project=templateElementProvider.getElementRoot();
		project.setText(DoclipseProject.getCurrentProject().getName());
		project.setKind(IClasspathEntry.CPE_PROJECT);
		elements.add(project);
		treeListDialogField.setElements(elements);
		PixelConverter converter= new PixelConverter(parent);
		Composite composite= new Composite(parent, SWT.NONE);
		LayoutUtil.doDefaultLayout(composite, new DialogField[] { treeListDialogField }, true, SWT.DEFAULT, SWT.DEFAULT);
		LayoutUtil.setHorizontalGrabbing(treeListDialogField.getTreeControl(null));
		int buttonBarWidth= converter.convertWidthInCharsToPixels(24);
		treeListDialogField.setButtonsMinWidth(buttonBarWidth);
		this.diableButtons();
		treeListDialogField.setViewerComparator(new CPListElementSorter());
	
		treeListDialogField.getTreeViewer().refresh();
		return composite;
	}
	/**
	 * 初始化按钮状态
	 */
	private void diableButtons(){
		for(int i=0;i<buttonLabels.length;i++){
			treeListDialogField.enableButton(i, false);
		}
	}
	public TreeListDialogField<TemplateElement> getTreeListDialogField() {
		return treeListDialogField;
	}
}

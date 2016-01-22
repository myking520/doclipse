package com.beust.doclipse.preferences.template;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.internal.ui.wizards.IStatusChangeListener;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.CPListElementSorter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.TreeListDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
	
	public static String[] buttonLabels= new String[] {
			"添加类",
			"添加模板",
			"添加输出",
			"编辑",
			"删除"
		};
	@Override
	public void statusChanged(IStatus status) {
	}

	@Override
	protected Control createContents(Composite parent) {
		TemplateAdapter adapter=new TemplateAdapter(this);
		treeListDialogField=new TreeListDialogField<TemplateElement>(adapter, buttonLabels, new TemplateLabelProvider());
		List<TemplateElement> elements=new ArrayList<TemplateElement>();
		TemplateElement project=new TemplateElement(DoclipseProject.getCurrentProject());
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

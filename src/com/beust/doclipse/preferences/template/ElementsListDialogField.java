package com.beust.doclipse.preferences.template;

import org.eclipse.jdt.internal.ui.wizards.dialogfields.ITreeListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.TreeListDialogField;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * @author myking520
 *
 */
public class ElementsListDialogField<E> extends TreeListDialogField<E> {

	public ElementsListDialogField(ITreeListAdapter<E> adapter, String[] buttonLabels, ILabelProvider lprovider) {
		super(adapter, buttonLabels, lprovider);
	}

	@Override
	protected TreeViewer createTreeViewer(Composite parent) {
		TreeViewer treeview = super.createTreeViewer(parent);
		// treeview.setCellEditors(new CellEditor[]{new
		// ComboBoxCellEditor(treeview.getTree(), new String[]{"first",
		// "second", "third", "forth"})});
		// treeview.setCellModifier(new CellModifier());
		// treeview.setColumnProperties(new String[]{"1","2"});
		return treeview;
	}

}

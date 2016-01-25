package com.beust.doclipse.preferences.template;

import org.eclipse.jface.viewers.ICellModifier;

public class CellModifier implements ICellModifier {

	@Override
	public boolean canModify(Object element, String property) {
		return false;
	}

	@Override
	public Object getValue(Object element, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modify(Object element, String property, Object value) {
		// TODO Auto-generated method stub

	}

}

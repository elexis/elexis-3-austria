/*******************************************************************************
 * Copyright (c) 2015 MEDEVIT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 *******************************************************************************/
package at.medevit.elexis.at.rezepte.ui.abgabedialog;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import at.medevit.elexis.at.rezepte.model.Verschreibung;
import at.medevit.elexis.at.rezepte.ui.PropertyTextCellEditor;

public class AbgabeDialogEditingSupport extends EditingSupport {

	private TableViewer viewer;
	
	public AbgabeDialogEditingSupport(ColumnViewer viewer) {
		super(viewer);
		this.viewer = (TableViewer) viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new PropertyTextCellEditor(viewer, 0);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return ((Verschreibung) element).getOriginalpackung();
	}

	@Override
	protected void setValue(Object element, Object value) {
		((Verschreibung) element).setOriginalpackung(String
				.valueOf(value));
		viewer.refresh();
	}

}

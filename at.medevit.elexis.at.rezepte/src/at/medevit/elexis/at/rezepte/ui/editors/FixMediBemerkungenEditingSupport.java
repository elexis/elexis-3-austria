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
package at.medevit.elexis.at.rezepte.ui.editors;

import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import at.medevit.elexis.at.rezepte.ui.FixMediDisplay;
import at.medevit.elexis.at.rezepte.ui.PropertyTextCellEditor;
import ch.elexis.data.Prescription;

public class FixMediBemerkungenEditingSupport extends EditingSupport {

	private TableViewer tableViewer;
	private Map ext;
	
	public FixMediBemerkungenEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}

	public FixMediBemerkungenEditingSupport(TableViewer tv) {
		super(tv);
		this.tableViewer = tv;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new PropertyTextCellEditor(tableViewer, 0);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		ext = ((Prescription) element)
				.getMap(Prescription.FLD_EXTINFO);
		return (String) ext.get(FixMediDisplay.BEMERKUNGEN_EINNAHMELISTE);
	}

	@Override
	protected void setValue(Object element, Object value) {
		String newValue = (String) value;
		ext.put(FixMediDisplay.BEMERKUNGEN_EINNAHMELISTE, newValue);
		((Prescription) element).setMap(Prescription.FLD_EXTINFO, ext);
		tableViewer.refresh(element);
	}

}

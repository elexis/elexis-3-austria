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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Text;

import at.medevit.elexis.at.rezepte.ui.PropertyTextCellEditor;
import ch.elexis.data.Prescription;

public class FixMediSignaturEditingSupport extends EditingSupport {
	
	private TableViewer tv;
	
	public FixMediSignaturEditingSupport(ColumnViewer viewer){
		super(viewer);
	}
	
	public FixMediSignaturEditingSupport(TableViewer tv){
		super(tv);
		this.tv = tv;
	}
	
	@Override
	protected CellEditor getCellEditor(Object element){
		PropertyTextCellEditor ptce = new PropertyTextCellEditor(tv, 0);
		((Text) ptce.getControl()).setTextLimit(10);
		return ptce;
	}
	
	@Override
	protected boolean canEdit(Object element){
		return true;
	}
	
	@Override
	protected Object getValue(Object element){
		return ((Prescription) element).getDosis();
	}
	
	@Override
	protected void setValue(Object element, Object value){
		((Prescription) element).setDosis((String) value);
		tv.refresh(element);
	}
	
}

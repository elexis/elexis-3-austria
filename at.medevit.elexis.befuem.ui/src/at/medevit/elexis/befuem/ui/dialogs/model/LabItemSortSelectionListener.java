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
package at.medevit.elexis.befuem.ui.dialogs.model;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;

public class LabItemSortSelectionListener extends SelectionAdapter {
	private int index;
	private TableColumn column;
	private TableViewer viewer;
	
	public LabItemSortSelectionListener(int idx, TableColumn col, TableViewer view) {
		index = idx;
		column = col;
		viewer = view;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		((LabItemComparator)viewer.getComparator()).setColumn(index);
		int dir = viewer.getTable().getSortDirection();
		if (viewer.getTable().getSortColumn() == column) {
			dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
		} else {
			dir = SWT.DOWN;
		}
		viewer.getTable().setSortDirection(dir);
		viewer.getTable().setSortColumn(column);
		viewer.refresh();
	}
}

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

import ch.elexis.data.LabItem;

public class LabItemRefFemaleEditingSupport extends LabItemTextEditingSupport {

	public LabItemRefFemaleEditingSupport(TableViewer viewer) {
		super(viewer);
	}

	@Override
	protected Object getValue(Object element) {
		return ((LabItem)element).getRefW();
	}

	@Override
	protected void setValue(Object element, Object value) {
		((LabItem)element).setRefW((String)value);
		viewer.refresh();
	}
}

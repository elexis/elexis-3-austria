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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import ch.elexis.data.LabItem;

public class LabItemComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public static final int PROPERTY_NAME = 0;
	public static final int PROPERTY_SHORTNAME = 1;
	public static final int PROPERTY_UNIT = 2;
	public static final int PROPERTY_REFM = 3;
	public static final int PROPERTY_REFW = 4;
	
	public LabItemComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		LabItem i1 = (LabItem) e1;
		LabItem i2 = (LabItem) e2;
		int rc = 0;
		if(i1 != null && i2 != null) {
		switch (propertyIndex) {
			case PROPERTY_NAME:
				rc = i1.getName().compareTo(i2.getName());
				break;
			case PROPERTY_SHORTNAME:
				rc = i1.getKuerzel().compareTo(i2.getKuerzel());
				break;
			case PROPERTY_UNIT:
				rc = i1.getEinheit().compareTo(i2.getEinheit());
				break;
			case PROPERTY_REFM:
				rc = i1.getRefM().compareTo(i2.getRefM());
				break;
			case PROPERTY_REFW:
				rc = i1.getRefW().compareTo(i2.getRefW());
				break;		
			default:
				rc = 0;
			}
			// If descending order, flip the direction
			if (direction == DESCENDING) {
				rc = -rc;
			}
		}
		return rc;
	}
}

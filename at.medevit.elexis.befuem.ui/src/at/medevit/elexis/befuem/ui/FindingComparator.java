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
package at.medevit.elexis.befuem.ui;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;

public class FindingComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public static final int PROPERTY_SENDER = 0;
	public static final int PROPERTY_TYP = 1;
	public static final int PROPERTY_NAME = 2;
	public static final int PROPERTY_SIZE = 3;
	public static final int PROPERTY_RECEPTION = 4;
	public static final int PROPERTY_RECEIVER = 5;
	
	public FindingComparator() {
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
		NetClientFinding f1 = (NetClientFinding) e1;
		NetClientFinding f2 = (NetClientFinding) e2;
		int rc = 0;
		if(f1 != null && f2 != null) {
		switch (propertyIndex) {
			case PROPERTY_SENDER:
				rc = f1.getSenderName().compareTo(f2.getSenderName());
				break;
			case PROPERTY_TYP:
				rc = f1.getFindingTyp().compareTo(f2.getFindingTyp());
				break;
			case PROPERTY_NAME:
				rc = f1.getFindingName().compareTo(f2.getFindingName());
				break;
			case PROPERTY_SIZE:
				int size1 = (int)f1.getSize();
				int size2 = (int)f2.getSize();
				rc = size1 - size2;
				break;
			case PROPERTY_RECEPTION:
				rc = f1.getReception().compareTo(f2.getReception());
				break;
			case PROPERTY_RECEIVER:
				rc = f1.getReceiverName().compareTo(f2.getReceiverName());
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

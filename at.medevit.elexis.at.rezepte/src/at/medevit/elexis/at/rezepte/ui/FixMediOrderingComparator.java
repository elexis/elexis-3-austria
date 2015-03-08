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
package at.medevit.elexis.at.rezepte.ui;

import java.util.Comparator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import ch.elexis.data.Prescription;

public class FixMediOrderingComparator extends ViewerComparator implements Comparator<Prescription> {
	@Override
	public int compare(Viewer viewer, Object e1, Object e2){
		return compare((Prescription) e1, (Prescription) e2);
	}
	
	@Override
	public int compare(Prescription o1, Prescription o2){
		try {
			int p1 = Integer.parseInt(o1.get(Prescription.FLD_COUNT));
			int p2 = Integer.parseInt(o2.get(Prescription.FLD_COUNT));
			return p1 - p2;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}

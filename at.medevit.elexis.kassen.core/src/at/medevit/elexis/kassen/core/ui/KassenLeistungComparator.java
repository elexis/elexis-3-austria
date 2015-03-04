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
package at.medevit.elexis.kassen.core.ui;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import at.medevit.elexis.kassen.core.model.KassenLeistung;

public class KassenLeistungComparator extends ViewerComparator {

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		KassenLeistung kl1 = (KassenLeistung) e1;
		KassenLeistung kl2 = (KassenLeistung) e2;

		int rc = 0;
		
		if(kl1.isGroup() && kl2.isGroup())
			rc = kl1.getGroup().compareTo(kl2.getGroup());
		else if(!kl1.isGroup() && !kl2.isGroup())
			rc = kl1.getPosition().compareTo(kl2.getPosition());

		return rc;
	}

}

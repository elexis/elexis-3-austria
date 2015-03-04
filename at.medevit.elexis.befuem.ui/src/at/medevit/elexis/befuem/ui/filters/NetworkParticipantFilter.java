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
package at.medevit.elexis.befuem.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.medevit.elexis.befuem.contextservice.networkparticipant.NetworkParticipant;

/**
 * Filter for NetworkParticipantFilter objects.
 * All comparison is done on lower case strings.
 * 
 * @author thomas
 *
 */
public class NetworkParticipantFilter extends ViewerFilter {

	private String searchString;

	public void setSearchText(String s) {
		if(s == null || s.length() == 0)
			searchString = s;
		else
			searchString = ".*" + s.toLowerCase() + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		NetworkParticipant participant = (NetworkParticipant) element;
		String hvNummer = participant.getHVNummer().toLowerCase();
		if (hvNummer != null && hvNummer.matches(searchString)) {
			return true;
		}
		String vorname = participant.getVorname().toLowerCase();
		if (vorname != null && vorname.matches(searchString)) {
			return true;
		}
		String nachname = participant.getNachname().toLowerCase();
		if (nachname != null && nachname.matches(searchString)) {
			return true;
		}
		String email = participant.getBefundEmail().toLowerCase();
		if (email != null && email.matches(searchString)) {
			return true;
		}
		return false;
	}
}
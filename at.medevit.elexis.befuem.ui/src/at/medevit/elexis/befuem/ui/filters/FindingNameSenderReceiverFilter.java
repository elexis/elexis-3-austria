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

import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import ch.elexis.data.Patient;

/**
 * Filter for NetClientFinding objects.
 * All comparison is done on lower case strings.
 * 
 * @author thomas
 *
 */
public class FindingNameSenderReceiverFilter extends ViewerFilter {

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
		NetClientFinding finding = (NetClientFinding) element;
		String senderName = finding.getSenderName().toLowerCase();
		if (senderName != null && senderName.matches(searchString)) {
			return true;
		}
		String receiverName = finding.getReceiverName().toLowerCase();
		if (receiverName != null && receiverName.matches(searchString)) {
			return true;
		}
		if(finding.getElexisFindings() != null && finding.getElexisFindings().size() > 0) {
			ElexisFinding ef = finding.getElexisFindings().get(0);
			if(ef.getPatient() != null) {
				Patient patient = ef.getPatient();
				if(patient.getName().toLowerCase().matches(searchString) ||
						patient.getVorname().toLowerCase().matches(searchString))
					return true;
			} else {
				if(ef.getPatientInfo().getLastname().toLowerCase().matches(searchString) ||
						ef.getPatientInfo().getFirstname().toLowerCase().matches(searchString))
					return true;
			}
		}

		return false;
	}
}
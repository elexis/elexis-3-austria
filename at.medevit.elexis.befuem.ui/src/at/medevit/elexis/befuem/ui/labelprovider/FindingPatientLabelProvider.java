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
package at.medevit.elexis.befuem.ui.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.ui.Messages;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.data.Patient;

public class FindingPatientLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		NetClientFinding finding = (NetClientFinding)element;
		if(finding.getElexisFindings() != null && finding.getElexisFindings().size() > 0) {
			ElexisFinding ef = finding.getElexisFindings().get(0);
			if(ef.getPatient() != null) {
				return ef.getPatient().getLabel();
			} else {
				String text = ef.getPatientInfo().getLastname() + " " + //$NON-NLS-1$
				ef.getPatientInfo().getFirstname() + ", " + //$NON-NLS-1$
				((ef.getPatientInfo().getBirthdate() == null) ? "" : ef.getPatientInfo().getBirthdate()); //$NON-NLS-1$
				return text;
			}
		} else {
			return Messages.FindingPatientLabelProvider_UnknownPatient;
		}
	}
	
	@Override
	public Image getImage(Object element) {
		NetClientFinding finding = (NetClientFinding)element;
		if(finding.getElexisFindings() != null && finding.getElexisFindings().size() > 0) {
			ElexisFinding ef = finding.getElexisFindings().get(0);
			Patient patient = ef.getPatient();
			if(patient != null) {
				if(patient.getGeschlecht().equalsIgnoreCase("m")) //$NON-NLS-1$
					return Images.IMG_MANN.getImage();
				else if (patient.getGeschlecht().equalsIgnoreCase("w")) //$NON-NLS-1$
					return Images.IMG_FRAU.getImage();
			}
		}
		
		return null;
	}
}

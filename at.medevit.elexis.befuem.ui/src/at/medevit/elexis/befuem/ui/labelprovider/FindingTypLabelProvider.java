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

import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Typ;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.ui.Messages;
import ch.elexis.core.ui.icons.Images;

import com.swtdesigner.ResourceManager;

public class FindingTypLabelProvider extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		NetClientFinding finding = (NetClientFinding)element;
		Typ typ = finding.getFindingTyp();
		if(typ == Typ.LAB) {	
			return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/labor_view.png"); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (typ == Typ.FINDING) {
			return Images.IMG_MAIL.getImage();
		} else if (typ == Typ.FINDING_REQ) {
			return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/mail_warning.png"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/warning.png"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public String getText(Object element) {
		NetClientFinding finding = (NetClientFinding)element;
		Typ typ = finding.getFindingTyp();
		if(typ == Typ.LAB) {	
			return Messages.FindingTypLabelProvider_TypLab;
		} else if (typ == Typ.FINDING) {
			return Messages.FindingTypLabelProvider_TypText;
		} else if (typ == Typ.FINDING_REQ) {
			return Messages.FindingTypLabelProvider_TypReq;
		} else {
			return Messages.FindingTypLabelProvider_TypUnknown;
		}
	}
}

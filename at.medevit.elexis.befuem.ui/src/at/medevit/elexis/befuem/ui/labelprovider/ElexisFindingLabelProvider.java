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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFindingContent.Typ;
import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.LabResultTest;
import at.medevit.elexis.befuem.contextservice.finding.TextContent;
import ch.elexis.core.ui.icons.Images;

import com.swtdesigner.ResourceManager;

public class ElexisFindingLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if(element instanceof ElexisFinding) {
			ElexisFinding finding = (ElexisFinding)element;
			Typ typ = finding.getContent().get(0).getTyp();
			if(typ == Typ.TEXT) {
				return Images.IMG_MAIL.getImage();
			} else if (typ == Typ.LAB) {
				if(finding.getUnresolvedLabResults().size() > 0) {
					return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/labor_view_unknown.png"); //$NON-NLS-1$ //$NON-NLS-2$					
				} else {
					return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/labor_view.png"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}  else {
				return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/warning.png"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if (element instanceof TextContent) {
			return Images.IMG_MAIL.getImage();
		} else if (element instanceof LabResultTest) {
			return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/labor_view_unknown.png"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/warning.png"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public String getText(Object element) {
		if(element instanceof ElexisFinding) {
			ElexisFinding finding = (ElexisFinding)element;
			return finding.getDescription();
		} else if (element instanceof TextContent) {
			TextContent content = (TextContent)element;
			return content.getDescription();
		} else if (element instanceof LabResultTest) {
			LabResultTest content = (LabResultTest)element;
			return content.getDescription();
		} else {
			return null;
		}
	}

}

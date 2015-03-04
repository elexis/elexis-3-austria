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

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.netservice.INetClient.Location;

import com.swtdesigner.ResourceManager;

public class ArchiveFindingInOutLabelProvider extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		NetClientFinding finding = (NetClientFinding)element;
		Location loc = finding.getLocation();
		if(loc == Location.ARCHIVE_INBOX) {	
			return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/inbox_into.png"); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (loc == Location.ARCHIVE_OUTBOX) {
			return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/outbox_out.png"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return ResourceManager.getPluginImage("at.medevit.elexis.befuem.ui", "rsc/outbox.png"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public String getText(Object element) {
		return ""; //$NON-NLS-1$
	}
}

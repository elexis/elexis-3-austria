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

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;

public class FindingSizeLabelProvider extends ColumnLabelProvider {
	
	@Override
	public String getText(Object element) {
		NetClientFinding finding = (NetClientFinding)element;
		return getDisplayFileSize(finding.getSize());
	}
	
	String getDisplayFileSize(long size) {
		if(size < 1024) {
			return String.valueOf(size) + " B"; //$NON-NLS-1$
		} else if(size < 1024*1024) {
			int kbSize = (int)size/1024;
			return String.valueOf(kbSize) + " KB"; //$NON-NLS-1$
		} else if(size < 1024*1024*1024) {
			int mbSize = (int)size/(1024*1024);
			return String.valueOf(mbSize) + " MB"; //$NON-NLS-1$
		} else {
			int gbSize = (int)size/(1024*1024*1024);
			return String.valueOf(gbSize) + " GB"; //$NON-NLS-1$
		}
	}
}

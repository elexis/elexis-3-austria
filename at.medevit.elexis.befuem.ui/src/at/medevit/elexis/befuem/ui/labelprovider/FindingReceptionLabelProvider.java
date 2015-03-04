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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;


public class FindingReceptionLabelProvider  extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		NetClientFinding finding = (NetClientFinding)element;
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
		Date reception = finding.getReception();
		if(reception != null)
			return formatter.format(reception);
		else
			return ""; //$NON-NLS-1$
	}
}

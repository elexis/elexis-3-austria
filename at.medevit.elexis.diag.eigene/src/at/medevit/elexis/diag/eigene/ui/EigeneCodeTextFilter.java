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
package at.medevit.elexis.diag.eigene.ui;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.medevit.elexis.diag.eigene.model.Diagnose;

public class EigeneCodeTextFilter extends ViewerFilter {
	
	private String searchString;
	
	public void setSearchText(String s){
		if (s == null || s.length() == 0)
			searchString = s;
		else
			searchString = ".*" + s.toLowerCase() + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element){
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		Diagnose diag = (Diagnose) element;
		
		String code = diag.getCode().toLowerCase();
		if (code != null && code.matches(searchString)) {
			return true;
		}
		
		String text = diag.getText().toLowerCase();
		if (text != null && text.matches(searchString)) {
			return true;
		}
		
		return false;
	}
}

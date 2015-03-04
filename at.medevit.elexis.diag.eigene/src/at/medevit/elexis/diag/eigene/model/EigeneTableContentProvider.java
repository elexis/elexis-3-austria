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
package at.medevit.elexis.diag.eigene.model;

import java.util.HashMap;

import org.eclipse.jface.viewers.Viewer;

import ch.elexis.core.ui.util.viewers.ViewerConfigurer.ICommonViewerContentProvider;

public class EigeneTableContentProvider implements ICommonViewerContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		return Diagnose.getAll().toArray();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changed(HashMap<String, String> values) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reorder(String field) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startListening() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopListening() {
		// TODO Auto-generated method stub

	}

}

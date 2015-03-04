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
package at.medevit.elexis.kassen.core.ui;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.medevit.elexis.kassen.core.model.KassenLeistung;
import ch.elexis.core.ui.util.viewers.ViewerConfigurer.ICommonViewerContentProvider;

/**
 * Generic {@link ITreeContentProvider} implementation for displaying KassenLeistung subclasses in a TreeViewer.
 * Also implements {@link ICommonViewerContentProvider} for usage with the {@link ch.elexis.util.viewers.CommonViewer} system.
 * 
 * @author thomas
 *
 */
public class KassenLeistungTreeContentProvider implements ITreeContentProvider, ICommonViewerContentProvider{

	Class<? extends KassenLeistung> clazz;
	List<KassenLeistung> rootLeistungen;
	
	public KassenLeistungTreeContentProvider(Class<? extends KassenLeistung> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if(newInput instanceof KassenLeistung)
			this.rootLeistungen = (List<KassenLeistung>)newInput;
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

	@Override
	public Object[] getElements(Object inputElement) {
		if(rootLeistungen != null) {
			return rootLeistungen.toArray();
		} else if (clazz != null) {
			return KassenLeistung.getCurrentRootLeistungen(clazz).toArray();
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement != null && parentElement instanceof KassenLeistung) {
			KassenLeistung leistung = (KassenLeistung)parentElement;
			if(leistung.isGroup()) {
				return leistung.getChildren().toArray();
			}
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		KassenLeistung leistung = (KassenLeistung)element;
		return leistung.isGroup();
	}

	@Override
	public void changed(HashMap<String, String> values) {
		// TODO Auto-generated method stub
		
	}

}

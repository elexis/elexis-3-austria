/*******************************************************************************
 * Copyright (c) 2009-2010, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    
 *******************************************************************************/

package ch.elexis.artikel_at.model;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;

import ch.elexis.artikel_at.data.Medikament;
import ch.elexis.artikel_at.views.MedikamentSelector2;
import ch.elexis.core.ui.selectors.SelectorPanel;
import ch.elexis.core.ui.util.viewers.SelectorPanelProvider;
import ch.elexis.core.ui.util.viewers.ViewerConfigurer.ICommonViewerContentProvider;
import ch.elexis.data.Query;

public class ContentProvider implements ICommonViewerContentProvider {
	MedikamentSelector2 msl;
	Query<Medikament> qMedi = new Query<Medikament>(Medikament.class);
	
	public ContentProvider(MedikamentSelector2 mine){
		msl = mine;
	}
	
	public void startListening(){
		msl.getConfigurer().getControlFieldProvider().addChangeListener(this);
		
	}
	
	public void stopListening(){
		msl.getConfigurer().getControlFieldProvider().removeChangeListener(this);
	}
	
	public Object[] getElements(Object inputElement){
		SelectorPanelProvider slp =
			(SelectorPanelProvider) msl.getConfigurer().getControlFieldProvider();
		SelectorPanel panel = slp.getPanel();
		HashMap<String, String> values = panel.getValues();
		String m = values.get(msl.SELECT_NAME);
		qMedi.clear();
		
		if (m.length() > 0) {
			qMedi.add("Name", "LIKE", m + "%", true);
		}
		qMedi.orderBy(false, "Name");
		List<Medikament> list = qMedi.execute();
		for (Medikament medikament : list) {
			Medikament.load(medikament.getId());
		}
		return list.toArray();
	}
	
	public void dispose(){
		stopListening();
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		// TODO Auto-generated method stub
		
	}
	
	public void changed(HashMap<String, String> vars){
		msl.reload();
	}
	
	public void reorder(String field){
		msl.reload();
		
	}
	
	public void selected(){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void init(){
		// TODO Auto-generated method stub
		
	}
}

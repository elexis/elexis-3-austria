/*******************************************************************************
 * Copyright (c) 2009, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    
 *******************************************************************************/

package ch.elexis.artikel_at.views;

import org.eclipse.swt.SWT;

import ch.elexis.artikel_at.data.Medikament;
import ch.elexis.artikel_at.model.ContentProvider;
import ch.elexis.core.ui.selectors.FieldDescriptor;
import ch.elexis.core.ui.util.viewers.CommonViewer;
import ch.elexis.core.ui.util.viewers.SelectorPanelProvider;
import ch.elexis.core.ui.util.viewers.SimpleWidgetProvider;
import ch.elexis.core.ui.util.viewers.ViewerConfigurer;
import ch.elexis.core.ui.views.codesystems.CodeSelectorFactory;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.PersistentObjectFactory;

public class MedikamentSelector2 extends CodeSelectorFactory {
	SelectorPanelProvider slp;
	public static final String SELECT_NAME = "Name";
	// public static final String SELECT_SUBSTANCE = "Substanz";
	// public static final String SELECT_NOTE = "Notiz";
	FieldDescriptor<?>[] fields = {
		new FieldDescriptor<Medikament>(SELECT_NAME),
	// new FieldDescriptor<Medikament>(SELECT_SUBSTANCE),
	// new FieldDescriptor<Medikament>(SELECT_NOTE),
		};
	private CommonViewer cv;
	
	@Override
	public ViewerConfigurer createViewerConfigurer(CommonViewer cv){
		this.cv = cv;
		new MedikamentContextMenu(
			(Medikament) new PersistentObjectFactory().createTemplate(Medikament.class), cv);
		
		slp = new SelectorPanelProvider(fields, true);
		ContentProvider cp = new ContentProvider(this);
		ViewerConfigurer vc =
			new ViewerConfigurer(cp, new VidalLabelProvider(), new SimpleWidgetProvider(
				SimpleWidgetProvider.TYPE_TABLE, SWT.NONE, cv));
		vc.setControlFieldProvider(slp);
		
		return vc;
	}
	
	public void reload(){
		cv.notify(CommonViewer.Message.update);
	}
	
	public ViewerConfigurer getConfigurer(){
		return cv.getConfigurer();
	}
	
	@Override
	public void dispose(){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getCodeSystemName(){
		return Medikament.CODESYSTEMNAME;
	}
	
	@Override
	public Class<? extends PersistentObject> getElementClass(){
		return Medikament.class;
	}
	
}

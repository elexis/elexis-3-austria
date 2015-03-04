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
package at.medevit.elexis.kassen.bva;

import org.eclipse.swt.SWT;

import at.medevit.elexis.kassen.bva.model.BvaLeistung;
import at.medevit.elexis.kassen.core.ui.KassenLeistungControlFieldProvider;
import at.medevit.elexis.kassen.core.ui.KassenLeistungLabelProvider;
import at.medevit.elexis.kassen.core.ui.KassenLeistungTreeContentProvider;
import ch.elexis.core.ui.util.viewers.CommonViewer;
import ch.elexis.core.ui.util.viewers.SelectorPanelProvider;
import ch.elexis.core.ui.util.viewers.SimpleWidgetProvider;
import ch.elexis.core.ui.util.viewers.ViewerConfigurer;
import ch.elexis.core.ui.views.codesystems.CodeSelectorFactory;

public class BvaCodeSelectorFactory extends CodeSelectorFactory {
	SelectorPanelProvider slp;
	CommonViewer cv;	
	public BvaCodeSelectorFactory(){

	}
	
	@Override
	public ViewerConfigurer createViewerConfigurer(CommonViewer cv){
		this.cv = cv;
		ViewerConfigurer vc =
			new ViewerConfigurer(new KassenLeistungTreeContentProvider(BvaLeistung.class),
					new KassenLeistungLabelProvider(), new KassenLeistungControlFieldProvider(cv),
					new ViewerConfigurer.DefaultButtonProvider(), new SimpleWidgetProvider(
					SimpleWidgetProvider.TYPE_TREE, SWT.NONE, null));
		return vc;
	}
	
	@Override
	public Class getElementClass(){
		return BvaLeistung.class;
	}
	
	@Override
	public void dispose(){
		cv.dispose();
	}
	
	@Override
	public String getCodeSystemName(){
		return "BVA"; //$NON-NLS-1$
	}
}
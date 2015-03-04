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
package at.medevit.elexis.kassen.svb;

import org.eclipse.swt.SWT;

import at.medevit.elexis.kassen.core.ui.KassenLeistungControlFieldProvider;
import at.medevit.elexis.kassen.core.ui.KassenLeistungLabelProvider;
import at.medevit.elexis.kassen.core.ui.KassenLeistungTreeContentProvider;
import at.medevit.elexis.kassen.svb.model.SvbLeistung;
import ch.elexis.core.data.events.ElexisEvent;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.core.ui.events.ElexisUiEventListenerImpl;
import ch.elexis.core.ui.util.viewers.CommonViewer;
import ch.elexis.core.ui.util.viewers.SelectorPanelProvider;
import ch.elexis.core.ui.util.viewers.SimpleWidgetProvider;
import ch.elexis.core.ui.util.viewers.ViewerConfigurer;
import ch.elexis.core.ui.views.codesystems.CodeSelectorFactory;

public class SvbCodeSelectorFactory extends CodeSelectorFactory {
	SelectorPanelProvider slp;
	CommonViewer cv;
	
	public SvbCodeSelectorFactory(){
		
	}
	
	@Override
	public ViewerConfigurer createViewerConfigurer(CommonViewer cv){
		this.cv = cv;
		ViewerConfigurer vc =
			new ViewerConfigurer(new KassenLeistungTreeContentProvider(SvbLeistung.class),
				new KassenLeistungLabelProvider(), new KassenLeistungControlFieldProvider(cv),
				new ViewerConfigurer.DefaultButtonProvider(), new SimpleWidgetProvider(
					SimpleWidgetProvider.TYPE_TREE, SWT.NONE, null));
		
		ElexisEventDispatcher.getInstance().addListeners(
			new UpdateEventListener(cv, SvbLeistung.class, ElexisEvent.EVENT_RELOAD));
		
		return vc;
	}
	
	@Override
	public Class getElementClass(){
		return SvbLeistung.class;
	}
	
	@Override
	public void dispose(){
		cv.dispose();
	}
	
	@Override
	public String getCodeSystemName(){
		return "SVB"; //$NON-NLS-1$
	}
	
	private class UpdateEventListener extends ElexisUiEventListenerImpl {
		
		CommonViewer viewer;
		
		UpdateEventListener(CommonViewer viewer, final Class<?> clazz, int mode){
			super(clazz, mode);
			this.viewer = viewer;
		}
		
		@Override
		public void runInUi(ElexisEvent ev){
			viewer.notify(CommonViewer.Message.update);
		}
	}
}

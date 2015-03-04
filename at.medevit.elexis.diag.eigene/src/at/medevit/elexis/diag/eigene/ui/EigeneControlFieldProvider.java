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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

import ch.elexis.core.ui.UiDesk;
import ch.elexis.core.ui.icons.Images;
import ch.elexis.core.ui.util.viewers.CommonViewer;
import ch.elexis.core.ui.util.viewers.ViewerConfigurer.ControlFieldListener;
import ch.elexis.core.ui.util.viewers.ViewerConfigurer.ControlFieldProvider;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.rgw.tools.IFilter;


public class EigeneControlFieldProvider implements ControlFieldProvider {
	
	private CommonViewer commonViewer;
	private StructuredViewer viewer;
	
	private Text txtFilter;
	
	private EigeneCodeTextFilter filterCodeText;
	
	public EigeneControlFieldProvider(final CommonViewer viewer){
		commonViewer = viewer;
	}
	
	@Override
	public Composite createControl(Composite parent){
		Composite ret = new Composite(parent, SWT.NONE);
		ret.setLayout(new FormLayout());
		
		Label lblFilter = new Label(ret, SWT.NONE);
		lblFilter.setText("Filter: ");
		
		txtFilter = new Text(ret, SWT.BORDER | SWT.SEARCH);
		txtFilter.setText(""); //$NON-NLS-1$
		
		ToolBarManager tbManager = new ToolBarManager(SWT.FLAT | SWT.HORIZONTAL | SWT.WRAP);
		tbManager.add(new Action("neu erstellen") {
			{
				setImageDescriptor(Images.IMG_NEW.getImageDescriptor());
				setToolTipText("Neue Diagnose erstellen");
			}
			
			@Override
			public void run(){
				EditEigeneDialog dialog = new EditEigeneDialog(UiDesk.getTopShell(), null);
				if (dialog.open() == EditEigeneDialog.OK)
					viewer.refresh();
			}
		});
		ToolBar toolbar = tbManager.createControl(ret);
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 5);
		lblFilter.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		toolbar.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(lblFilter, 5);
		fd.right = new FormAttachment(toolbar, -5);
		txtFilter.setLayoutData(fd);
		
		return ret;
	}
	
	@Override
	public String[] getValues(){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void clearValues(){
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean isEmpty(){
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setQuery(Query<? extends PersistentObject> q){
		// TODO Auto-generated method stub
	}
	
	@Override
	public IFilter createFilter(){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void fireChangedEvent(){
		// TODO Auto-generated method stub
	}
	
	@Override
	public void fireSortEvent(String text){
		// TODO Auto-generated method stub
	}
	
	@Override
	public void setFocus(){
		// apply filter to viewer on focus as the creation in common viewer is done
		// first filter then viewer -> viewer not ready on createControl.
		if (viewer == null) {
			viewer = commonViewer.getViewerWidget();
// viewer.setComparator(new KassenLeistungComparator());
			filterCodeText = new EigeneCodeTextFilter();
			viewer.addFilter(filterCodeText);
			txtFilter.addKeyListener(new FilterKeyListener(txtFilter, viewer));
		}
	}
	
	private class FilterKeyListener extends KeyAdapter {
		private Text text;
		private StructuredViewer viewer;
		
		FilterKeyListener(Text filterTxt, StructuredViewer viewer){
			text = filterTxt;
			this.viewer = viewer;
		}
		
		public void keyReleased(KeyEvent ke){
			String txt = text.getText();
			if (txt.length() > 1) {
				filterCodeText.setSearchText(txt);
				viewer.getControl().setRedraw(false);
				viewer.refresh();
				viewer.getControl().setRedraw(true);
			} else {
				filterCodeText.setSearchText(null);
				viewer.getControl().setRedraw(false);
				viewer.refresh();
				viewer.getControl().setRedraw(true);
			}
		}
	}

	@Override
	public void addChangeListener(ControlFieldListener cl){
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeChangeListener(ControlFieldListener cl){
		// TODO Auto-generated method stub
		
	}
}

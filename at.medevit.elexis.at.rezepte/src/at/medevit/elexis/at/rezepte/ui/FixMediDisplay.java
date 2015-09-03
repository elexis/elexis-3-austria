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
package at.medevit.elexis.at.rezepte.ui;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import at.medevit.elexis.at.rezepte.model.FixMediContentProvider;
import at.medevit.elexis.at.rezepte.ui.dnd.FixMediDisplayDragListener;
import at.medevit.elexis.at.rezepte.ui.dnd.FixMediDisplayDropAdapter;
import at.medevit.elexis.at.rezepte.ui.editors.FixMediBemerkungenEditingSupport;
import at.medevit.elexis.at.rezepte.ui.editors.FixMediEinnahmevorschriftEditingSupport;
import at.medevit.elexis.at.rezepte.ui.editors.FixMediSignaturEditingSupport;
import ch.elexis.core.data.events.ElexisEvent;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.core.ui.events.ElexisUiEventListenerImpl;
import ch.elexis.data.Patient;

public class FixMediDisplay extends ViewPart implements ITabbedPropertySheetPageContributor {	
	public static final String ID = "at.medevit.elexis.at.rezepte.ui.FixMediDisplay";
	public static final String BEMERKUNGEN_EINNAHMELISTE = "BemerkungenEinnahmeliste";
	private Table tableFixMedi;

	private ElexisUiEventListenerImpl eeli_pat;
	private TableViewer tableViewerFixMedi;
	
	public FixMediDisplay() {
		eeli_pat = new ElexisUiEventListenerImpl(Patient.class) {
			public void runInUi(ElexisEvent ev) {
				reload();
			}
		};
		ElexisEventDispatcher.getInstance().addListeners(eeli_pat);
	}

	@Override
	public void createPartControl(Composite parent) {		
		Composite composite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);
		
		tableViewerFixMedi = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tableFixMedi = tableViewerFixMedi.getTable();
		tableFixMedi.setLinesVisible(true);
		tableFixMedi.setHeaderVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewerFixMedi, SWT.NONE);
		TableColumn tableColName = tableViewerColumn.getColumn();
		tableColName.setText("Name");
		tableColumnLayout.setColumnData(tableColName, new ColumnWeightData(34, 100));
		
		TableViewerColumn tableViewerColumnSignatur = new TableViewerColumn(tableViewerFixMedi, SWT.NONE);
		TableColumn tblclmnSignatur = tableViewerColumnSignatur.getColumn();
		tblclmnSignatur.setText("Signatur");
		tableViewerColumnSignatur.setEditingSupport(new FixMediSignaturEditingSupport(tableViewerFixMedi));
		tableColumnLayout.setColumnData(tblclmnSignatur, new ColumnWeightData(16, 50));
		
		TableViewerColumn tableViewerColumnEinnahmevorschrift = new TableViewerColumn(tableViewerFixMedi, SWT.NONE);
		TableColumn tableColEinnahmevorschr = tableViewerColumnEinnahmevorschrift.getColumn();
		tableColEinnahmevorschr.setText("Einnahmevorschrift");
		tableViewerColumnEinnahmevorschrift.setEditingSupport(new FixMediEinnahmevorschriftEditingSupport(tableViewerFixMedi));
		tableColumnLayout.setColumnData(tableColEinnahmevorschr, new ColumnWeightData(25, 100));
		
		TableViewerColumn tableViewerColumnBemerkungen = new TableViewerColumn(tableViewerFixMedi, SWT.NONE);
		TableColumn tblclmnBemerkungen = tableViewerColumnBemerkungen.getColumn();
		tblclmnBemerkungen.setToolTipText("Bemerkungen zur Anzeige auf Einnahmeliste.");
		tblclmnBemerkungen.setText("Bemerkungen");
		tableViewerColumnBemerkungen.setEditingSupport(new FixMediBemerkungenEditingSupport(tableViewerFixMedi));
		tableColumnLayout.setColumnData(tblclmnBemerkungen, new ColumnWeightData(25, 100));
		
		tableViewerFixMedi.setLabelProvider(new FixMediDisplayLabelProvider());
		tableViewerFixMedi.setContentProvider(new FixMediContentProvider());
		tableViewerFixMedi.setComparator(new FixMediOrderingComparator());

		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance() };
		tableViewerFixMedi.addDragSupport(operations, transferTypes, new FixMediDisplayDragListener(tableViewerFixMedi));
		tableViewerFixMedi.addDropSupport(operations, transferTypes, new FixMediDisplayDropAdapter(tableViewerFixMedi));	
		
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(tableFixMedi);
		tableFixMedi.setMenu(menu);
		getSite().registerContextMenu(menuManager, tableViewerFixMedi);
		getSite().setSelectionProvider(tableViewerFixMedi);
	}

	@Override
	public void setFocus() {
		Patient act=ElexisEventDispatcher.getSelectedPatient();
		if(act!=null) tableViewerFixMedi.setInput(act.getFixmedikation());
	}
	
	public void reload() {
		Patient act = ElexisEventDispatcher.getSelectedPatient();
		tableViewerFixMedi.setInput(act.getFixmedikation());
	}

	@Override
	public void dispose() {
		super.dispose();
		tableFixMedi.dispose();
		ElexisEventDispatcher.getInstance().removeListeners(eeli_pat);
	}

	public TableViewer getTableViewerFixMedi() {
		return tableViewerFixMedi;
	}

	@Override
	public String getContributorId(){
		return getSite().getId();
	}

	@Override
	public Object getAdapter(Class adapter){
		if (adapter == IPropertySheetPage.class)
            return new TabbedPropertySheetPage(this);
        return super.getAdapter(adapter);
	}
	
}

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
package at.medevit.elexis.kassen.privat.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import at.medevit.elexis.kassen.privat.Activator;
import at.medevit.elexis.kassen.privat.model.PrivatKasse;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

public class PrivatPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	TableViewer viewer;
	
	@Override
	public void init(IWorkbench workbench){
		setPreferenceStore(new SettingsPreferenceStore(CoreHub.globalCfg, Activator.PLUGIN_ID));
		setDescription("Einstellungen zu privat Versicherungen. Der konfigurierte Zuschlag wird pro verrechneter Leistung aufgeschlagen.");
	}
	
	@Override
	protected Control createContents(final Composite parent){
		// create the field editors by calling super
		super.createContents(parent);
		// create a composite
		Composite tableParent = new Composite(parent, SWT.NULL);
		tableParent.setLayout(new FormLayout());
		Label titel = new Label(tableParent, SWT.NONE);
		titel.setText("Private Krankenkassen:");
		
		viewer = new TableViewer(tableParent, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 0);
		titel.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(titel, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(100, -5);
		viewer.getControl().setLayoutData(fd);
		
		// create a table viewer
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText("Name");
		column.setWidth(100);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				PrivatKasse kasse = (PrivatKasse) element;
				return kasse.getName();
			}
		});
		
		viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("Kataloge");
		column.setWidth(200);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				PrivatKasse kasse = (PrivatKasse) element;
				return kasse.getCatalogsAsString();
			}
		});
		
		viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("Zuschlag");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				PrivatKasse kasse = (PrivatKasse) element;
				StringBuilder sb = new StringBuilder();
				sb.append(kasse.getValueAsString());
				sb.append(" ");
				sb.append(kasse.getValueType().getPostfix());
				return sb.toString();
			}
		});
		
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(PrivatKasse.getAll());
		
		// First we create a menu Manager
		MenuManager menuManager = new MenuManager();
		// Then add the actions for PointsArea
		menuManager.add(new Action() {
			@Override
			public String getText(){
				return "add";
			}
			
			@Override
			public void run(){
				EditPrivatKasseDialog dialog = new EditPrivatKasseDialog(parent.getShell(), null);
				dialog.create();
				if (dialog.open() == Window.OK) {
					viewer.setInput(PrivatKasse.getAll());
					viewer.refresh();
				}
			}
		});
		
		menuManager.add(new Action() {
			@Override
			public String getText(){
				return "edit";
			}
			
			@Override
			public void run(){
				ISelection selection = viewer.getSelection();
				if (selection != null & selection instanceof IStructuredSelection) {
					IStructuredSelection strucSelection = (IStructuredSelection) selection;
					PrivatKasse sel = (PrivatKasse) strucSelection.getFirstElement();
					
					EditPrivatKasseDialog dialog =
						new EditPrivatKasseDialog(parent.getShell(), sel);
					dialog.create();
					if (dialog.open() == Window.OK) {
						viewer.setInput(PrivatKasse.getAll());
						viewer.refresh();
					}
				}
			}
		});
		
		// set the menu for the table viewer
		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		
		return tableParent;
	}
	
	@Override
	protected void createFieldEditors(){
		// TODO Auto-generated method stub
		
	}
}

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
package at.medevit.elexis.kassen.sva.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.IPointsArea;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.KassenCodes.InsuranceCategory;
import at.medevit.elexis.kassen.core.model.PointsAreaFactory;
import at.medevit.elexis.kassen.core.ui.EditCatalogDialog;
import at.medevit.elexis.kassen.core.ui.EditPointsAreaDialog;
import at.medevit.elexis.kassen.core.ui.PointsAreaStyledCellLabelProvider;
import at.medevit.elexis.kassen.core.ui.PointsAreaStyledCellLabelProvider.ContentTyp;
import at.medevit.elexis.kassen.sva.Activator;
import at.medevit.elexis.kassen.sva.model.SvaLeistung;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.dialogs.KontaktSelektor;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Kontakt;

public class SvaPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	TableViewer viewer;
	Text contactTxt;
	
	@Override
	public void init(IWorkbench workbench){
		setPreferenceStore(new SettingsPreferenceStore(CoreHub.globalCfg, Activator.PLUGIN_ID));
		setDescription("Einstellungen zur Sozialversicherungsanstalt der gewerblichen Wirtschaft");
	}
	
	@Override
	protected Control createContents(final Composite parent){
		// create the field editors by calling super
		super.createContents(parent);
		// create a composite for selecting the contact of the bva
		Composite contactParent = new Composite(parent, SWT.NULL);
		contactParent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		contactParent.setLayout(new FormLayout());
		Label contactLbl = new Label(contactParent, SWT.NONE);
		contactLbl.setText("Kontakt der SVA:");
		contactTxt = new Text(contactParent, SWT.BORDER);
		contactTxt.setEditable(false);
		String svaContactId =
			getPreferenceStore().getString(
				SvaPreferenceInitializer.SVAPREF + CorePreferenceConstants.KASSE_CONTACT);
		if (svaContactId.length() > 1) {
			Kontakt svaContact = Kontakt.load(svaContactId);
			contactTxt.setText(svaContact.getLabel());
		}
		Button contactBtn = new Button(contactParent, SWT.PUSH);
		contactBtn.setText("Kontakt");
		contactBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				KontaktSelektor ksl =
					new KontaktSelektor(getShell(), Kontakt.class, "Kontakt auswählen",
						"Kontakt der SVA auswählen, bzw. zuerst anlegen.", false,
						Kontakt.DEFAULT_SORT);
				if (ksl.open() == Dialog.OK) {
					Kontakt sel = (Kontakt) ksl.getSelection();
					getPreferenceStore().setValue(
						SvaPreferenceInitializer.SVAPREF + CorePreferenceConstants.KASSE_CONTACT,
						sel.getId());
					contactTxt.setText(sel.getLabel());
				}
			}
		});
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 0);
		contactLbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(contactLbl, 0, SWT.CENTER);
		fd.left = new FormAttachment(contactLbl, 5);
		fd.right = new FormAttachment(contactBtn, -5);
		contactTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(contactLbl, 0, SWT.CENTER);
		fd.right = new FormAttachment(100, -5);
		contactBtn.setLayoutData(fd);
		
		// create a composite for the PointAreas
		Composite tableParent = new Composite(parent, SWT.NULL);
		tableParent.setLayout(new FormLayout());
		Label titel = new Label(tableParent, SWT.NONE);
		titel.setText("Punktwerte der Leistungen:");
		
		viewer = new TableViewer(tableParent, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		Button openCatalog = new Button(tableParent, SWT.PUSH);
		openCatalog.setText("Leistungskatalog");
		openCatalog.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e){
				EditCatalogDialog dialog =
					new EditCatalogDialog(parent.getShell(), SvaLeistung.class);
				dialog.create();
				dialog.open();
			}
		});
		
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 0);
		titel.setLayoutData(fd);
		
		fd = new FormData();
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(100, -5);
		openCatalog.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(titel, 10);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(openCatalog, -10);
		table.setLayoutData(fd);
		
		// create a table viewer for the PointAreas
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText("Gültig ab");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new PointsAreaStyledCellLabelProvider(
			ContentTyp.COLUMN_VALIDFROM));
		
		viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("Gültig bis");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new PointsAreaStyledCellLabelProvider(
			ContentTyp.COLUMN_VALIDTO));
		
		viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("Bereich");
		column.setWidth(300);
		viewerColumn.setLabelProvider(new PointsAreaStyledCellLabelProvider(
			ContentTyp.COLUMN_AREADEFINITION));
		
		viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("Punktwert");
		column.setWidth(100);
		viewerColumn
			.setLabelProvider(new PointsAreaStyledCellLabelProvider(ContentTyp.COLUMN_VALUE));
		
		try {
			viewer.setContentProvider(new ArrayContentProvider());
			viewer.setInput(PointsAreaFactory.getInstance().getPointsAreasForClass(
				SvaLeistung.class));
			
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
					EditPointsAreaDialog dialog =
						new EditPointsAreaDialog(parent.getShell(), SvaLeistung.class);
					dialog.create();
					if (dialog.open() == Window.OK) {
						viewer.setInput(PointsAreaFactory.getInstance().getPointsAreasForClass(
							SvaLeistung.class));
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
						if(strucSelection==null) {
							return;
						}
						IPointsArea sel = (IPointsArea) strucSelection.getFirstElement();
						
						EditPointsAreaDialog dialog =
							new EditPointsAreaDialog(parent.getShell(), sel, SvaLeistung.class);
						dialog.create();
						if (dialog.open() == Window.OK) {
							viewer.setInput(PointsAreaFactory.getInstance().getPointsAreasForClass(
								SvaLeistung.class));
							viewer.refresh();
						}
					}
				}
			});
			menuManager.add(new Action() {
				@Override
				public String getText(){
					return "enable/disable";
				}
				
				@Override
				public void run(){
					ISelection selection = viewer.getSelection();
					if (selection != null & selection instanceof IStructuredSelection) {
						IStructuredSelection strucSelection = (IStructuredSelection) selection;
						if(strucSelection==null) {
							return;
						}
						IPointsArea sel = (IPointsArea) strucSelection.getFirstElement();
						if (sel.isEnabled())
							sel.setIsEnabled(false);
						else
							sel.setIsEnabled(true);
						viewer.setInput(PointsAreaFactory.getInstance().getPointsAreasForClass(
							SvaLeistung.class));
						viewer.refresh();
					}
				}
			});
			
			// set the menu for the table viewer
			Menu menu = menuManager.createContextMenu(viewer.getControl());
			viewer.getControl().setMenu(menu);
		} catch (IllegalArgumentException ie) {
			StatusManager.getManager().handle(
				new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could create points areas. "
					+ ie.getMessage()), StatusManager.SHOW);
		}
		return tableParent;
	}
	
	@Override
	protected void createFieldEditors(){
		Composite parent = getFieldEditorParent();
		
		StringFieldEditor hvCodeEditor =
			new StringFieldEditor(SvaPreferenceInitializer.SVAPREF
				+ CorePreferenceConstants.KASSE_HVCODE, "HV Code: ", parent);
		addField(hvCodeEditor);
		
		StringFieldEditor billIntervalEditor =
			new StringFieldEditor(SvaPreferenceInitializer.SVAPREF
				+ CorePreferenceConstants.KASSE_BILLINGINTERVAL, "Abrechnungs Interval (Monate): ",
				parent);
		addField(billIntervalEditor);
		
		InsuranceCategory[] categories = KassenCodes.InsuranceCategory.values();
		String[][] namevalues = new String[categories.length][2];
		for (int i = 0; i < categories.length; i++) {
			namevalues[i][0] = categories[i].getName();
			namevalues[i][1] = Integer.toString(categories[i].getCode());
		}
		
		ComboFieldEditor insuranceCategoryEditor =
			new ComboFieldEditor(SvaPreferenceInitializer.SVAPREF
				+ CorePreferenceConstants.KASSE_STDINSURANCECATEGORIE,
				"Standard Versicherungs Kategorie: ", namevalues, parent);
		addField(insuranceCategoryEditor);
	}
	
}

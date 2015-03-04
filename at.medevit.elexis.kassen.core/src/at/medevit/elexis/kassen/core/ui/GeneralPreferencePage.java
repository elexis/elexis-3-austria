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

import java.text.ParseException;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import at.medevit.elexis.kassen.core.Activator;
import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.KassenCodes.FederalState;
import at.medevit.elexis.kassen.core.model.KassenCodes.SpecialityCode;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.dialogs.KontaktSelektor;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Rechnungssteller;

public class GeneralPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	Rechnungssteller mandantRechnungssteller;
	private ArrayList<KassenCodes.SpecialityCode>fachgebiete = new ArrayList<KassenCodes.SpecialityCode>();
	
	public GeneralPreferencePage() {
		super(GRID);
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(new SettingsPreferenceStore(CoreHub.mandantCfg, Activator.PLUGIN_ID));
		setDescription("Einstellungen für den aktuellen Mandanten und dessen Rechnungssteller.");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		
		// begin of Preferences for actual Mandant
		Label titel = new Label(parent, SWT.NONE);
		FontData[]bfd = titel.getFont().getFontData();
		bfd[0].setStyle(SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), bfd[0]);
		titel.setFont(boldFont);
		titel.setText("Einstellungen für Mandant:");
		Label text = new Label(parent, SWT.NONE);
		text.setFont(boldFont);
		text.setText(CoreHub.actMandant.getLabel());
		// HV Nummer
		StringFieldEditor hvNrEditor = new StringFieldEditor(CorePreferenceConstants.MANDANT_HVNUMBER,
				"HV/VP Nummer", parent);
		addField(hvNrEditor);
		// Fachgebiet
		SpecialityCode[] codes = KassenCodes.SpecialityCode.values();
		String[][] namevalues = new String[codes.length][2];
		for(int i = 0; i < codes.length; i++) {
			namevalues[i][0] = codes[i].getName();
			namevalues[i][1] = Integer.toString(codes[i].getCode());
		}
		
		ComboFieldEditor specialityEditor = new ComboFieldEditor(CorePreferenceConstants.MANDANT_FACHGEBIET,
				"Fachgebiet", namevalues, parent);
		addField(specialityEditor);
		// Bundesland
		FederalState[] sates = KassenCodes.FederalState.values();
		namevalues = new String[sates.length][2];
		for(int i = 0; i < sates.length; i++) {
			namevalues[i][0] = sates[i].getName();
			namevalues[i][1] = Integer.toString(sates[i].getCode());
		}
		
		ComboFieldEditor stateEditor = new ComboFieldEditor(CorePreferenceConstants.MANDANT_BUNDESLAND,
				"Bundesland", namevalues, parent);
		addField(stateEditor);
		// DVR Nummer
		StringFieldEditor dvrNrEditor = new StringFieldEditor(CorePreferenceConstants.MANDANT_DVRNUMBER,
				"DVR Nummer", parent);
		addField(dvrNrEditor);
		// Old/New Positions Id
		BooleanFieldEditor useNewPosId = new BooleanFieldEditor(CorePreferenceConstants.KASSEN_USENEWPOSID,
				"Neue Positions Id verwenden", parent);
		useNewPosId.setPreferenceName(CorePreferenceConstants.KASSEN_USENEWPOSID);
		addField(useNewPosId);
		
		Label sep = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		sep.setLayoutData(new GridData(
			GridData.FILL, GridData.CENTER,
			true, false, 2, 0));
		
		// begin of preferences for rechnungssteller
		mandantRechnungssteller = CoreHub.actMandant.getRechnungssteller();
		Label rstitel = new Label(parent, SWT.NONE);
		rstitel.setFont(boldFont);
		rstitel.setText("Einstellungen für Rechnungssteller:");
		Label rstext = new Label(parent, SWT.NONE);
		rstext.setFont(boldFont);
		rstext.setText(mandantRechnungssteller.getLabel());
		// HV/VP Nummer 
		if(mandantRechnungssteller.istOrganisation()) {
			Label lbl = new Label(parent, SWT.NONE);
			lbl.setText("HV/VP Nummer");
			lbl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true,
					false, 0, 0));
			final Text hvNumber = new Text(parent, SWT.BORDER);
			hvNumber.setLayoutData(new GridData(GridData.FILL,
					GridData.CENTER, true, false, 0, 0));
			hvNumber.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					mandantRechnungssteller.setInfoElement(
							CorePreferenceConstants.RECHNUNGSSTELLER_HVNUMBER,
							hvNumber.getText());
				}
			});
			hvNumber
					.setText(mandantRechnungssteller
							.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_HVNUMBER));
		} else {
			Label lbl = new Label(parent, SWT.NONE);
			lbl.setText("HV/VP Nummer");
			lbl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true,
					false, 0, 0));
			final Text hvNumber = new Text(parent, SWT.BORDER);
			hvNumber.setLayoutData(new GridData(GridData.FILL,
					GridData.CENTER, true, false, 0, 0));
			hvNumber.setEditable(false);
			hvNumber.setText(getPreferenceStore().getString(CorePreferenceConstants.MANDANT_HVNUMBER));
		}
		// Kontonummer
		Label lbl = new Label(parent, SWT.NONE);
		lbl.setText("Kontonummer");
		lbl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true,
				false, 0, 0));
		final Text accountNumber = new Text(parent, SWT.BORDER);
		accountNumber.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, true, false, 0, 0));
		accountNumber.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				mandantRechnungssteller.setInfoElement(
						CorePreferenceConstants.RECHNUNGSSTELLER_ACCOUNTNR,
						accountNumber.getText());
			}
		});
		accountNumber
				.setText(mandantRechnungssteller
						.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_ACCOUNTNR));
		// Bankleitzahl
		lbl = new Label(parent, SWT.NONE);
		lbl.setText("Bankleitzahl");
		lbl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true,
				false, 0, 0));
		final Text bankCode = new Text(parent, SWT.BORDER);
		bankCode.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, true, false, 0, 0));
		bankCode.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				mandantRechnungssteller.setInfoElement(
						CorePreferenceConstants.RECHNUNGSSTELLER_BANKCODE,
						bankCode.getText());
			}
		});
		bankCode.setText(mandantRechnungssteller
				.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_BANKCODE));
		// Bank Kontakt
		lbl = new Label(parent, SWT.NONE);
		lbl.setText("Kontakt der Bank:");
		
		Composite contactParent = new Composite(parent, SWT.NONE);
		contactParent.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, true, false, 0, 0));
		contactParent.setLayout(new FormLayout());
		final Text contactTxt = new Text(contactParent, SWT.BORDER);
		contactTxt.setEditable(false);
		
		String bankContactId = mandantRechnungssteller.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_BANKCONTACT);
		if(bankContactId.length() > 1) {
			Kontakt bvaContact = Kontakt.load(bankContactId);
			contactTxt.setText(bvaContact.getLabel());
		}
		Button contactBtn = new Button(contactParent, SWT.PUSH);
		contactBtn.setText("Kontakt");
		contactBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				KontaktSelektor ksl =
					new KontaktSelektor(getShell(), Kontakt.class, "Kontakt auswählen",
						"Kontakt der Bank auswählen, bzw. anlegen.", false, Kontakt.DEFAULT_SORT);
				if (ksl.open() == Dialog.OK) {
					Kontakt sel = (Kontakt) ksl.getSelection();
					mandantRechnungssteller.setInfoElement(CorePreferenceConstants.RECHNUNGSSTELLER_BANKCONTACT, sel.getId());
					contactTxt.setText(sel.getLabel());
				}
			}
		});
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		contactBtn.setLayoutData(fd);
		
		fd = new FormData();		
		fd.top = new FormAttachment(contactBtn, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(contactBtn, -5);
		contactTxt.setLayoutData(fd);
		
		// Fachgebiete
		try {
			fachgebiete
					.addAll(KassenLeistung.getSpecialitiesForString(mandantRechnungssteller
							.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_SPECIALITIES)));
		} catch (ParseException e1) {
			// ignore and build ui to set new fachgebiete
		}
		lbl = new Label(parent, SWT.NONE);
		lbl.setText("Fachgebiete:");
		
		Composite specialityParent = new Composite(parent, SWT.NONE);
		specialityParent.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, true, false, 0, 0));
		specialityParent.setLayout(new FormLayout());
		
		final ListViewer fachgebieteList = new ListViewer(specialityParent, SWT.BORDER | SWT.V_SCROLL);
		fachgebieteList.setContentProvider(new ArrayContentProvider());
		fachgebieteList.setInput(fachgebiete);
		fachgebieteList.setLabelProvider(new LabelProvider() {
		    @Override
		    public String getText(Object element) {
		    	KassenCodes.SpecialityCode code = (KassenCodes.SpecialityCode) element;
		        return code.getName();
		    }
		});
		
		// First we create a menu Manager
		MenuManager menuManager = new MenuManager();
		menuManager.add(new Action() {
			@Override
			public String getText() {
				return "remove";
			}

			@Override
			public void run() {
				ISelection selection = fachgebieteList.getSelection();
				if (selection != null & selection instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					SpecialityCode code = (KassenCodes.SpecialityCode) sel.getFirstElement();
					if(code != null) {
						fachgebiete.remove(code);
						fachgebieteList.refresh();
					}
				}
			}
		});
		
		// set the menu for the list viewer
		Menu menu = menuManager.createContextMenu(fachgebieteList.getControl());
		fachgebieteList.getControl().setMenu(menu);
		
		final ComboViewer fachgebieteCombo = new ComboViewer(specialityParent, SWT.BORDER);
		fachgebieteCombo.setContentProvider(new ArrayContentProvider());
		fachgebieteCombo.setInput(KassenCodes.SpecialityCode.values());
		fachgebieteCombo.setLabelProvider(new LabelProvider() {
		    @Override
		    public String getText(Object element) {
		    	KassenCodes.SpecialityCode code = (KassenCodes.SpecialityCode) element;
		        return code.getName();
		    }
		});
		
		Button fachgebieteBtn = new Button(specialityParent, SWT.PUSH);
		fachgebieteBtn.setText("<<");
		fachgebieteBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection sel = (IStructuredSelection) fachgebieteCombo.getSelection();
				SpecialityCode code = (KassenCodes.SpecialityCode) sel.getFirstElement();
				if(code != null) {
					fachgebiete.add(code);
					mandantRechnungssteller
							.setInfoElement(
									CorePreferenceConstants.RECHNUNGSSTELLER_SPECIALITIES,
									KassenLeistung.getStringForSpecialities(fachgebiete));
					fachgebieteList.refresh();
				}
			}
		});
		
		fd = new FormData();
		fd.top = new FormAttachment(0, 0);
	    fd.left = new FormAttachment(0, 0);
	    fd.right = new FormAttachment(55, -5);
	    fd.bottom = new FormAttachment(100, 0);
	    fachgebieteList.getControl().setLayoutData(fd);

		fd = new FormData();
		fd.top = new FormAttachment(0, 0);
	    fd.left = new FormAttachment(55, 5);
	    fd.right = new FormAttachment(90, -5);
	    fd.width = 100;
	    fachgebieteCombo.getControl().setLayoutData(fd);
	    
		fd = new FormData();
	    fd.top = new FormAttachment(0, 0);
	    fd.left = new FormAttachment(90, 5);
	    fd.right = new FormAttachment(100, 0);
	    fachgebieteBtn.setLayoutData(fd);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getSource() instanceof BooleanFieldEditor) {
			BooleanFieldEditor source = (BooleanFieldEditor) event.getSource();
			if(CorePreferenceConstants.KASSEN_USENEWPOSID.equals(source.getPreferenceName()))
				KassenLeistung.setUseNewPosId((Boolean) event.getNewValue());
		}
		super.propertyChange(event);
	}
}

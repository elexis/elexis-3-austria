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

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.privat.model.Catalog;
import at.medevit.elexis.kassen.privat.model.PreferenceConstants;
import at.medevit.elexis.kassen.privat.model.PreferenceConstants.ValueType;
import at.medevit.elexis.kassen.privat.model.PrivatKasse;
import ch.elexis.core.ui.dialogs.KontaktSelektor;
import ch.elexis.data.Kontakt;

public class EditPrivatKasseDialog extends TitleAreaDialog {
	PrivatKasse kasse;
	
	private Text nameTxt;
	private Text contactTxt;
	private Button contactBtn;
	private Kontakt contact;
	private CheckboxTableViewer catalogsTable;
	private Text valueTxt;
	private ComboViewer valueTypeCombo;
	
	public EditPrivatKasseDialog(Shell parentShell, PrivatKasse kasse){
		super(parentShell);
		this.kasse = kasse;
	}
	
	@Override
	protected Control createContents(Composite parent){
		Control contents = super.createContents(parent);
		if (kasse == null) {
			setTitle("Neue privat Kasse");
			setMessage("Daten der neuen privat Kasse eingeben");
		} else {
			setTitle("Privat Kasse editieren");
			setMessage("Daten der privat Kasse ändern");
		}
		return contents;
	}
	
	@Override
	protected Control createDialogArea(Composite parent){
		Composite composite = (Composite) super.createDialogArea(parent);
		
		Composite areaComposite = new Composite(composite, SWT.NONE);
		areaComposite
			.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		
		areaComposite.setLayout(new FormLayout());
		
		// name
		Label lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Name");
		nameTxt = new Text(areaComposite, SWT.BORDER);
		if (kasse != null) {
			nameTxt.setText(kasse.getName());
			nameTxt.setEditable(false);
		}
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		nameTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(nameTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// contact
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Kontakt");
		contactTxt = new Text(areaComposite, SWT.BORDER);
		contactTxt.setEditable(false);
		if (kasse != null && kasse.getContact() != null) {
			contact = kasse.getContact();
			contactTxt.setText(kasse.getContact().getLabel());
			contactTxt.setText(contact.getLabel());
		}
		
		contactBtn = new Button(areaComposite, SWT.PUSH);
		contactBtn.setText("Kontakt");
		contactBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				KontaktSelektor ksl =
					new KontaktSelektor(getShell(), Kontakt.class, "Kontakt auswählen",
						"Kontakt der privaten Kasse auswählen, bzw. zuerst anlegen.", false,
						Kontakt.DEFAULT_SORT);
				if (ksl.open() == Dialog.OK) {
					contact = (Kontakt) ksl.getSelection();
					contactTxt.setText(contact.getLabel());
				}
			}
		});
		
		fd = new FormData();
		fd.top = new FormAttachment(nameTxt, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(80, -5);
		contactTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(contactTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(contactTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(80, 5);
		fd.right = new FormAttachment(100, -5);
		contactBtn.setLayoutData(fd);
		
		// catalog
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Kataloge");
		Table table =
			new Table(areaComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.CHECK);
		catalogsTable = new CheckboxTableViewer(table);
		catalogsTable.setContentProvider(new ArrayContentProvider());
		List<Catalog> catalogs = Catalog.getAll();
		catalogsTable.setInput(catalogs);
		if (kasse != null) {
			List<Catalog> selected = kasse.getCatalogs();
			for (Catalog catalog : catalogs) {
				for (Catalog selCatalog : selected) {
					if (catalog.getSystemName().equalsIgnoreCase(selCatalog.getSystemName())) {
						catalogsTable.setChecked(catalog, true);
						break;
					}
				}
			}
		} else {
			catalogsTable.setAllChecked(true);
		}
		
		fd = new FormData();
		fd.top = new FormAttachment(contactTxt, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		catalogsTable.getControl().setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(catalogsTable.getControl(), 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// value
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Zuschlag");
		valueTxt = new Text(areaComposite, SWT.BORDER);
		if (kasse != null)
			valueTxt.setText(kasse.getValueAsString());
		
		valueTypeCombo = new ComboViewer(areaComposite);
		valueTypeCombo.setContentProvider(new ArrayContentProvider());
		valueTypeCombo.setInput(PreferenceConstants.ValueType.values());
		if (kasse == null) {
			valueTypeCombo.setSelection(new StructuredSelection(
				PreferenceConstants.ValueType.PERCENT));
		} else {
			valueTypeCombo.setSelection(new StructuredSelection(kasse.getValueType()));
		}
		
		fd = new FormData();
		fd.top = new FormAttachment(catalogsTable.getControl(), 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(80, -5);
		valueTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(valueTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(valueTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(80, 5);
		fd.right = new FormAttachment(100, -5);
		valueTypeCombo.getControl().setLayoutData(fd);
		
		return composite;
	}
	
	public PrivatKasse getPrivatKasse(){
		return kasse;
	}
	
	@Override
	protected void okPressed(){
		// check for correctness
		// name
		if (nameTxt.getText().length() == 0) {
			setErrorMessage("Kein Name angegeben.");
			return;
		}
		// catalog
		Object[] catalogs = catalogsTable.getCheckedElements();
		if (catalogs.length == 0) {
			setErrorMessage("Kein Katalog ausgewählt");
			return;
		}
		IStructuredSelection selection = (IStructuredSelection) valueTypeCombo.getSelection();
		PreferenceConstants.ValueType type = (ValueType) selection.getFirstElement();
		// value
		if (valueTxt.getText().length() == 0) {
			setErrorMessage("Kein Zuschlag angegeben.");
			return;
		} else {
			try {
				Double.parseDouble(valueTxt.getText());
			} catch (NumberFormatException ex) {
				setErrorMessage("Zuschlag enthält keine gültige Zahl.");
				return;
			}
		}
		// create the kasse if necessary
		if (kasse == null) {
			if (PrivatKasse.getByName(nameTxt.getText()) != null) {
				setErrorMessage("Name " + nameTxt.getText() + " bereits vergeben.");
				return;
			}
			kasse = new PrivatKasse(CorePreferenceConstants.CFG_KEY + "/" + nameTxt.getText());
		}
		// set the values
		kasse.setName(nameTxt.getText());
		if (contact != null)
			kasse.setContact(contact);
		kasse.setCatalog(catalogs);
		kasse.setValue(valueTxt.getText());
		kasse.setValueType(type);
		
		super.okPressed();
	}
}

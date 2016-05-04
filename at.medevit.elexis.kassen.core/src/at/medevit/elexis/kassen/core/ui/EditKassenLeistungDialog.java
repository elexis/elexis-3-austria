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
import java.util.Date;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.KassenCodes.SpecialityCode;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.LeistungBean;
import at.medevit.elexis.kassen.core.model.expressions.KassenExpressionFactory;

import com.tiff.common.ui.datepicker.DatePickerCombo;

public class EditKassenLeistungDialog extends TitleAreaDialog {
	
	private LeistungBean editLeistung;
	private LeistungBean retLeistung;
	private Class<? extends KassenLeistung> clazz;
	
	private Button isGroup;
	private Button isPosition;
	
	private Text gruppeIdTxt;
	private ComboViewer positionGruppeIdCombo;
	private Text positionIdTxt;
	private Text positionNeuIdTxt;
	
	private Text positionTitelTxt;
	private Text positionHinweisTxtArea;
	
	private DatePickerCombo fromDateCombo;
	private DatePickerCombo toDateCombo;
	
	private ListViewer positionFachgebieteList;
	private ComboViewer positionFachgebieteCombo;
	private Button positionFachgebieteBtn;
	private ArrayList<KassenCodes.SpecialityCode> fachgebiete =
		new ArrayList<KassenCodes.SpecialityCode>();
	
	private Button positionAusFachCheck;
	
	private Text positionPunktWertTxt;
	private Text positionGeldWertTxt;
	
	private Text positionZusatzTxt;
	private Text positionLogikTxt;
	
	public EditKassenLeistungDialog(Shell parentShell, LeistungBean leistung,
		Class<? extends KassenLeistung> clazz){
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.editLeistung = leistung;
		this.clazz = clazz;
	}
	
	@Override
	protected Control createContents(Composite parent){
		Control contents = super.createContents(parent);
		
		setTitle("Kassen Leistung");
		setMessage("Die Kassen Leistung ändern.");
		return contents;
	}
	
	@Override
	protected Control createDialogArea(Composite parent){
		Composite composite = (Composite) super.createDialogArea(parent);
		
		Composite areaComposite = new Composite(composite, SWT.NONE);
		areaComposite
			.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		
		areaComposite.setLayout(new FormLayout());
		// Create the first group
		Group group1 = new Group(areaComposite, SWT.SHADOW_IN);
		group1.setText("Leistungs Typ");
		group1.setLayout(new RowLayout(SWT.HORIZONTAL));
		isGroup = new Button(group1, SWT.RADIO);
		isGroup.setText("Gruppe");
		isGroup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				if (isGroup.getSelection())
					enableGroup();
			}
		});
		isPosition = new Button(group1, SWT.RADIO);
		isPosition.setText("Position");
		isPosition.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				if (isPosition.getSelection())
					enablePosition();
			}
		});
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		group1.setLayoutData(fd);
		
		// gruppeId
		Label lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Gruppen Id:");
		gruppeIdTxt = new Text(areaComposite, SWT.BORDER);
		
		fd = new FormData();
		fd.top = new FormAttachment(group1, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		gruppeIdTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(gruppeIdTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionGruppeId
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Positions Gruppen Id:");
		positionGruppeIdCombo = new ComboViewer(areaComposite, SWT.BORDER);
		positionGruppeIdCombo.setContentProvider(new ArrayContentProvider());
		positionGruppeIdCombo.setInput(KassenLeistung.getAllCurrentGroupLeistungen(clazz));
		positionGruppeIdCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				return leistung.getGroup() + " " + leistung.getTitle();
			}
		});
		
		fd = new FormData();
		fd.top = new FormAttachment(gruppeIdTxt, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		fd.width = 100;
		positionGruppeIdCombo.getControl().setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionGruppeIdCombo.getControl(), 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionId
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Positions Id:");
		positionIdTxt = new Text(areaComposite, SWT.BORDER);
		Label newPosLbl = new Label(areaComposite, SWT.NONE);
		newPosLbl.setText("Neue Id:");
		positionNeuIdTxt = new Text(areaComposite, SWT.BORDER);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionGruppeIdCombo.getControl(), 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(50, -5);
		positionIdTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionIdTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionIdTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(50, 5);
		fd.right = new FormAttachment(70, -5);
		newPosLbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionIdTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(70, 5);
		fd.right = new FormAttachment(100, -5);
		positionNeuIdTxt.setLayoutData(fd);
		
		// positionTitel
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Titel:");
		positionTitelTxt = new Text(areaComposite, SWT.BORDER);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionIdTxt, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		positionTitelTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionTitelTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionHinweis
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Hinweis Text:");
		positionHinweisTxtArea =
			new Text(areaComposite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionTitelTxt, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(positionTitelTxt, 85);
		positionHinweisTxtArea.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionHinweisTxtArea, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionPunktWert
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Punkt Wert:");
		positionPunktWertTxt = new Text(areaComposite, SWT.BORDER);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionHinweisTxtArea, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		positionPunktWertTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionPunktWertTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionGeldWertTxt
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Geld Wert:");
		positionGeldWertTxt = new Text(areaComposite, SWT.BORDER);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionPunktWertTxt, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		positionGeldWertTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionGeldWertTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// fromDateCombo toDateCombo
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Gültigkeitsbereich:");
		fromDateCombo = new DatePickerCombo(areaComposite, SWT.BORDER);
		toDateCombo = new DatePickerCombo(areaComposite, SWT.BORDER);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionGeldWertTxt, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(60, -5);
		fromDateCombo.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionGeldWertTxt, 5);
		fd.left = new FormAttachment(60, 5);
		fd.right = new FormAttachment(100, -5);
		toDateCombo.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(toDateCombo, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionFachgebieteList
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Fachgebiete:");
		positionFachgebieteList = new ListViewer(areaComposite, SWT.BORDER | SWT.V_SCROLL);
		positionFachgebieteList.setContentProvider(new ArrayContentProvider());
		positionFachgebieteList.setInput(fachgebiete);
		positionFachgebieteList.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				KassenCodes.SpecialityCode code = (KassenCodes.SpecialityCode) element;
				return code.getName();
			}
		});
		
		// First we create a menu Manager
		MenuManager menuManager = new MenuManager();
		menuManager.add(new Action() {
			@Override
			public String getText(){
				return "remove";
			}
			
			@Override
			public void run(){
				ISelection selection = positionFachgebieteList.getSelection();
				if (selection != null & selection instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) selection;
					if(sel==null) {
						return;
					}
					SpecialityCode code = (KassenCodes.SpecialityCode) sel.getFirstElement();
					if (code != null) {
						fachgebiete.remove(code);
						positionFachgebieteList.refresh();
					}
				}
			}
		});
		
		// set the menu for the list viewer
		Menu menu = menuManager.createContextMenu(positionFachgebieteList.getControl());
		positionFachgebieteList.getControl().setMenu(menu);
		
		positionFachgebieteCombo = new ComboViewer(areaComposite, SWT.BORDER);
		positionFachgebieteCombo.setContentProvider(new ArrayContentProvider());
		positionFachgebieteCombo.setInput(KassenCodes.SpecialityCode.values());
		positionFachgebieteCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				KassenCodes.SpecialityCode code = (KassenCodes.SpecialityCode) element;
				return code.getName();
			}
		});
		
		positionFachgebieteBtn = new Button(areaComposite, SWT.PUSH);
		positionFachgebieteBtn.setText("<<");
		positionFachgebieteBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				IStructuredSelection sel =
					(IStructuredSelection) positionFachgebieteCombo.getSelection();
				SpecialityCode code = (KassenCodes.SpecialityCode) sel.getFirstElement();
				if (code != null) {
					fachgebiete.add(code);
					positionFachgebieteList.refresh();
				}
			}
		});
		
		fd = new FormData();
		fd.top = new FormAttachment(fromDateCombo, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(55, -5);
		fd.bottom = new FormAttachment(fromDateCombo, 85);
		positionFachgebieteList.getControl().setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(fromDateCombo, 5);
		fd.left = new FormAttachment(55, 5);
		fd.right = new FormAttachment(90, -5);
		fd.width = 100;
		positionFachgebieteCombo.getControl().setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(fromDateCombo, 5);
		fd.left = new FormAttachment(90, 5);
		fd.right = new FormAttachment(100, -5);
		positionFachgebieteBtn.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionFachgebieteList.getControl(), 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionAusFachCheck
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Poisition ist aus Fach:");
		positionAusFachCheck = new Button(areaComposite, SWT.CHECK);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionFachgebieteList.getControl(), 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		positionAusFachCheck.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionAusFachCheck, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionZusatzTxt
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Poisition Zusätze:");
		positionZusatzTxt = new Text(areaComposite, SWT.BORDER);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionAusFachCheck, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		positionZusatzTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionZusatzTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// positionLogikTxt
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Poisition Logik:");
		positionLogikTxt = new Text(areaComposite, SWT.BORDER);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionZusatzTxt, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		positionLogikTxt.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(positionLogikTxt, 0, SWT.CENTER);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		// try to initialize the values
		if (editLeistung != null) {
			String grpId = editLeistung.getGruppeId();
			if (grpId != null && grpId.length() > 0) {
				isGroup.setSelection(true);
				enableGroup();
			} else {
				isPosition.setSelection(true);
				enablePosition();
			}
			
			setLeistung(editLeistung);
		}
		
		return composite;
	}
	
	@Override
	protected void okPressed(){
		if (validateLeistung()) {
			retLeistung = getLeistung();
			super.okPressed();
		}
	}
	
	public LeistungBean getLeistungBean(){
		return retLeistung;
	}
	
	private void enableGroup(){
		gruppeIdTxt.setEnabled(true);
		positionGruppeIdCombo.getControl().setEnabled(false);
		positionIdTxt.setEnabled(false);
		positionNeuIdTxt.setEnabled(false);
		
		positionTitelTxt.setEnabled(true);
		positionHinweisTxtArea.setEnabled(true);
		
		fromDateCombo.setEnabled(true);
		toDateCombo.setEnabled(true);
		
		positionFachgebieteList.getControl().setEnabled(false);
		positionFachgebieteCombo.getControl().setEnabled(false);
		positionFachgebieteBtn.setEnabled(false);
		positionAusFachCheck.setEnabled(false);
		
		positionPunktWertTxt.setEnabled(false);
		positionGeldWertTxt.setEnabled(false);
		
		positionZusatzTxt.setEnabled(false);
		positionLogikTxt.setEnabled(false);
	}
	
	private void enablePosition(){
		gruppeIdTxt.setEnabled(false);
		positionGruppeIdCombo.getControl().setEnabled(true);
		positionIdTxt.setEnabled(true);
		positionNeuIdTxt.setEnabled(true);
		
		positionTitelTxt.setEnabled(true);
		positionHinweisTxtArea.setEnabled(true);
		
		fromDateCombo.setEnabled(true);
		toDateCombo.setEnabled(true);
		
		positionFachgebieteList.getControl().setEnabled(true);
		positionFachgebieteCombo.getControl().setEnabled(true);
		positionFachgebieteBtn.setEnabled(true);
		positionAusFachCheck.setEnabled(true);
		
		positionPunktWertTxt.setEnabled(true);
		positionGeldWertTxt.setEnabled(true);
		
		positionZusatzTxt.setEnabled(true);
		positionLogikTxt.setEnabled(true);
	}
	
	private void setLeistung(LeistungBean leistung){
		gruppeIdTxt.setText(leistung.getGruppeId());
		
		String positionGruppenId = leistung.getPositionGruppenId();
		if (positionGruppenId != null && positionGruppenId.length() > 0) {
			List<? extends KassenLeistung> kl =
				KassenLeistung
					.getCurrentLeistungenByIds(positionGruppenId, null, null, null, clazz);
			
			positionGruppeIdCombo.setSelection(new StructuredSelection(kl));
		}
		
		positionIdTxt.setText(leistung.getPositionId());
		positionNeuIdTxt.setText(leistung.getPositionNeuId());
		
		positionTitelTxt.setText(leistung.getPositionTitle());
		positionHinweisTxtArea.setText(leistung.getPositionHinweis());
		
		String from = leistung.getValidFromDate();
		fromDateCombo.setDate(KassenLeistung.getDateForStringWithDefault(from));
		
		String to = leistung.getValidToDate();
		try {
			toDateCombo.setDate(KassenLeistung.getDateForString(to));
		} catch (ParseException e) {
			// ignore and leave empty ...
		}
		
		try {
			fachgebiete.addAll(KassenLeistung.getSpecialitiesForString(leistung
				.getPositionFachgebiete()));
		} catch (ParseException e) {
			// ignore and leave empty ...
		}
		positionFachgebieteList.refresh();
		
		String ausFach = leistung.getPositionAusFach();
		positionAusFachCheck.setSelection(ausFach.equals("1"));
		
		positionPunktWertTxt.setText(leistung.getPositionPunktWert());
		positionGeldWertTxt.setText(leistung.getPositionGeldWert());
		
		positionZusatzTxt.setText(leistung.getPositionZusatz());
		positionLogikTxt.setText(leistung.getPositionLogik());
	}
	
	private boolean validateLeistung(){
		if (isGroup.getSelection()) {
			// validate groupid
			String grpId = gruppeIdTxt.getText();
			if (!(grpId != null && grpId.length() > 0)) {
				setErrorMessage("No id for group.");
				return false;
			} else if (!grpId.matches("[0-9.]+")) {
				setErrorMessage("Group id contains not valid characters.");
				return false;
			} else if (!grpId.matches("[0-9]+.[0-9]+") && !grpId.matches("[0-9]+")) {
				setErrorMessage("Currently only 2 group levels (x.y) are supported.");
				return false;
			} else {
				if (editLeistung == null) {
					List<? extends KassenLeistung> tst =
						KassenLeistung.getCurrentLeistungenByIds(grpId, null, null, null, clazz);
					if (tst.size() > 0) {
						setErrorMessage("Group id already in db.");
						return false;
					}
				}
			}
			// validate group titel
			String title = positionTitelTxt.getText();
			if (!(title != null && title.length() > 0)) {
				setErrorMessage("No titel for group.");
				return false;
			}
		} else {
			// validate positiongroupid
			StructuredSelection sel = (StructuredSelection) positionGruppeIdCombo.getSelection();
			KassenLeistung kl = (KassenLeistung) sel.getFirstElement();
			if (kl == null) {
				setErrorMessage("No group for position selected.");
				return false;
			}
			
			// validate positionid
			String posId = positionIdTxt.getText();
			if (!(posId != null && posId.length() > 0)) {
				setErrorMessage("No id for position.");
			} else {
				if (editLeistung == null) {
					List<? extends KassenLeistung> tst =
						KassenLeistung.getCurrentLeistungenByIds(null, kl.getGroup(), posId, null,
							clazz);
					if (tst.size() > 0) {
						setErrorMessage("Position id already in group.");
						return false;
					}
				}
			}
			
			// validate positionneuid
			String posneuId = positionIdTxt.getText();
			if (posneuId != null && posneuId.length() > 0) {
				if (editLeistung == null) {
					List<? extends KassenLeistung> tst =
						KassenLeistung.getCurrentLeistungenByIds(null, kl.getGroup(), null,
							posneuId, clazz);
					if (tst.size() > 0) {
						setErrorMessage("Position neu id already in group.");
						return false;
					}
				}
			}
			
			// validate position titel
			String title = positionTitelTxt.getText();
			if (!(title != null && title.length() > 0)) {
				setErrorMessage("No titel for position.");
				return false;
			}
			
			// validate values
			String pVal = positionPunktWertTxt.getText();
			String mVal = positionGeldWertTxt.getText();
			boolean pIsSet = (pVal != null && pVal.length() > 0);
			boolean mIsSet = (mVal != null && mVal.length() > 0);
			if (!pIsSet && !mIsSet) {
				setErrorMessage("No point and money value.");
				return false;
			}
			if (pIsSet) {
				try {
					KassenLeistung.getDoubleForString(pVal);
				} catch (ParseException e) {
					setErrorMessage("Point value not valid.");
					return false;
				}
			}
			if (mIsSet) {
				try {
					KassenLeistung.getDoubleForString(mVal);
				} catch (ParseException e) {
					setErrorMessage("Money value not valid.");
					return false;
				}
			}
			
			// validate zusatz
			String zusatz = positionZusatzTxt.getText();
			if (zusatz != null && zusatz.length() > 0) {
				try {
					KassenLeistung.getAdditionalFromString(zusatz, clazz);
				} catch (ParseException e) {
					setErrorMessage("Zusatz value not valid. " + e.getMessage());
					return false;
				}
			}
			// validate logik
			String logik = positionLogikTxt.getText();
			if (logik != null && logik.length() > 0) {
				try {
					KassenExpressionFactory.getInstance().getExpressionForString(logik, clazz);
				} catch (ParseException e) {
					setErrorMessage("Logik value not valid. " + e.getMessage());
					return false;
				}
			}
		}
		return true;
	}
	
	private LeistungBean getLeistung(){
		LeistungBean leistung = new LeistungBean();
		
		leistung.setGruppeId(gruppeIdTxt.getText());
		
		StructuredSelection sel = (StructuredSelection) positionGruppeIdCombo.getSelection();
		KassenLeistung kl = (KassenLeistung) sel.getFirstElement();
		if (kl != null)
			leistung.setPositionGruppenId(kl.getGroup());
		
		leistung.setPositionId(positionIdTxt.getText());
		leistung.setPositionNeuId(positionNeuIdTxt.getText());
		
		leistung.setPositionTitle(positionTitelTxt.getText());
		leistung.setPositionHinweis(positionHinweisTxtArea.getText());
		
		Date from = fromDateCombo.getDate();
		if (from != null)
			leistung.setValidFromDate(KassenLeistung.getStringForDate(from));
		
		Date to = toDateCombo.getDate();
		if (to != null)
			leistung.setValidToDate(KassenLeistung.getStringForDate(to));
		
		String def = KassenLeistung.getStringForSpecialities(fachgebiete);
		if (def != null)
			leistung.setPositionFachgebiete(def);
		
		leistung.setPositionAusFach(positionAusFachCheck.getSelection() ? "1" : "0");
		
		leistung.setPositionPunktWert(positionPunktWertTxt.getText());
		leistung.setPositionGeldWert(positionGeldWertTxt.getText());
		
		leistung.setPositionZusatz(positionZusatzTxt.getText());
		leistung.setPositionLogik(positionLogikTxt.getText());
		
		return leistung;
	}
}

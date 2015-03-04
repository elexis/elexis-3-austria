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
package at.medevit.elexis.at.properties.propertyPage;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import at.medevit.elexis.at.XID.SVNR;
import at.medevit.elexis.at.model.PatientKassenData;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import ch.elexis.data.Patient;

public class KassenDaten extends PropertyPage implements IWorkbenchPropertyPage {
	
	private Patient pat;
	private PatientKassenData data;
	private Text txtSVNr;
	
	private ComboViewer sysOneCombo;
	private ComboViewer sysTwoCombo;
	
	private ComboViewer insuranceCatCombo;
	
	private ComboViewer additionalInsuranceCombo;
	
	private ControlDecoration txtSVNrDeco;
	private Image errorImage;
	
	public KassenDaten(){
		super();
	}
	
	@Override
	protected Control createContents(Composite parent){
		init();
		super.setTitle(pat.getLabel());
		Composite comp = new Composite(parent, SWT.None);
		comp.setLayout(new GridLayout(2, false));
		
		Label lblSVNr = new Label(comp, SWT.NONE);
		lblSVNr.setText("SV Nummer");
		
		txtSVNr = new Text(comp, SWT.BORDER);
		txtSVNr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtSVNr.setText(pat.getXid(SVNR.DOMAIN_AT_SVNR));
		txtSVNr.setTextLimit(10);
		
		txtSVNrDeco = new ControlDecoration(txtSVNr, SWT.LEFT);
		errorImage =
			FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		txtSVNrDeco.setImage(errorImage);
		txtSVNrDeco.setDescriptionText("Fehlerhafte Versicherungsnummer!");
		txtSVNrDeco.setShowHover(true);
		txtSVNrDeco.hide();
		
		new Label(comp, SWT.NONE).setText("Erste Kasse");
		sysOneCombo = new ComboViewer(comp, SWT.BORDER);
		sysOneCombo.setContentProvider(new ArrayContentProvider());
		sysOneCombo.setInput(getKassenCodeSystemNamesForCombo());
		sysOneCombo.setSorter(new ViewerSorter());
		sysOneCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				if (element instanceof String)
					return (String) element;
				else
					return "?";
			}
		});
		sysOneCombo.getControl().setLayoutData(
			new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if (data != null) {
			sysOneCombo.setSelection(new StructuredSelection(data
				.get(PatientKassenData.FLD_SYSTEMONE)));
		}
		
		new Label(comp, SWT.NONE).setText("Zweite Kasse");
		sysTwoCombo = new ComboViewer(comp, SWT.BORDER);
		sysTwoCombo.setContentProvider(new ArrayContentProvider());
		sysTwoCombo.setInput(getKassenCodeSystemNamesForCombo());
		sysTwoCombo.setSorter(new ViewerSorter());
		sysTwoCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				if (element instanceof String)
					return (String) element;
				else
					return "?";
			}
		});
		sysTwoCombo.getControl().setLayoutData(
			new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if (data != null) {
			sysTwoCombo.setSelection(new StructuredSelection(data
				.get(PatientKassenData.FLD_SYSTEMTWO)));
		}
		
		new Label(comp, SWT.NONE).setText("Versich. Kategorie");
		insuranceCatCombo = new ComboViewer(comp, SWT.BORDER);
		insuranceCatCombo.setContentProvider(new ArrayContentProvider());
		insuranceCatCombo.setInput(KassenCodes.InsuranceCategory.values());
		insuranceCatCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				KassenCodes.InsuranceCategory category = (KassenCodes.InsuranceCategory) element;
				return category.getName();
			}
		});
		insuranceCatCombo.getControl().setLayoutData(
			new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		new Label(comp, SWT.NONE).setText("Zusatzversichert");
		additionalInsuranceCombo = new ComboViewer(comp, SWT.BORDER);
		additionalInsuranceCombo.setContentProvider(new ArrayContentProvider());
		additionalInsuranceCombo.setInput(getAdditinalInsurancesForCombo());
		additionalInsuranceCombo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element){
				if (element instanceof String)
					return (String) element;
				else
					return "?";
			}
		});
		additionalInsuranceCombo.getControl().setLayoutData(
			new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if (data != null) {
			additionalInsuranceCombo.setSelection(new StructuredSelection(data
				.get(PatientKassenData.FLD_ADDITIONALINSURANCE)));
		}
		return comp;
	}
	
	@Override
	protected void performApply(){
		if (SVNR.validateSVNumber(txtSVNr.getText())) {
			txtSVNrDeco.hide();
			setTitle(pat.getLabel());
			pat.addXid(SVNR.DOMAIN_AT_SVNR, txtSVNr.getText(), true);
		} else {
			txtSVNrDeco.show();
			setErrorMessage("Fehlerhafte Versicherungsnummer");
		}
		
		if (data == null)
			data = new PatientKassenData(pat);
		
		StructuredSelection selection = (StructuredSelection) sysOneCombo.getSelection();
		String sysOneName = (String) selection.getFirstElement();
		if (sysOneName != null) {
			data.set(PatientKassenData.FLD_SYSTEMONE, sysOneName);
		}
		
		selection = (StructuredSelection) sysTwoCombo.getSelection();
		String sysTwoName = (String) selection.getFirstElement();
		if (sysTwoName != null) {
			data.set(PatientKassenData.FLD_SYSTEMTWO, sysTwoName);
		}
		
		selection = (StructuredSelection) additionalInsuranceCombo.getSelection();
		String additionalInsuranceName = (String) selection.getFirstElement();
		if (additionalInsuranceName != null) {
			data.set(PatientKassenData.FLD_ADDITIONALINSURANCE, additionalInsuranceName);
		}
	}
	
	private String[] getKassenCodeSystemNamesForCombo(){
		String[] codesystems = KassenLeistung.getAllKassenCodeSystemNames();
		String[] ret = new String[codesystems.length + 1];
		ret[0] = "";
		for (int i = 0; i < codesystems.length; i++)
			ret[i + 1] = codesystems[i];
		return ret;
	}
	
	private String[] getAdditinalInsurancesForCombo(){
		String[] names = KassenLeistung.getAdditionalInsuranceNames();
		String[] ret = new String[0];
		if (names != null) {
			ret = new String[names.length + 1];
			ret[0] = "";
			for (int i = 0; i < names.length; i++)
				ret[i + 1] = names[i];
		}
		return ret;
	}
	
	private void init(){
		IAdaptable adapt = getElement();
		pat = (Patient) adapt.getAdapter(Patient.class);
		
		List<PatientKassenData> datas = PatientKassenData.getByPatient(pat);
		if (datas.size() > 0)
			data = datas.get(0);
	}
	
	@Override
	public boolean performOk(){
		performApply();
		boolean ret = true;
		if (!SVNR.validateSVNumber(txtSVNr.getText()))
			ret = false;
		return ret;
	}
}

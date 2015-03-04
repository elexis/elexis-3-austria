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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.medevit.elexis.kassen.core.model.IPointsArea;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.PointsAreaFactory;

import com.tiff.common.ui.datepicker.DatePickerCombo;

public class EditPointsAreaDialog extends TitleAreaDialog {

	private IPointsArea area;
	private Class<? extends KassenLeistung> clazz;
	
	private Text areaDefinition;
	private Text valueDefinition;
	
	private DatePickerCombo fromDateCombo;
	private DatePickerCombo toDateCombo;
	
	public EditPointsAreaDialog(Shell parentShell, Class<? extends KassenLeistung> clazz) {
		super(parentShell);
		this.clazz = clazz;
	}
	
	public EditPointsAreaDialog(Shell parentShell, IPointsArea area, Class<? extends KassenLeistung> clazz) {
		super(parentShell);
		this.clazz = clazz;
		this.area = area;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);

		setTitle("Neuer Punktwertbreich");
		setMessage("Den neuen Punktwertbereich definieren.");
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		Composite areaComposite  = new Composite(composite, SWT.NONE);
		areaComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		areaComposite.setLayout(new FormLayout());
		
		fromDateCombo = new DatePickerCombo(areaComposite, SWT.BORDER);
		if(area != null)
			fromDateCombo.setDate(area.getValidFromDate());
		
		toDateCombo = new DatePickerCombo(areaComposite, SWT.BORDER);
		if(area != null && area.getValidToDate() != null)
			toDateCombo.setDate(area.getValidToDate());
		
		FormData fd = new FormData();
	    fd.top = new FormAttachment(0, 5);
	    fd.left = new FormAttachment(0, 5);
	    fd.right = new FormAttachment(50, -5);
	    fromDateCombo.setLayoutData(fd);
		
		fd = new FormData();
	    fd.top = new FormAttachment(0, 5);
	    fd.left = new FormAttachment(50, 5);
	    fd.right = new FormAttachment(100, -5);
	    toDateCombo.setLayoutData(fd);
	    
		Label lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Bereich: ");
		areaDefinition = new Text(areaComposite, SWT.BORDER);
		if(area != null)
			areaDefinition.setText(area.getAreaDefinition());
		
		fd = new FormData();
	    fd.top = new FormAttachment(fromDateCombo, 5);
	    fd.left = new FormAttachment(20, 5);
	    fd.right = new FormAttachment(100, -5);
	    areaDefinition.setLayoutData(fd);

		fd = new FormData();
	    fd.top = new FormAttachment(areaDefinition, 0, SWT.CENTER);
	    fd.left = new FormAttachment(0, 5);
	    fd.right = new FormAttachment(20, -5);
	    lbl.setLayoutData(fd);
	    
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Wert: ");
		valueDefinition = new Text(areaComposite, SWT.BORDER);
		if(area != null)
			valueDefinition.setText(area.getValue());
		
		fd = new FormData();
	    fd.top = new FormAttachment(areaDefinition, 5);
	    fd.left = new FormAttachment(20, 5);
	    fd.right = new FormAttachment(100, -5);
	    valueDefinition.setLayoutData(fd);

		fd = new FormData();
	    fd.top = new FormAttachment(valueDefinition, 0, SWT.CENTER);
	    fd.left = new FormAttachment(0, 5);
	    fd.right = new FormAttachment(20, -5);
	    lbl.setLayoutData(fd);
		
		return composite;
	}

	public IPointsArea getPointsArea() {
		return area;
	}
	
	@Override
	protected void okPressed() {
		try {
			if(area == null) {
				area = PointsAreaFactory.getInstance().getPointsAreaForString(
						areaDefinition.getText() + " " + valueDefinition.getText(), clazz);
				area.setValidFromDate(fromDateCombo.getDate());
				area.setValidToDate(toDateCombo.getDate());
				area.setUserDefined(true);
			} else {
				area.setAreaDefinition(areaDefinition.getText(), clazz);
				area.setMoneyValue(KassenLeistung.getDoubleForString(valueDefinition.getText()));
				area.setValidFromDate(fromDateCombo.getDate());
				area.setValidToDate(toDateCombo.getDate());
			}
		} catch (IllegalArgumentException ie) {
			setErrorMessage(ie.getMessage());
			return;
		} catch (ParseException pe) {
			setErrorMessage(pe.getMessage());
			return;
		}
		super.okPressed();
	}
}

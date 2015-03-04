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

import java.util.List;

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

import at.medevit.elexis.diag.eigene.model.Diagnose;
import ch.elexis.core.data.events.ElexisEventDispatcher;

public class EditEigeneDialog extends TitleAreaDialog {
	
	private Text code;
	private Text text;
	private Diagnose diagnose;
	
	public EditEigeneDialog(Shell parentShell, Diagnose diagnose){
		super(parentShell);
		this.diagnose = diagnose;
	}
	
	@Override
	protected Control createContents(Composite parent){
		Control contents = super.createContents(parent);
		
		if (diagnose == null) {
			setTitle("Neue Diagnose");
			setMessage("Die Daten der neuen Diagnose erfassen.");
		} else {
			setTitle("Diagnose editieren");
			setMessage("Die Daten der Diagnose ändern.");
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
		
		Label lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Code");
		code = new Text(areaComposite, SWT.BORDER);
		if (diagnose != null)
			code.setText(diagnose.getCode());
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		code.setLayoutData(fd);
		
		lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Text");
		text = new Text(areaComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		if (diagnose != null)
			text.setText(diagnose.getText());
		
		fd = new FormData();
		fd.top = new FormAttachment(code, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(20, -5);
		lbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(code, 5);
		fd.left = new FormAttachment(20, 5);
		fd.right = new FormAttachment(100, -5);
		fd.height = 100;
		text.setLayoutData(fd);
		
		return composite;
	}
	
	@Override
	protected void okPressed(){
		String textTxt = text.getText();
		String codeTxt = code.getText();
		
		if (codeTxt.length() == 0) {
			setErrorMessage("Diagnose code darf nicht leer sein.");
			return;
		}
		if (codeTxt.length() > 25) {
			setErrorMessage("Diagnose code darf max. 25 Zeichen lang sein.");
			return;
		}
		if (textTxt.length() > 255) {
			setErrorMessage("Diagnose Text darf max. 255 Zeichen lang sein.");
			return;
		}
		List<Diagnose> diags = Diagnose.getByCode(codeTxt);
		if (diags.size() > 0) {
			setErrorMessage("Diagnose mit dem code " + codeTxt + " bereits vorhanden.");
			return;
		}
		if (diagnose == null) {
			diagnose = new Diagnose(codeTxt, textTxt);
		} else {
			diagnose.setCode(codeTxt);
			diagnose.set(Diagnose.FLD_TEXT, textTxt);
		}
		
		ElexisEventDispatcher.reload(Diagnose.class);
		super.okPressed();
	}
}

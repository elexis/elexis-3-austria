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

import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExportCatalogDialog extends TitleAreaDialog {

	private FileDialog fileDialog;
	private Text fileTxt;
	private String filename;
	
	public ExportCatalogDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);

		setTitle("Leistungskatalog exportieren");
		setMessage("Bitte die Datei auswählen in die exportiert werden soll.");
		return contents;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		Composite areaComposite  = new Composite(composite, SWT.NONE);
		areaComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		
		areaComposite.setLayout(new FormLayout());

		Button openFileDialog = new Button(areaComposite, SWT.PUSH);
		openFileDialog.setText("Suchen ...");
		setButtonLayoutData(openFileDialog);
		openFileDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileDialog = new FileDialog(parent.getShell());
				fileTxt.setText(fileDialog.open());
			}
		});
		
		Label lbl = new Label(areaComposite, SWT.NONE);
		lbl.setText("Datei: ");
		
		fileTxt = new Text(areaComposite, SWT.BORDER);
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		openFileDialog.setLayoutData(fd);
		
		fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.top = new FormAttachment(openFileDialog, 0, SWT.CENTER);
		lbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(openFileDialog, 0, SWT.CENTER);
		fd.right = new FormAttachment(openFileDialog, -5);
		fd.left = new FormAttachment(lbl, 5);
		fileTxt.setLayoutData(fd);
		
		return composite;
	}

	public String getFilename() {
		return filename;
	}
	
	@Override
	protected void okPressed() {
		filename = fileTxt.getText();
		if(!(filename != null && filename.length() > 0)) {
			setErrorMessage("Kein Dateiname ausgewählt.");
			return;
		}
		super.okPressed();
	}
}

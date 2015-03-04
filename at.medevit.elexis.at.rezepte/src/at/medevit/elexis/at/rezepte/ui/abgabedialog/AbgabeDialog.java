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
package at.medevit.elexis.at.rezepte.ui.abgabedialog;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import at.medevit.elexis.at.rezepte.model.Verschreibung;
import ch.elexis.data.Patient;

import com.swtdesigner.ResourceManager;

public class AbgabeDialog extends TitleAreaDialog {

	private TableViewer tableViewer;
	private List<Verschreibung> vrs;
	private Patient patient;
	private boolean verordnungsdatumDrucken = true;

	public AbgabeDialog(Shell parentShell, List<Verschreibung> vrs, Patient p) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.vrs = vrs;
		this.patient = p;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public AbgabeDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(ResourceManager.getPluginImage(
				"at.medevit.elexis.at.rezepte", "rsc/icons/banner.png"));
		setMessage("Bitte Abgabemengen für " + patient.getName() + " "
				+ patient.getVorname() + " festlegen.");
		setTitle("Rezept");
		
		Composite area = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) area.getLayout();
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
//		container.setBounds(10, 10, 430, 138);
		TableColumnLayout tcl = new TableColumnLayout();
		container.setLayout(tcl);

		tableViewer = new TableViewer(container, SWT.BORDER);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		tableViewerColumn.setEditingSupport(new AbgabeDialogEditingSupport(tableViewer));

		TableColumn tblclmnOriginalpackung = tableViewerColumn.getColumn();
		tblclmnOriginalpackung.setText("OP");
		tcl.setColumnData(tblclmnOriginalpackung, new ColumnWeightData(20, 100));
		//----
		TableViewerColumn tableViewerColName = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColName.getColumn();
		tblclmnName.setText("Name");
		tcl.setColumnData(tblclmnName, new ColumnWeightData(45, 200));
		//----
		TableViewerColumn tableViewerColSig = new TableViewerColumn(
				tableViewer, SWT.NONE);
		TableColumn tblclmnSignatur = tableViewerColSig.getColumn();
		tblclmnSignatur.setWidth(127);
		tblclmnSignatur.setText("Signatur");
		tcl.setColumnData(tblclmnSignatur, new ColumnWeightData(35, 127));
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new AbgabeDialogLabelProvider());
		tableViewer.setInput(vrs);

		// Set the first element to be edited
		tableViewer.editElement(tableViewer.getElementAt(0), 0);
		
		Group optionGroup = new Group(area, SWT.None);
		optionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		optionGroup.setLayout(new GridLayout(2, false));
		
		Button btnRezeptgebhrenbefreiung = new Button(optionGroup, SWT.CHECK);
		btnRezeptgebhrenbefreiung.setEnabled(false);
		btnRezeptgebhrenbefreiung.setText("Rezeptgebührenbefreiung");
		
		Button btnVerordnungsdatumDrucken = new Button(optionGroup, SWT.CHECK);
		btnVerordnungsdatumDrucken.setSelection(true);
		btnVerordnungsdatumDrucken.setText("Verordnungs&datum drucken");
		btnVerordnungsdatumDrucken.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				verordnungsdatumDrucken = ((Button) e.widget).getSelection();
			}
		});
		

		return area;
	}

	
	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button buttonCancel = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
		buttonCancel.setText("Abbrechen");
		
		Button buttonDrucken = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		buttonDrucken.setToolTipText("Drucken auf Rezeptdrucker");
		buttonDrucken.setText("Drucken");
	}

	public boolean isVerordnungsdatumDrucken(){
		return verordnungsdatumDrucken;
	}	
}

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
package at.medevit.elexis.befuem.ui.dialogs;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.befuem.contextservice.finding.LabResultNonUniqueException;
import at.medevit.elexis.befuem.contextservice.finding.LabResultTest;
import at.medevit.elexis.befuem.ui.Activator;
import at.medevit.elexis.befuem.ui.Messages;
import at.medevit.elexis.befuem.ui.dialogs.model.LabItemComparator;
import at.medevit.elexis.befuem.ui.dialogs.model.LabItemNameEditingSupport;
import at.medevit.elexis.befuem.ui.dialogs.model.LabItemRefFemaleEditingSupport;
import at.medevit.elexis.befuem.ui.dialogs.model.LabItemRefMaleEditingSupport;
import at.medevit.elexis.befuem.ui.dialogs.model.LabItemShortNameEditingSupport;
import at.medevit.elexis.befuem.ui.dialogs.model.LabItemSortSelectionListener;
import at.medevit.elexis.befuem.ui.dialogs.model.LabItemUnitEditingSupport;
import ch.elexis.data.Kontakt;
import ch.elexis.data.LabItem;
import ch.elexis.data.Patient;

public class LabItemMapDialog extends TitleAreaDialog {
	private LabResultTest test;
	private Composite testView;
	private Kontakt labor;
	private Patient patient;
	private TableViewer viewer;
	
	private static Color colorYellow = Display.getCurrent().getSystemColor(
			SWT.COLOR_YELLOW);
	
	public LabItemMapDialog(Shell parentShell, LabResultTest test, Kontakt labor, Patient patient) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.test = test;
		this.labor = labor;
		this.patient = patient;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		Composite ret = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		ret.setLayoutData(gridData);
		
		ret.setLayout(new FormLayout());
		
		createLabResultTestView(ret);
		createViewer(ret);
		
		FormData fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.top = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		testView.setLayoutData(fd);
		
		fd = new FormData();
		fd.left = new FormAttachment(0, 5);
		fd.top = new FormAttachment(testView, 5);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(100, -5);
		viewer.getControl().setLayoutData(fd);
		
		return parent;
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Map Lab Item"); //$NON-NLS-1$
		setTitle(Messages.LabItemMapDialog_Title);
		setMessage(Messages.LabItemMapDialog_Message);
	}

	private void createLabResultTestView(Composite parent) {
		testView = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		testView.setLayout(layout);
		
		String titelStr = Messages.LabItemMapDialog_LabItemParameters;
		StyledText titel = new StyledText(testView, SWT.NONE);
		titel.setBackground(testView.getBackground());
		titel.setText(titelStr);
		StyleRange styleRange = new StyleRange();
		styleRange.start = 0;
		styleRange.length = titelStr.length();
		styleRange.fontStyle = SWT.BOLD;
		titel.setStyleRange(styleRange);
		new Label(testView, SWT.NONE);
		
		new Label(testView, SWT.NONE).setText(Messages.LabItemMapDialog_LabItemDescription);
		new Text(testView, SWT.READ_ONLY).setText(
				(test.getItemDescription().length() < 1) ?
				test.getItemShortDescription() : test.getItemDescription());;
		
		new Label(testView, SWT.NONE).setText(Messages.LabItemMapDialog_LabItemShortDescription);
		new Text(testView, SWT.READ_ONLY).setText(
				test.getItemShortDescription());
		
		new Label(testView, SWT.NONE).setText(Messages.LabItemMapDialog_LabItemUnit);
		new Text(testView, SWT.READ_ONLY).setText(
				test.getDataUnit());
		
		Label label = new Label(testView, SWT.NONE);
		if(patient.getGeschlecht().equalsIgnoreCase("w")) { //$NON-NLS-1$
			label.setText(Messages.LabItemMapDialog_LabItemRefW);
		}
		else {
			label.setText(Messages.LabItemMapDialog_LabItemRefM);
		}
		new Text(testView, SWT.READ_ONLY).setText(test.getRefString());
	}
	
	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		viewer.setInput(LabItem.getLabItems(labor.getId(), null, null, null, null));
		// Set the sorter for the table
		viewer.setComparator(new LabItemComparator());
	}
	
	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { Messages.LabItemMapDialog_LabItemDescriptionColumnText, Messages.LabItemMapDialog_LabItemShortDescriptionColumnText, Messages.LabItemMapDialog_LabItemUnitColumnText, Messages.LabItemMapDialog_LabItemRefMColumnText, Messages.LabItemMapDialog_LabItemRefWColumnText };
		int[] bounds = { 100, 100, 100, 100, 100 };
		// NAME
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				LabItem item = (LabItem) cell.getElement();
				String cellText = item.getName();
				cell.setText(cellText);
				// highlight matching cells
				if (test.getItemDescription().equalsIgnoreCase(cellText)) {
					cell.setBackground(colorYellow);
				}
				super.update(cell);

			}
		});
		col.setEditingSupport(new LabItemNameEditingSupport(viewer));
		col.getColumn().addSelectionListener(
				new LabItemSortSelectionListener(
						LabItemComparator.PROPERTY_NAME,
						col.getColumn(),
						viewer));
		// SHORTNAME
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				LabItem item = (LabItem) cell.getElement();
				String cellText = item.getKuerzel();
				cell.setText(cellText);
				// highlight matching cells
				if (test.getItemShortDescription().equalsIgnoreCase(cellText)) {
					cell.setBackground(colorYellow);
				}
				super.update(cell);

			}
		});
		col.setEditingSupport(new LabItemShortNameEditingSupport(viewer));
		col.getColumn().addSelectionListener(
				new LabItemSortSelectionListener(
						LabItemComparator.PROPERTY_SHORTNAME,
						col.getColumn(),
						viewer));
		// UNIT
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				LabItem item = (LabItem) element;
				return item.getEinheit();
			}
		});
		col.setEditingSupport(new LabItemUnitEditingSupport(viewer));
		col.getColumn().addSelectionListener(
				new LabItemSortSelectionListener(
						LabItemComparator.PROPERTY_UNIT,
						col.getColumn(),
						viewer));
		// REFM
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				LabItem item = (LabItem) element;
				return item.getRefM();
			}
		});
		col.setEditingSupport(new LabItemRefMaleEditingSupport(viewer));
		col.getColumn().addSelectionListener(
				new LabItemSortSelectionListener(
						LabItemComparator.PROPERTY_REFM,
						col.getColumn(),
						viewer));
		// REFW
		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				LabItem item = (LabItem) element;
				return item.getRefW();
			}
		});
		col.setEditingSupport(new LabItemRefFemaleEditingSupport(viewer));
		col.getColumn().addSelectionListener(
				new LabItemSortSelectionListener(
						LabItemComparator.PROPERTY_REFW,
						col.getColumn(),
						viewer));
	}
	
	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	boolean isItemResolved() {
		try {
			return test.getLabItem(labor, patient.getGeschlecht()) != null;
		} catch (LabResultNonUniqueException e) {
			Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, Messages.LabItemMapDialog_NonUniqueLabResultError);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
			return false;
		}
	}
	
	@Override
	protected void okPressed() {
		if(!isItemResolved()) {
			setErrorMessage(Messages.LabItemMapDialog_NotResolvedError);
			return;
		}
		super.okPressed();
	}
}

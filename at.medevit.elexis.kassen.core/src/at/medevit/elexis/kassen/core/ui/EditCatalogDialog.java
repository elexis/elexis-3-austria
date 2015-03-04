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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.kassen.core.Activator;
import at.medevit.elexis.kassen.core.importer.CsvLeistungsExporter;
import at.medevit.elexis.kassen.core.model.DateRange;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.LeistungBean;
import ch.elexis.data.PersistentObject;

public class EditCatalogDialog extends TitleAreaDialog {
	
	TreeViewer viewer;
	private Text txtFilter;
	private KassenLeistungPositionTitleFilter filterPositionTitle;
	private KassenLeistungTreeContentProvider treeContentProvider;
	
	private Class<? extends KassenLeistung> clazz;
	
	public EditCatalogDialog(Shell parentShell, Class<? extends KassenLeistung> clazz){
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.clazz = clazz;
	}
	
	@Override
	protected Control createContents(Composite parent){
		Control contents = super.createContents(parent);
		
		setTitle("Leistungskatalog");
		setMessage("Den Leistungskatalog ändern.");
		return contents;
	}
	
	@Override
	protected Control createDialogArea(final Composite parent){
		Composite composite = (Composite) super.createDialogArea(parent);
		
		Composite areaComposite = new Composite(composite, SWT.NONE);
		areaComposite
			.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		
		areaComposite.setLayout(new FormLayout());
		
		Label lblFilter = new Label(areaComposite, SWT.NONE);
		lblFilter.setText("Filter: ");
		
		txtFilter = new Text(areaComposite, SWT.BORDER | SWT.SEARCH);
		txtFilter.setText(""); //$NON-NLS-1$
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 5);
		lblFilter.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(lblFilter, 5);
		fd.right = new FormAttachment(100, -5);
		txtFilter.setLayoutData(fd);
		
		viewer = new TreeViewer(areaComposite, SWT.BORDER | SWT.FULL_SELECTION);
		Tree tree = viewer.getTree();
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		
		fd = new FormData();
		fd.top = new FormAttachment(txtFilter, 5);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		fd.bottom = new FormAttachment(100, 0);
		tree.setLayoutData(fd);
		
		treeContentProvider = new KassenLeistungTreeContentProvider(clazz);
		viewer.setContentProvider(treeContentProvider);
		viewer.setComparator(new KassenLeistungComparator());
		filterPositionTitle = new KassenLeistungPositionTitleFilter();
		viewer.addFilter(filterPositionTitle);
		txtFilter.addKeyListener(new FilterKeyListener(txtFilter, viewer));
		
// viewer.setLabelProvider(new KassenLeistungLabelProvider());
		
		TreeViewerColumn viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		TreeColumn column = viewerColumn.getColumn();
		column.setText("GruppeId");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				String id = leistung.get(KassenLeistung.FLD_GRUPPEID);
				return id;
			}
		});
		
		viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("PositionGruppenId");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				String id = leistung.get(KassenLeistung.FLD_POSITIONGRUPPENID);
				return id;
			}
		});
		
		viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("PositionId");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				String id = leistung.get(KassenLeistung.FLD_POSITIONID);
				return id;
			}
		});
		
		viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("ValidFrom");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				String id = leistung.getValidRange().getFromDateAsString("dd.MM.yyyy");
				return id;
			}
		});
		
		viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("ValidTo");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				String id = leistung.getValidRange().getToDateAsString("dd.MM.yyyy");
				return id;
			}
		});
		
		viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("PositionTitle");
		column.setWidth(250);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				String id = leistung.get(KassenLeistung.FLD_POSITIONTITLE);
				return id;
			}
		});
		
		viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("PositionPunktWert");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				String id = leistung.get(KassenLeistung.FLD_POSITIONPUNKTWERT);
				return id;
			}
		});
		
		viewerColumn = new TreeViewerColumn(viewer, SWT.NONE);
		column = viewerColumn.getColumn();
		column.setText("PositionGeldWert");
		column.setWidth(75);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element){
				KassenLeistung leistung = (KassenLeistung) element;
				String id = leistung.get(KassenLeistung.FLD_POSITIONGELDWERT);
				return id;
			}
		});
		
		// First we create a menu Manager
		MenuManager menuManager = new MenuManager();
		// Then add the actions for KassenLeistungen
		menuManager.add(new Action() {
			@Override
			public String getText(){
				return "add";
			}
			
			@Override
			public void run(){
				EditKassenLeistungDialog dialog =
					new EditKassenLeistungDialog(parent.getShell(), null, clazz);
				dialog.create();
				if (dialog.open() == Window.OK) {
					try {
						Constructor<? extends KassenLeistung> cons =
							clazz.getConstructor(LeistungBean.class);
						cons.newInstance(dialog.getLeistungBean());
					} catch (InstantiationException ie) {
						StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Could not create new KassenLeistung.", ie), StatusManager.SHOW);
					} catch (IllegalAccessException ie) {
						StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Could not create new KassenLeistung.", ie), StatusManager.SHOW);
					} catch (SecurityException ie) {
						StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Could not create new KassenLeistung.", ie), StatusManager.SHOW);
					} catch (NoSuchMethodException ie) {
						StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Could not create new KassenLeistung.", ie), StatusManager.SHOW);
					} catch (IllegalArgumentException ie) {
						StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Could not create new KassenLeistung.", ie), StatusManager.SHOW);
					} catch (InvocationTargetException ie) {
						StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Could not create new KassenLeistung.", ie), StatusManager.SHOW);
					}
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
					KassenLeistung sel = (KassenLeistung) strucSelection.getFirstElement();
					
					EditKassenLeistungDialog dialog =
						new EditKassenLeistungDialog(parent.getShell(), sel.getBeanForLeistung(),
							clazz);
					dialog.create();
					if (dialog.open() == Window.OK) {
						if (sel.isGroup()) {
							// move the whole group if the id changes
							LeistungBean newGroup = dialog.getLeistungBean();
							String newGroupId = newGroup.getGruppeId();
							if (!sel.getGroup().equalsIgnoreCase(newGroupId)) {
								if (moveGroup(sel, newGroupId))
									sel.setLeistungFromBean(dialog.getLeistungBean());
							} else {
								sel.setLeistungFromBean(dialog.getLeistungBean());
							}
							
							// set the valid from/to date fields for the whole group on change
							try {
								String from = newGroup.getValidFromDate();
								String to = newGroup.getValidToDate();
								
								Date fromDate = KassenLeistung.getDateForString(from);
								Date toDate = null;
								DateRange newValidDateRange = null;
								if (to != null) {
									try {
										toDate = KassenLeistung.getDateForString(to);
									} catch (ParseException e) {}
								}
								if (toDate != null)
									newValidDateRange = new DateRange(fromDate, toDate);
								else
									newValidDateRange = new DateRange(fromDate);
								
								setGroupDateRange(sel, newValidDateRange);
							} catch (ParseException e) {}
							
						} else {
							// write the new values
							sel.setLeistungFromBean(dialog.getLeistungBean());
						}
						viewer.refresh();
					}
				}
			}
			
			private void setGroupDateRange(KassenLeistung source, DateRange newDateRange){
				List<? extends KassenLeistung> children = source.getChildren();
				for (KassenLeistung child : children) {
					if (child.isGroup()) {
						setGroupDateRange(child, newDateRange);
						child.setValidRange(newDateRange);
					} else {
						child.setValidRange(newDateRange);
					}
				}
			}
			
			private boolean moveGroup(KassenLeistung source, String newGroupId){
				String oldGroupId = source.getGroup();
				int newDotIdx = newGroupId.indexOf('.');
				int oldDotIdx = oldGroupId.indexOf('.');
				boolean oldIsSubGroup = oldDotIdx != -1;
				boolean newIsSubGroup = newDotIdx != -1;
				
				if (oldIsSubGroup && newIsSubGroup) {
					List<? extends KassenLeistung> positionen =
						KassenLeistung.getCurrentLeistungenByIds(null, oldGroupId, null, null,
							clazz);
					for (KassenLeistung position : positionen)
						position.set(KassenLeistung.FLD_POSITIONGRUPPENID, newGroupId);
					
					return true;
				} else if (!oldIsSubGroup && !newIsSubGroup) {
					List<? extends KassenLeistung> children = source.getChildren();
					
					for (KassenLeistung child : children) {
						if (child.isGroup()) {
							String[] childGrpParts = child.getGroup().split("\\.");
							String newGrpId = newGroupId + "." + childGrpParts[1];
							if (moveGroup(child, newGrpId))
								child.set(KassenLeistung.FLD_GRUPPEID, newGrpId);
						} else {
							child.set(KassenLeistung.FLD_POSITIONGRUPPENID, newGroupId);
						}
					}
					return true;
				}
				
				return false;
			}
		});
		
		menuManager.add(new Action() {
			@Override
			public String getText(){
				return "remove";
			}
			
			@Override
			public void run(){
				ISelection selection = viewer.getSelection();
				if (selection != null & selection instanceof IStructuredSelection) {
					IStructuredSelection strucSelection = (IStructuredSelection) selection;
					KassenLeistung sel = (KassenLeistung) strucSelection.getFirstElement();
					boolean confirm =
						MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Confirm",
							"Really delete " + sel.getLabel() + " ?");
					if (confirm) {
						sel.delete();
						viewer.refresh();
					}
				}
			}
		});
		
		// set the menu for the table viewer
		Menu menu = menuManager.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		
		viewer.setInput(treeContentProvider.getElements(null));
		viewer.refresh();
		return composite;
	}
	
	@Override
	protected Control createButtonBar(Composite parent){
		Composite composite = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.RIGHT, true, false));
		
		createButtons(composite);
		
		super.createButtonBar(composite);
		
		return composite;
	}
	
	private Composite createButtons(final Composite parent){
		Composite composite = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 0; // this is incremented by createButton
		layout.makeColumnsEqualWidth = true;
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.horizontalSpacing =
			convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		composite.setLayout(layout);
		GridData data = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		composite.setLayoutData(data);
		
		// increment the number of columns in the button bar
		((GridLayout) composite.getLayout()).numColumns++;
		Button export = new Button(composite, SWT.PUSH);
		export.setText("Export");
		setButtonLayoutData(export);
		export.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e){
				final ExportCatalogDialog dialog = new ExportCatalogDialog(parent.getShell());
				dialog.create();
				if (dialog.open() == Window.OK) {
					ProgressMonitorDialog progress = new ProgressMonitorDialog(parent.getShell());
					try {
						progress.run(true, false, new IRunnableWithProgress() {
							@Override
							public void run(IProgressMonitor monitor){
								monitor.beginTask("Leistungskatalog export",
									IProgressMonitor.UNKNOWN);
								OutputStreamWriter writer = null;
								try {
									List<KassenLeistung> leistungen =
										KassenLeistung.getAllCurrentLeistungen(clazz);
									writer =
										new OutputStreamWriter(new FileOutputStream(new File(dialog
											.getFilename())), "UTF-8");
									CsvLeistungsExporter.writeLeistungenAsCsvToStream(leistungen,
										writer);
								} catch (IOException fe) {
									StatusManager.getManager().handle(
										new Status(IStatus.ERROR, Activator.PLUGIN_ID,
											"IO Error during export.", fe), StatusManager.SHOW);
								} finally {
									if (writer != null) {
										try {
											writer.close();
										} catch (IOException e1) {
											// ignore exception on close ...
										}
									}
								}
								monitor.done();
							}
						});
					} catch (InvocationTargetException ie) {
						StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Could not execute export.", ie), StatusManager.SHOW);
					} catch (InterruptedException ie) {
						StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								"Could not execute export.", ie), StatusManager.SHOW);
					}
				}
			}
		});
		
		// increment the number of columns in the button bar
		((GridLayout) composite.getLayout()).numColumns++;
		Button undelete = new Button(composite, SWT.PUSH);
		undelete.setText("Undelete All");
		setButtonLayoutData(undelete);
		undelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e){
				boolean confirm =
					MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "Confirm",
						"Really undelete all ?");
				if (confirm) {
					KassenLeistung root = KassenLeistung.getCurrentRootLeistungen(clazz).get(0);
					String tablename = root.getKassenLeistungTableName();
					PersistentObject.getConnection().exec(
						"update " + tablename + " set deleted='0' where deleted='1'");
					viewer.refresh();
				}
			}
		});
		return composite;
	}
	
	private class FilterKeyListener extends KeyAdapter {
		private Text text;
		private StructuredViewer viewer;
		
		FilterKeyListener(Text filterTxt, StructuredViewer viewer){
			text = filterTxt;
			this.viewer = viewer;
		}
		
		public void keyReleased(KeyEvent ke){
			String txt = text.getText();
			if (txt.length() > 1) {
				filterPositionTitle.setSearchText(txt);
				viewer.getControl().setRedraw(false);
				viewer.refresh();
				viewer.getControl().setRedraw(true);
				// make sure results are seen
				if (viewer instanceof TreeViewer) {
					((TreeViewer) viewer).expandAll();
				}
			} else {
				filterPositionTitle.setSearchText(null);
				viewer.getControl().setRedraw(false);
				viewer.refresh();
				viewer.getControl().setRedraw(true);
			}
		}
	}
}

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

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Typ;
import at.medevit.elexis.befuem.contextservice.finding.AbstractFindingContent;
import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.LabContent;
import at.medevit.elexis.befuem.contextservice.finding.LabResultNonUniqueException;
import at.medevit.elexis.befuem.contextservice.finding.LabResultTest;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.ui.Activator;
import at.medevit.elexis.befuem.ui.Messages;
import at.medevit.elexis.befuem.ui.dialogs.model.ElexisFindingTreeContentProvider;
import at.medevit.elexis.befuem.ui.labelprovider.ElexisFindingLabelProvider;
import ch.elexis.core.ui.dialogs.KontaktSelektor;
import ch.elexis.core.ui.laboratory.dialogs.EditLabItem;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Patient;


public class ClientFindingImportDialog extends TitleAreaDialog {

	NetClientFinding finding;
	
	Kontakt sender;
	Patient patient;
	
	boolean print = false;
	boolean reminder = false;
	
	Label senderLbl;
	Button senderSearchBtn;
	Label patientLbl;
	Button patientSearchBtn;
	
	TreeViewer contentViewer;
	
	Label keywordsLbl;
	Text keywordsTxt;
	
//	Button optionPrint;
//	Button optionReminder;
	
	Shell parentShell;
	
	public ClientFindingImportDialog(Shell parentShell, NetClientFinding finding) {
		super(parentShell);
		this.parentShell = parentShell;
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.finding = finding;
	}

	@Override
	protected Control createDialogArea(Composite parent){
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

		senderLbl = new Label(ret, SWT.NONE);
		if(finding.getFindingTyp() != Typ.UNDEFINED)
			sender = finding.getElexisFindings().get(0).getSource();
		
		if(testSender()) {
			senderLbl.setText(sender.getLabel());
		}
		senderSearchBtn = new Button(ret, SWT.PUSH);
		senderSearchBtn.setText(Messages.ClientFindingImportDialog_SelectSenderButton);
		senderSearchBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				KontaktSelektor ksl =
					new KontaktSelektor(parentShell, Kontakt.class, Messages.ClientFindingImportDialog_SelectSenderTitle,
						Messages.ClientFindingImportDialog_SelectSenderDescription,Kontakt.DEFAULT_SORT);
				if (ksl.open() == Dialog.OK) {
					sender = (Kontakt) ksl.getSelection();
					senderLbl.setText(sender.getLabel());
					senderLbl.getParent().layout();
					// pass sender selection to the finding
					// and try to resolve labresults
					java.util.List<ElexisFinding> ef = finding.getElexisFindings();
					for(ElexisFinding f : ef) {
						f.setSource(sender);
					}
					// try to resolve the finding
					try {
						finding.resolve();
					} catch (LabResultNonUniqueException lie) {
						Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, 
								Messages.ClientFindingImportDialog_LabResultNonUniqueWarning + lie.getItemsAsString());
						StatusManager.getManager().handle(status, StatusManager.SHOW);
					}
					contentViewer.refresh();
				} else {
					if(sender != null) {
						senderLbl.setText(sender.getLabel());
						senderLbl.getParent().layout();
					}
				}
			}
		});
		
		patientLbl = new Label(ret, SWT.NONE);
		if(finding.getFindingTyp() != Typ.UNDEFINED)
			patient = finding.getElexisFindings().get(0).getPatient();
		if(testPatient()) {
			patientLbl.setText(patient.getLabel());
		}
		patientSearchBtn = new Button(ret, SWT.PUSH);
		patientSearchBtn.setText(Messages.ClientFindingImportDialog_SelectPatientButton);
		patientSearchBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				KontaktSelektor ksl =
					new KontaktSelektor(parentShell, Patient.class, Messages.ClientFindingImportDialog_SelectPatientTitle,
						Messages.ClientFindingImportDialog_SelectPatientDescription,Kontakt.DEFAULT_SORT);
				if (ksl.open() == Dialog.OK) {
					patient = (Patient) ksl.getSelection();
					patientLbl.setText(patient.getLabel());
					patientLbl.getParent().layout();
					// pass patient selection to the finding ...
					java.util.List<ElexisFinding> ef = finding.getElexisFindings();
					for(ElexisFinding f : ef) {
						f.setPatient(patient);
					}
					// try to resolve the finding
					try {
						finding.resolve();
					} catch (LabResultNonUniqueException lie) {
						Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, 
								Messages.ClientFindingImportDialog_LabResultNonUniqueWarning + lie.getItemsAsString());
						StatusManager.getManager().handle(status, StatusManager.SHOW);
					}
					contentViewer.refresh();
				} else {
					if(patient != null) {
						patientLbl.setText(patient.getLabel());
						patientLbl.getParent().layout();
					}
				}
			}
		});
		
		createContentTreeView(ret);
		
		keywordsLbl = new Label(ret, SWT.NONE);
		keywordsLbl.setText("Stichwörter");
		keywordsTxt = new Text(ret, SWT.BORDER);
		keywordsTxt.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// update the keywords in the ElexisFinding
				setElexisFindingKeywords(keywordsTxt.getText());
			}
		});
		if(testSender()) {
			keywordsTxt.setText(sender.getLabel(true));
			setElexisFindingKeywords(keywordsTxt.getText());
		}
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		senderSearchBtn.setLayoutData(fd);

		fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 5);
		senderLbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(senderSearchBtn , 5);
		fd.right = new FormAttachment(100, -5);
		patientSearchBtn.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(senderSearchBtn , 5);
		fd.left = new FormAttachment(0, 5);
		patientLbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(patientSearchBtn, 5);
		fd.left = new FormAttachment(0, 5);
		keywordsLbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(patientSearchBtn, 5);
		fd.left = new FormAttachment(keywordsLbl, 5);
		fd.right = new FormAttachment(100, -5);
		keywordsTxt.setLayoutData(fd);
		
//		fd = new FormData();
//		fd.bottom = new FormAttachment(100 , -5);
//		fd.left = new FormAttachment(0, 5);
//		optionPrint.setLayoutData(fd);		
//
//		fd = new FormData();
//		fd.bottom = new FormAttachment(100 , -5);
//		fd.left = new FormAttachment(optionPrint, 10);
//		optionReminder.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(keywordsTxt , 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		fd.bottom = new FormAttachment(100, -5);
		contentViewer.getControl().setLayoutData(fd);
		
		new Label(parent, SWT.NONE);
		
		// First we create a menu Manager
		MenuManager menuManager = new MenuManager();
		addActions(menuManager);
		Menu menu = menuManager.createContextMenu(contentViewer.getTree());
		// Set the MenuManager
		contentViewer.getTree().setMenu(menu);
		
		return parent;
	}

	@Override
	public void create(){
		super.create();
		getShell().setText("Import"); //$NON-NLS-1$
		setTitle(Messages.ClientFindingImportDialog_Title);
		setMessage(Messages.ClientFindingImportDialog_Message);
	}
	
	@Override
	protected void okPressed() {
		if(!testSender()) {
			setErrorMessage(Messages.ClientFindingImportDialog_NoSenderSelectedError);
			return;
		} else if(!testPatient()) {
			setErrorMessage(Messages.ClientFindingImportDialog_NoPatientSelectedError);
			return;
		}
		if(finding.isResolved() == false) {
			setErrorMessage(Messages.ClientFindingImportDialog_FindingsNotResolvedError);
			return;
		}
//		print = optionPrint.getSelection();
//		reminder = optionReminder.getSelection();
		
		super.okPressed();
	}
	
	private boolean testSender() {
		return sender != null;
	}
	
	private boolean testPatient() {
		return patient != null;
	}
	
	private boolean testContactInfo() {
		return testSender() && testPatient();
	}
	
	private void createContentTreeView(Composite parent) {
		contentViewer = new TreeViewer(parent, SWT.BORDER);
		
		// dont create provider if the finding has no content
		if(finding.getElexisFindings().size() > 0) {
			contentViewer.setContentProvider(new ElexisFindingTreeContentProvider());
			contentViewer.setLabelProvider(new ElexisFindingLabelProvider());
			contentViewer.setInput(finding.getElexisFindings());
		}
	}
	
	private void addActions(MenuManager menuManager) {
		if(finding.getFindingTyp() == Typ.LAB) {
			menuManager.add(new Action() {
				@Override
				public String getText() {
					return Messages.ClientFindingImportDialog_ResolveActionCreate;
				}
	
				@Override
				public void run() {
					ISelection selection = contentViewer.getSelection();
					if (selection != null & selection instanceof IStructuredSelection) {
						IStructuredSelection strucSelection = (IStructuredSelection) selection;
						Object sel = strucSelection.getFirstElement();
						if(sel instanceof LabResultTest) {
							LabResultTest test = (LabResultTest)sel;
							EditLabItem dialog = new EditLabItem(contentViewer.getTree().getShell(), null);
							dialog.create();
							dialog.setTitelText(test.getItemDescription());
							dialog.setShortDescText(test.getItemShortDescription());
							if(patient.getGeschlecht().equalsIgnoreCase("w")) //$NON-NLS-1$
								dialog.setRefFText(test.getRefString());
							else if (patient.getGeschlecht().equalsIgnoreCase("m")) //$NON-NLS-1$
								dialog.setRefMText(test.getRefString());
							dialog.setUnitText(test.getDataUnit());
							if (dialog.open() == Window.OK) {
								contentViewer.refresh();
							}
						}
					}
					// try to resolve the finding
					try {
						finding.resolve();
					} catch (LabResultNonUniqueException lie) {
						Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, 
								Messages.ClientFindingImportDialog_LabResultNonUniqueWarning + lie.getItemsAsString());
						StatusManager.getManager().handle(status, StatusManager.SHOW);
					}
				}			
			});
			
			menuManager.add(new Action() {
				@Override
				public String getText() {
					return Messages.ClientFindingImportDialog_ResolveActionAutoCreate;
				}
	
				@Override
				public void run() {
					ISelection selection = contentViewer.getSelection();
					if (selection != null & selection instanceof IStructuredSelection) {
						IStructuredSelection strucSelection = (IStructuredSelection) selection;
						Object sel = strucSelection.getFirstElement();
						if(sel instanceof LabResultTest) {
							LabResultTest test = (LabResultTest)sel;
							if(sender == null) {
								setErrorMessage(Messages.ClientFindingImportDialog_NoSenderSelectedError);
								return;
							}
							test.createLabItem(sender, patient.getGeschlecht(), 0);
							contentViewer.refresh();
						} else if (sel instanceof ElexisFinding) {
							ElexisFinding finding = (ElexisFinding)sel;
							if(finding.getSource() == null) {
								setErrorMessage(Messages.ClientFindingImportDialog_NoSenderSelectedError);
								return;
							}
							List<AbstractFindingContent> contents = finding.getContent();
							for(AbstractFindingContent content : contents) {
								if(content.getTyp() == AbstractFindingContent.Typ.LAB) {
									List<LabResultTest> tests = ((LabContent)content).getResult().getTests();
									int sequenz = 0;
									for(LabResultTest test : tests) {
										test.createLabItem(sender, patient.getGeschlecht(), sequenz++);
									}
								}
							}
							contentViewer.refresh();
						}
					}
					// try to resolve the finding
					try {
						finding.resolve();
					} catch (LabResultNonUniqueException lie) {
						Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, 
								Messages.ClientFindingImportDialog_LabResultNonUniqueWarning + lie.getItemsAsString());
						StatusManager.getManager().handle(status, StatusManager.SHOW);
					}
				}			
			});
			
			menuManager.add(new Action() {
				@Override
				public String getText() {
					return Messages.ClientFindingImportDialog_ResolveActionEdit;
				}
	
				@Override
				public void run() {
					ISelection selection = contentViewer.getSelection();
					if (selection != null & selection instanceof IStructuredSelection) {
						IStructuredSelection strucSelection = (IStructuredSelection) selection;
						Object sel = strucSelection.getFirstElement();
						if(sel instanceof LabResultTest) {
							LabResultTest test = (LabResultTest)sel;
							if(!testContactInfo()) {
								MessageDialog.openError(getShell(), Messages.ClientFindingImportDialog_NotSelectedErrorDialogTitle, 
										Messages.ClientFindingImportDialog_NotSelectedErrorDialogDescription);
								return;
							}
							LabItemMapDialog dialog = new LabItemMapDialog(getShell(), test, sender, patient);
							dialog.create();
							if (dialog.open() == Window.OK) {
								contentViewer.refresh();
							}
						}
					}
					// try to resolve the finding
					try {
						finding.resolve();
					} catch (LabResultNonUniqueException lie) {
						Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, 
								Messages.ClientFindingImportDialog_LabResultNonUniqueWarning + lie.getItemsAsString());
						StatusManager.getManager().handle(status, StatusManager.SHOW);
					}
				}			
			});
		}
	}
	
	private void setElexisFindingKeywords(String keywords) {
		List<ElexisFinding> efindings = finding.getElexisFindings();
		for(ElexisFinding efinding : efindings)
			efinding.setKeywords(keywords);
	}
}

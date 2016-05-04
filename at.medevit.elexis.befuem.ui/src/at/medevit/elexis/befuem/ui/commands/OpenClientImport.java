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
package at.medevit.elexis.befuem.ui.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Typ;
import at.medevit.elexis.befuem.contextservice.finding.AbstractFindingContent;
import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.LabContent;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.ui.Activator;
import at.medevit.elexis.befuem.ui.Messages;
import at.medevit.elexis.befuem.ui.dialogs.ClientFindingImportDialog;
import at.medevit.elexis.befuem.ui.views.BefuemView;
import at.medevit.elexis.formattedoutput.FormattedOutputException;
import ch.elexis.core.data.interfaces.text.IOpaqueDocument;
import ch.elexis.core.data.services.GlobalServiceDescriptors;
import ch.elexis.core.data.services.IDocumentManager;
import ch.elexis.core.data.util.Extensions;
import ch.elexis.core.exceptions.ElexisException;
import ch.elexis.core.ui.text.GenericDocument;
import ch.rgw.tools.TimeTool;

public class OpenClientImport extends AbstractHandler {
	public static final String ID = "at.medevit.elexis.befuem.ui.command.openClientImport"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		BefuemView view = (BefuemView) HandlerUtil.getActiveWorkbenchWindow(event)
		.getActivePage().findView(BefuemView.ID);

		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			if(strucSelection==null) {
				return null;
			}
			Object sel = strucSelection.getFirstElement();
			// create the view with the selected finding
			if (sel != null && sel instanceof NetClientFinding) {
				NetClientFinding finding = (NetClientFinding) sel;
				if(finding.getFindingTyp() == Typ.LAB || finding.getFindingTyp() == Typ.FINDING ||
						finding.getFindingTyp() == Typ.FINDING_REQ) {
					ClientFindingImportDialog dialog = new ClientFindingImportDialog(shell, finding);
					dialog.create();
					if (dialog.open() == Window.OK) {
						// get the data from the view and create all necessary Elexis Objects
						if(finding.isResolved()) {
							importFinding(finding);
						} else {
							Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, finding.getUnresolvedAsString());
							StatusManager.getManager().handle(status, StatusManager.SHOW);
						}
						view.updateInboxViewer(false);
						view.updateArchiveViewer(false);
					}
				} else {
					Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, Messages.OpenClientImport_UnknownFindingTyp);
					StatusManager.getManager().handle(status, StatusManager.SHOW);
				}
			}
		}
		return null;
	}
	
	private void importFinding(NetClientFinding finding) {
		try {
			int findingCount = finding.getElexisFindings().size();
			if (findingCount > 0) {
				IDocumentManager docmgr = (IDocumentManager) Extensions.findBestService(GlobalServiceDescriptors.DOCUMENT_MANAGEMENT);
				
				for (int i = 0; i < findingCount; i++) {
					// create omnivore entries for the imported finding
					ElexisFinding efinding = finding.getElexisFindings().get(i);
					ByteArrayOutputStream pdf = efinding.getPdfFile();
					if (finding.getFindingTyp() == Typ.LAB && docmgr != null) {
						IOpaqueDocument doc = new GenericDocument(
								efinding.getPatient(),
								efinding.getPatient().getName() + " " + //$NON-NLS-1$
								efinding.getPatient().getVorname() + ", Lab.pdf", //$NON-NLS-1$
								null,
								pdf.toByteArray(),
								new TimeTool().toString(TimeTool.DATE_GER),
								efinding.getKeywords(), //$NON-NLS-1$
								"pdf"); //$NON-NLS-1$
						docmgr.addDocument(doc);
					} else if (finding.getFindingTyp() == Typ.FINDING && docmgr != null) {
						IOpaqueDocument doc = new GenericDocument(
								efinding.getPatient(),
								efinding.getPatient().getName() + " " + //$NON-NLS-1$
								efinding.getPatient().getVorname() + ", Finding.pdf", //$NON-NLS-1$
								null,
								pdf.toByteArray(),
								new TimeTool().toString(TimeTool.DATE_GER),
								efinding.getKeywords(), //$NON-NLS-1$
								"pdf"); //$NON-NLS-1$
						docmgr.addDocument(doc);
					}  else if (finding.getFindingTyp() == Typ.FINDING_REQ && docmgr != null) {
						IOpaqueDocument doc = new GenericDocument(
								efinding.getPatient(),
								efinding.getPatient().getName() + " " + //$NON-NLS-1$
								efinding.getPatient().getVorname() + ", Request.pdf", //$NON-NLS-1$
								null,
								pdf.toByteArray(),
								new TimeTool().toString(TimeTool.DATE_GER),
								efinding.getKeywords(), //$NON-NLS-1$
								"pdf"); //$NON-NLS-1$
						docmgr.addDocument(doc);
					}
					// create labresults for the imported finding
					if (finding.getFindingTyp() == Typ.LAB) {
						List<AbstractFindingContent> content = efinding
								.getContent();
						for (AbstractFindingContent contentObj : content) {
							if (contentObj.getTyp() == AbstractFindingContent.Typ.LAB) {
								LabContent labContent = (LabContent)contentObj;
								labContent.createLabResults(
										efinding.getPatient(),
										labContent.getResult().getResultDate());
							}
						}
					}
					finding.setPatient(efinding.getPatient());
				}
				finding.setImported(true);
				finding.setArchived(true);
			}
		} catch (IOException e) {
			Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.OpenClientImport_FailedMessage, e);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		} catch (FormattedOutputException e) {
			Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.OpenClientImport_DocumentPDFFailedMessage, e);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		} catch (ElexisException e) {
			Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, Messages.OpenClientImport_DocumentFailedMessage, e);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}
	}
}

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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import at.medevit.elexis.befuem.contextservice.networkparticipant.NetworkParticipant;
import at.medevit.elexis.befuem.ui.Messages;
import at.medevit.elexis.befuem.ui.views.BefuemView;
import ch.elexis.core.ui.dialogs.KontaktSelektor;
import ch.elexis.data.Kontakt;

public class SetParticipantKontakt extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		BefuemView view = (BefuemView) HandlerUtil.getActiveWorkbenchWindow(event)
		.getActivePage().findView(BefuemView.ID);

		ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getSelection();
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			Object sel = strucSelection.getFirstElement();
			// create the dialog for the selected Participant
			if (sel != null && sel instanceof NetworkParticipant) {
				NetworkParticipant participant = (NetworkParticipant) sel;
				KontaktSelektor ksl =
					new KontaktSelektor(shell, Kontakt.class, Messages.SetParticipantKontakt_SelectorTitle,
						Messages.SetParticipantKontakt_SelectorDescription, Kontakt.DEFAULT_SORT);
				if (ksl.open() == Dialog.OK) {
					participant.setKontakt((Kontakt) ksl.getSelection());
				}
				view.updateAdressbookViewer();				
			}
		}
		return null;
	}

}

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
package at.medevit.elexis.at.rezepte.commands;

import java.util.Arrays;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import at.medevit.elexis.at.rezepte.ui.FixMediDisplay;
import at.medevit.elexis.at.rezepte.ui.FixMediOrderingComparator;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.data.Patient;
import ch.elexis.data.Prescription;

public class CopyPrescriptionClipboard extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException{
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IViewPart view = page.findView(FixMediDisplay.ID);
		
		Clipboard cb = new Clipboard(PlatformUI.getWorkbench().getDisplay());
		ISelection selection = view.getSite().getSelectionProvider().getSelection();
		// No specific prescription selected, so copy all of them
		if(selection.isEmpty()) {
			Patient act=ElexisEventDispatcher.getSelectedPatient();
			Prescription[] pres = act.getFixmedikation();
			Arrays.sort(pres, new FixMediOrderingComparator());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < pres.length; i++) {
				sb.append(prescriptionToString(pres[i])+"\n");
			}
			TextTransfer textTransfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { sb.toString() },
					new Transfer[] { textTransfer });
			return null;
		}
		// TODO: There where some selected, copy only them!
		
		return null;
	}
	
	private String prescriptionToString(Prescription p) {
		return p.getArtikel().getName()+" "+p.getDosis()+" "+p.getBemerkung();
	}
}

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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.at.rezepte.Activator;
import at.medevit.elexis.at.rezepte.ui.FixMediOrderingComparator;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.core.ui.views.RezeptBlatt;
import ch.elexis.data.Patient;
import ch.elexis.data.Prescription;
import ch.elexis.data.Rezept;
import ch.rgw.tools.TimeTool;

public class RezeptDruckenOOffice extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException{
		Patient pat = ElexisEventDispatcher.getSelectedPatient();
		if (pat == null) {
			return null;
		}
		Prescription[] pres = pat.getFixmedikation();
		Arrays.sort(pres, new FixMediOrderingComparator());
		Rezept rezept = new Rezept(pat);
		for (Prescription p : pres) {
			if (!p.getEndTime().isEmpty()) {
				TimeTool endTime = new TimeTool(p.getEndTime());
				if (endTime.isBefore(new TimeTool())) {
					continue;
				}
			}
			//Requires new Prescription as direct references are passed
			//and the list of Fixmedikationen is else erased!
			rezept.addPrescription(new Prescription(p));
		}
		try {
			RezeptBlatt rpb = (RezeptBlatt) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().showView(RezeptBlatt.ID);
			rpb.createRezept(rezept);
		} catch (PartInitException e) {
			Status status =
				new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}
		return null;
	}
}

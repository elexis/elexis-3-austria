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
package at.medevit.elexis.kassen.vaeb.importer;

import java.io.FileNotFoundException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;

import at.medevit.elexis.kassen.core.importer.CsvLeistungsImporter;
import at.medevit.elexis.kassen.core.model.LeistungBean;
import at.medevit.elexis.kassen.vaeb.Activator;
import at.medevit.elexis.kassen.vaeb.model.VaebLeistung;
import ch.elexis.core.ui.util.ImporterPage;
import ch.elexis.core.ui.util.SWTHelper;
import ch.rgw.tools.StringTool;

public class VaebLeistungsImporter extends ImporterPage {

	@Override
	public IStatus doImport(IProgressMonitor monitor) throws Exception {
		String filename = results[0];
		if (StringTool.isNothing(filename)) return new Status(Status.ERROR, Activator.PLUGIN_ID, "Kein Dateiname angegeben."); //$NON-NLS-1$ //$NON-NLS-2$	
		
		try {			
			List <LeistungBean> leistungen = CsvLeistungsImporter.getLeistungenFromCsvFile(filename);

			monitor.beginTask("Importiere VAEB Honorartarife", IProgressMonitor.UNKNOWN);

			for(LeistungBean leistung : leistungen) {
				new VaebLeistung(leistung);
			}
			monitor.done();
			
		} catch (FileNotFoundException e) {	
			return new Status(Status.ERROR, Activator.PLUGIN_ID, "Datei nicht gefunden: "+ e.getMessage()); //$NON-NLS-1$
		} catch (RuntimeException e) {
			if(e.getCause() != null)
				e.getCause().printStackTrace();
			return new Status(Status.ERROR, Activator.PLUGIN_ID, "Fehler beim import: "+ e.getCause()); //$NON-NLS-1$
		}
		
		return Status.OK_STATUS;

	}	
	
	@Override
	public String getTitle() {
		return "VAEB Honorartarife";
	}

	@Override
	public String getDescription() {
		return "Honorartarifimport für CSV-Dateien"; 
	}

	@Override
	public Composite createPage(Composite parent) {
		FileBasedImporter fbi = new FileBasedImporter(parent, this);
		fbi.setLayoutData(SWTHelper.getFillGridData(1, true, 1, true));
		return fbi;
	}
}

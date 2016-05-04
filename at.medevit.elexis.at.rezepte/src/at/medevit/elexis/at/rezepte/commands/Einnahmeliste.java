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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.at.XID.SVNR;
import at.medevit.elexis.at.rezepte.Activator;
import at.medevit.elexis.at.rezepte.formattedoutputconsumer.DocumentCreator;
import at.medevit.elexis.at.rezepte.model.RezeptAT;
import at.medevit.elexis.at.rezepte.model.Verschreibung;
import at.medevit.elexis.at.rezepte.ui.FixMediDisplay;
import at.medevit.elexis.at.rezepte.ui.FixMediOrderingComparator;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.data.Anschrift;
import ch.elexis.data.Patient;
import ch.elexis.data.Prescription;
import ch.rgw.tools.TimeTool;

public class Einnahmeliste extends AbstractHandler {
	
	public static String einnahmeListeTemplate = "/rsc/xslt/einnahmelisteTemplate.xsl";
	private static byte[] PDFStore;
	private RezeptAT.Verschreibungen vrs;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException{
		Patient pat = ElexisEventDispatcher.getSelectedPatient();
		if (pat == null) {
			return null;
		}
		Prescription[] pres = pat.getFixmedikation();
		Arrays.sort(pres, new FixMediOrderingComparator());
		//		Rezept rezept = new Rezept(pat);
		//		for (Prescription p : pres) {
		//			// Requires new Prescription as direct references are passed
		//			// and the list of Fixmedikationen is else erased!
		//			rezept.addPrescription(new Prescription(p));
		//		}
		
		createEinnahmeliste(pat, pres);
		return null;
	}
	
	public boolean createEinnahmeliste(Patient pat, Prescription[] pres){
		RezeptAT rezeptOutput = new RezeptAT();
		rezeptOutput.setPatientName(
			pat.getVorname() + " " + pat.getName() + ", geb. " + pat.getGeburtsdatum());
		// TODO: Where does this number originate if Wahlarzt?
		// Should a combo be presented for this?
		rezeptOutput.setPatientVersicherungsnummer(pat.getXid(SVNR.DOMAIN_AT_SVNR));
		
		Anschrift pA = pat.getAnschrift();
		rezeptOutput.setPatientAnschrift(pA.getStrasse() + ", " + pA.getPlz() + " " + pA.getOrt());
		
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
		rezeptOutput.setDatum(sd.format(new Date()));
		
		vrs = rezeptOutput.getVerschreibungen();
		for (Prescription prescription : pres) {
			if (!prescription.getEndTime().isEmpty()) {
				TimeTool endTime = new TimeTool(prescription.getEndTime());
				if (endTime.isBefore(new TimeTool())) {
					continue;
				}
			}
			
			Verschreibung v = new Verschreibung();
			v.setArtikelname(prescription.getArtikel().getLabel());
			v.setDosierung(prescription.getDosis());
			v.setEinnahmevorschrift(prescription.getBemerkung());
			// TODO: What about this?
			v.setOriginalpackung("");
			v.setPrescription(prescription);
			
			String bemerkung = (String) prescription.getMap(Prescription.FLD_EXTINFO)
				.get(FixMediDisplay.BEMERKUNGEN_EINNAHMELISTE);
			v.setBemerkung(bemerkung);
			vrs.getVerschreibung().add(v);
		}
		
		//		replaceChars();
		
		try {
			PDFStore = DocumentCreator.createPdfFile(einnahmeListeTemplate, "", rezeptOutput)
				.toByteArray();
			File temp = File.createTempFile("rezeptAT_", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$
			temp.deleteOnExit();
			FileOutputStream fos = new FileOutputStream(temp);
			fos.write(PDFStore);
			fos.close();
			Program proggie = Program.findProgram("pdf");
			if (proggie != null) {
				proggie.execute(temp.getAbsolutePath());
			} else {
				if (Program.launch(temp.getAbsolutePath()) == false) {
					Runtime.getRuntime().exec(temp.getAbsolutePath());
				}
				
			}
		} catch (IOException e) {
			Status status =
				new Status(IStatus.WARNING, Activator.PLUGIN_ID, e.getLocalizedMessage(), e);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}
		
		return true;
	}
	
}

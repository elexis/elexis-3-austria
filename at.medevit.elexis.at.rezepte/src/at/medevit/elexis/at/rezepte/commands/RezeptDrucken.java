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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import at.medevit.elexis.at.XID.SVNR;
import at.medevit.elexis.at.rezepte.PrintHelper;
import at.medevit.elexis.at.rezepte.model.RezeptAT;
import at.medevit.elexis.at.rezepte.model.Verschreibung;
import at.medevit.elexis.at.rezepte.ui.FixMediDisplay;
import at.medevit.elexis.at.rezepte.ui.FixMediOrderingComparator;
import at.medevit.elexis.at.rezepte.ui.RezeptausdruckPreferencePage;
import at.medevit.elexis.at.rezepte.ui.abgabedialog.AbgabeDialog;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.data.Anschrift;
import ch.elexis.data.Patient;
import ch.elexis.data.Prescription;
import ch.elexis.data.Rezept;

public class RezeptDrucken extends AbstractHandler {
	
	public static String rezeptTemplate = "/rsc/xslt/rezeptTemplate.xsl";
	private RezeptAT.Verschreibungen vrs;
	private Patient pat;
	
	public Object execute(ExecutionEvent event) throws ExecutionException{
		pat = ElexisEventDispatcher.getSelectedPatient();
		Prescription[] pres = pat.getFixmedikation();
		Arrays.sort(pres, new FixMediOrderingComparator());
		Rezept rezept = new Rezept(pat);
		for (Prescription p : pres) {
			// Requires new Prescription as direct references are passed
			// and the list of Fixmedikationen is else erased!
			Prescription pnew = new Prescription(p);
			
			// We need to copy the Bemerkungen in the Hashtable, as this is not
			// done
			// by the default constructor!
			String bemerkung =
				(String) p.getMap(Prescription.FLD_EXTINFO).get(
					FixMediDisplay.BEMERKUNGEN_EINNAHMELISTE);
			if (bemerkung != null) {
				Map pnewExt = pnew.getMap(Prescription.FLD_EXTINFO);
				pnewExt.put(FixMediDisplay.BEMERKUNGEN_EINNAHMELISTE, bemerkung);
				pnew.setMap(Prescription.FLD_EXTINFO, pnewExt);
			}
			
			rezept.addPrescription(pnew);
		}
		createRezept(rezept);
		
		return null;
	}
	
	private void createRezept(Rezept rp){
		RezeptAT rezeptOutput = new RezeptAT();
		rezeptOutput.setPatientName(pat.getVorname() + " " + pat.getName());
		// TODO: Where does this number originate if Wahlarzt?
		// Should a combo be presented for this?
		rezeptOutput.setPatientVersicherungsnummer(pat.getXid(SVNR.DOMAIN_AT_SVNR));
		
		Anschrift pA = pat.getAnschrift();
		rezeptOutput.setPatientAnschrift(pA.getStrasse() + ", " + pA.getPlz() + " " + pA.getOrt());
		
		vrs = rezeptOutput.getVerschreibungen();
		List<Prescription> lrp = rp.getLines();
		for (Prescription prescription : lrp) {
			Verschreibung v = new Verschreibung();
			v.setArtikelname(prescription.getArtikel().getLabel());
			v.setDosierung(prescription.getDosis());
			v.setEinnahmevorschrift(prescription.getBemerkung());
			// TODO: What about this?
			v.setOriginalpackung("");
			v.setPrescription(prescription);
			
			String bemerkung =
				(String) prescription.getMap(Prescription.FLD_EXTINFO).get(
					FixMediDisplay.BEMERKUNGEN_EINNAHMELISTE);
			v.setBemerkung(bemerkung);
			vrs.getVerschreibung().add(v);
		}
		
		AbgabeDialog abgabeDia =
			new AbgabeDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				vrs.getVerschreibung(), pat);
		int returnValue = abgabeDia.open();
		if (returnValue == AbgabeDialog.CANCEL) {
			return;
		}
		
		if(abgabeDia.isVerordnungsdatumDrucken()) {
			rezeptOutput.setDatum(rp.getDate());
		} else {
			rezeptOutput.setDatum("");
		}
		
		clearIfOPNull();
		// replaceChars();
		
		if (vrs.getVerschreibung().size() == 0) {
			MessageDialog
				.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Keine Veschreibung",
					"Es sind keine Medikamente zur Verschreibung angegeben worden, der Ausdruck wird abgebrochen." +
					" Bitte überprüfen Sie ggf. den Parameter \"Medikamente mit nicht gesetzter OP vom Druck ausnehmen\" unter Einstellungen/Rezeptausdruck.");
			return;
		}
		
		// Print the recipe
		PrintHelper.printRezeptToRezeptPrinterInPostscript(rezeptTemplate, rezeptOutput);
	}
	
	/**
	 * Do not include a medi into the recipe if the OP is not set. This is user configurable on the
	 * preference page.
	 */
	private void clearIfOPNull(){
		if (CoreHub.localCfg.get(RezeptausdruckPreferencePage.NOPRINT_IF_OP_NULL, false)) {
			List<Verschreibung> lrv = vrs.getVerschreibung();
			// Need to modify using iterator to get rid of
			// ConcurrentModificationException
			for (Iterator<Verschreibung> iterator = lrv.iterator(); iterator.hasNext();) {
				Verschreibung verschreibung = (Verschreibung) iterator.next();
				if (verschreibung.getOriginalpackung().equalsIgnoreCase(""))
					iterator.remove();
			}
		}
	}

}

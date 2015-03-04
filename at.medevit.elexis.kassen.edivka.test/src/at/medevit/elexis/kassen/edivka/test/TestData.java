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
package at.medevit.elexis.kassen.edivka.test;

import java.util.ArrayList;
import java.util.List;

import at.medevit.elexis.kassen.bva.model.BvaLeistung;
import at.medevit.elexis.kassen.bva.model.PreferenceConstants;
import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import ch.elexis.core.constants.Preferences;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.events.ElexisEvent;
import ch.elexis.core.data.events.ElexisEventDispatcher;
import ch.elexis.data.Fall;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Mandant;
import ch.elexis.data.Organisation;
import ch.elexis.data.Patient;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Person;
import ch.elexis.data.Rechnung;
import ch.elexis.data.TICode;
import ch.rgw.io.SqlSettings;
import ch.rgw.tools.Result;

public class TestData {
	
	private static TestSzenario testSzenarioInstance = null;
	
	public static TestSzenario getTestSzenarioInstance(){
		if (testSzenarioInstance == null)
			testSzenarioInstance = new TestSzenario();
		
		return testSzenarioInstance;
	}
	
	public static class TestSzenario {
		public static final String MANDANTNAME = "Mandant.edivka";
		public static final String ORGNAME = "Org.edivka";
		public static final String PATIENTNAME = "Patient.edivka";
		
		List<Mandant> mandanten = new ArrayList<Mandant>();
		List<Patient> patienten = new ArrayList<Patient>();
		List<Fall> faelle = new ArrayList<Fall>();
		List<Konsultation> konsultationen = new ArrayList<Konsultation>();
		List<KassenLeistung> leistungen = new ArrayList<KassenLeistung>();
		List<Rechnung> rechnungen = new ArrayList<Rechnung>();
		
		TestSzenario(){
			CoreHub.userCfg.set(Preferences.LEISTUNGSCODES_BILLING_STRICT, false);
			createLeistungen();
			createMandanten();
			createPatientWithFall(PATIENTNAME, PATIENTNAME);
			for (int j = 0; j < faelle.size(); j++) {
				Konsultation kons = createKonsWithVerrechnetFor(faelle.get(j), mandanten.get(0));
				konsultationen.add(kons);
			}
			rechnungen.add(createRechnungForKonsultationen());
		}
		
		public List<Mandant> getMandanten(){
			return mandanten;
		}
		
		public List<Patient> getPatienten(){
			return patienten;
		}
		
		public List<Fall> getFaelle(){
			return faelle;
		}
		
		public List<Rechnung> getRechnungen(){
			return rechnungen;
		}
		
		public List<Konsultation> getKonsultationen(){
			return konsultationen;
		}
		
		private void createMandanten(){
			Mandant mandant = new Mandant(MANDANTNAME, MANDANTNAME);
			mandant.set(new String[] {
				Person.NAME, Person.FIRSTNAME, Person.TITLE, Person.SEX, Person.FLD_E_MAIL,
				Person.FLD_PHONE1, Person.FLD_FAX, Kontakt.FLD_STREET, Kontakt.FLD_ZIP,
				Kontakt.FLD_PLACE
			}, MANDANTNAME, MANDANTNAME, "Dr. med.", Person.MALE, "james@bond.uk",
				"0061 555 55 55", "0061 555 55 56", "10, Baker Street", "9999", "Elexikon");
			mandant.setRechnungssteller(mandant);
			
			// change the active mandant
			CoreHub.actMandant = mandant;
			CoreHub.mandantCfg =
				new SqlSettings(PersistentObject.getConnection(),
					"USERCONFIG", "Param", "Value", "UserID=" + mandant.getWrappedId()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			ElexisEventDispatcher.getInstance().fire(
				new ElexisEvent(CoreHub.actMandant, Mandant.class, ElexisEvent.EVENT_MANDATOR_CHANGED));
			
			mandanten.add(mandant);
		}
		
		private void createPatientWithFall(String firstname, String lastname){
			Patient pat = new Patient(lastname, firstname, "01.01.1900", "w");
			patienten.add(pat);
			
			// create BVA Abrechnungssystem with a contact
			Fall.createAbrechnungssystem("BVA", "BVA", "Fall-Standard", "");
			Organisation bvaOrg = new Organisation(ORGNAME, "");
			CoreHub.globalCfg.set(CorePreferenceConstants.CFG_KEY + PreferenceConstants.BVA_SYSTEMNAME
				+ CorePreferenceConstants.KASSE_CONTACT, bvaOrg.getId());
			
			faelle
				.add(pat.neuerFall(Fall.getDefaultCaseLabel(), Fall.getDefaultCaseReason(), "BVA"));
		}
		
		private void createLeistungen(){
			List<? extends KassenLeistung> bvaLeistungen =
				BvaLeistung.getCurrentLeistungenByIds(null, null, "A1", null, BvaLeistung.class);
			leistungen.add(bvaLeistungen.get(0));
		}
		
		private Konsultation createKonsWithVerrechnetFor(Fall fall, Mandant mandant){
			Konsultation kons = new Konsultation(fall);
			kons.addDiagnose(TICode.load("A1"));
			for (KassenLeistung leistung : leistungen) {
				kons.addLeistung(leistung);
			}
			return kons;
		}
		
		private Rechnung createRechnungForKonsultationen(){
			Result<Rechnung> result = Rechnung.build(konsultationen);
			return result.get();
		}
	}
}

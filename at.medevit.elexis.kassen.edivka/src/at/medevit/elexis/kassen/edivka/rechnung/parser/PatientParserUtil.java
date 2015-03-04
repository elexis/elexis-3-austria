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
package at.medevit.elexis.kassen.edivka.rechnung.parser;

import at.medevit.elexis.at.XID.SVNR;
import at.medevit.elexis.kassen.edivka.rechnung.model.RollenArt;
import at.medevit.elexis.kassen.edivka.rechnung.model.TpcPerson;
import ch.elexis.data.Patient;
import ch.elexis.data.Person;
import ch.elexis.data.Rechnung;

public class PatientParserUtil {

	public static TpcPerson getTpcPerson(Patient epat) {
		TpcPerson patient = new TpcPerson();
		
		patient.setFamilienName(epat.getName());
		patient.setVorName(epat.getVorname());
		patient.setTitel(epat.get(Person.TITLE));
		patient.setGeburtsDatum(epat.getGeburtsdatum());
		patient.setEMail(epat.getMailAddress());
		patient.setAdresse(AdresseParserUtil.getTpcAdresse(epat));
		
		String sex = epat.getGeschlecht();
		if(sex.equalsIgnoreCase("m"))
			patient.setGeschlecht("1");
		else if(sex.equalsIgnoreCase("w"))
			patient.setGeschlecht("2");
		else
			patient.setGeschlecht("0");
		
		return patient;
	}

	public static at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Patient getDokumentKopfPatient(
			Rechnung erechnung) {
		at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Patient patient = 
			new at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Patient();
		
		Patient epat = erechnung.getFall().getPatient();
		
		patient.setFamilienName(epat.getName());
		patient.setVorName(epat.getVorname());
		patient.setTitel(epat.get(Person.TITLE));
		patient.setGeburtsDatum(epat.getGeburtsdatum());
		patient.setEMail(epat.getMailAddress());
		patient.setAdresse(AdresseParserUtil.getTpcAdresse(epat));
		
		String sex = epat.getGeschlecht();
		if(sex.equalsIgnoreCase("m"))
			patient.setGeschlecht("1");
		else if(sex.equalsIgnoreCase("w"))
			patient.setGeschlecht("2");
		else
			patient.setGeschlecht("0");
		
		return patient;
	}

	public static at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.Patient getPKVRechnungPatient(
			Rechnung erechnung) {
		at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.Patient patient = 
			new at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.Patient();
		
		Patient epat = erechnung.getFall().getPatient();
		
		RollenArt rolle = new RollenArt();
		rolle.setRollenCode("PAT");
		rolle.setRollenBezeichnung("Patient");
		patient.setRollenArt(rolle);
		
		patient.setSVNummer(epat.getXid(SVNR.DOMAIN_AT_SVNR));
		
		patient.setFamilienName(epat.getName());
		patient.setVorName(epat.getVorname());
		patient.setTitelBezeichnung(epat.get(Person.TITLE));
		patient.setGeburtsDatum(epat.getGeburtsdatum());
		patient.setAdresse(AdresseParserUtil.getTpcAdresse(epat));
		
		String sex = epat.getGeschlecht();
		if(sex.equalsIgnoreCase("m"))
			patient.setGeschlecht("1");
		else if(sex.equalsIgnoreCase("w"))
			patient.setGeschlecht("2");
		else
			patient.setGeschlecht("0");
		
		patient.setPolizzenNummer("0");
		patient.setZessionsIndikator("0");
		patient.setAufenthalt(AufenthaltParserUtil.getPatientAufenthalt(erechnung));

		return patient;
	}
}

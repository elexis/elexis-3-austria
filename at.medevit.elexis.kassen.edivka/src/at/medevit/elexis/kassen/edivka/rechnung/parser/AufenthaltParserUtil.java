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
import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Leistender.Aufenthalt;
import at.medevit.elexis.kassen.edivka.rechnung.model.TpcPerson;
import ch.elexis.data.Rechnung;
import ch.elexis.data.Rechnungssteller;

public class AufenthaltParserUtil {

	public static Aufenthalt getAufenthalt(Rechnung erechnung) {
		Aufenthalt aufenthalt = new Aufenthalt();
		
		Rechnungssteller rechnungssteller = erechnung.getMandant().getRechnungssteller();
		ch.elexis.data.Patient epat = erechnung.getFall().getPatient();
		
		aufenthalt.setAufenthaltsKey(epat.getPatCode());
		aufenthalt.setBetriebsStelleKey(StelleParserUtil.getBetriebsStelleKey(rechnungssteller));

		TpcPerson patient = PatientParserUtil.getTpcPerson(epat);
		aufenthalt.setPatient(patient);
		aufenthalt.setFamilienName(patient.getFamilienName());
		aufenthalt.setVorName(patient.getVorName());
		aufenthalt.setGeschlecht(patient.getGeschlecht());
		aufenthalt.setGeburtsDatum(patient.getGeburtsDatum());
		aufenthalt.setSVNummer(epat.getXid(SVNR.DOMAIN_AT_SVNR));
		
		return aufenthalt;
	}

	public static at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.Patient.Aufenthalt getPatientAufenthalt(
			Rechnung erechnung) {
		at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.Patient.Aufenthalt aufenthalt = 
			new at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.Patient.Aufenthalt();
		
		// TODO implement when necessary
		
		return aufenthalt;
	}

}

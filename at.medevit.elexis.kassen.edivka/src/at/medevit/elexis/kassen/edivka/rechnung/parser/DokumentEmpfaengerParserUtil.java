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

import at.medevit.elexis.kassen.edivka.rechnung.model.DokumentEmpfaenger;
import at.medevit.elexis.kassen.edivka.rechnung.model.RollenArt;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Organisation;
import ch.elexis.data.Person;
import ch.elexis.data.Rechnung;

public class DokumentEmpfaengerParserUtil {

	public static DokumentEmpfaenger getDokumentEmpfaenger(Rechnung erechnung) {
		DokumentEmpfaenger empfaenger = new DokumentEmpfaenger();
		
		Kontakt garant = erechnung.getFall().getGarant();
		
		if(garant.istPerson()) {
			RollenArt rolle = new RollenArt();
			rolle.setRollenCode("PHV");
			rolle.setRollenBezeichnung("Hauptversicherte Person");
			empfaenger.setRollenArt(rolle);
						
			empfaenger.setGesamterName(garant.get(Person.TITLE) + " " +
					garant.get(Person.FIRSTNAME) + " " +
					garant.get(Person.NAME));
		} else if (garant.istOrganisation()) {
			RollenArt rolle = new RollenArt();
			rolle.setRollenCode("SVA");
			rolle.setRollenBezeichnung("Sozialversicherung");
			empfaenger.setRollenArt(rolle);
			
			empfaenger.setGesamterName(garant.get(Organisation.FLD_NAME1) + " " +
					garant.get(Organisation.FLD_NAME2));
		}
		empfaenger.setBetriebsStelleKey(StelleParserUtil.getBetriebsStelleKey(garant));
		empfaenger.setAdresse(AdresseParserUtil.getTpcAdresse(garant));
		
		return empfaenger;
	}
}

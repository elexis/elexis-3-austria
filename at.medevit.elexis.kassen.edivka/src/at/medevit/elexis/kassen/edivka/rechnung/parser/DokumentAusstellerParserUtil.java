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

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentAussteller;
import at.medevit.elexis.kassen.edivka.rechnung.model.RollenArt;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Organisation;
import ch.elexis.data.Person;
import ch.elexis.data.Rechnung;
import ch.elexis.data.Rechnungssteller;

public class DokumentAusstellerParserUtil {

	public static DokumentAussteller getDokumentAussteller(Rechnung erechnung) {
		DokumentAussteller aussteller = new DokumentAussteller();
		
		Rechnungssteller rechnungssteller = erechnung.getMandant().getRechnungssteller();

		aussteller.setBetriebsStelleKey(StelleParserUtil.getBetriebsStelleKey(rechnungssteller));
		
		if(rechnungssteller.istPerson()) {
			RollenArt rolle = new RollenArt();
			rolle.setRollenCode("AHB");
			rolle.setRollenBezeichnung("Hauptbehandelnder Arzt");
			aussteller.setRollenArt(rolle);
			
			aussteller.setGesamterName(rechnungssteller.get(Person.TITLE) + " " +
					rechnungssteller.get(Person.FIRSTNAME) + " " +
					rechnungssteller.get(Person.NAME));
		} else if (rechnungssteller.istOrganisation()) {
			RollenArt rolle = new RollenArt();
			rolle.setRollenCode("AGM");
			rolle.setRollenBezeichnung("Ärztegemeinschaft");
			aussteller.setRollenArt(rolle);
			
			aussteller.setGesamterName(rechnungssteller.get(Organisation.FLD_NAME1) + " " +
					rechnungssteller.get(Organisation.FLD_NAME2));
		}
		aussteller.setAdresse(AdresseParserUtil.getTpcAdresse(rechnungssteller));
		aussteller.setKontoNummer(rechnungssteller.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_ACCOUNTNR));
		aussteller.setBankLeitzahl(rechnungssteller.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_BANKCODE));
		String bankId = rechnungssteller.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_BANKCONTACT);
		Kontakt bank = Kontakt.load(bankId);
		aussteller.setBankName(bank.get(Kontakt.FLD_NAME1));
		return aussteller;
	}
}

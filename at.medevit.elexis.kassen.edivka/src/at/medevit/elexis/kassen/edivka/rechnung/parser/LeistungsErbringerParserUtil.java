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

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.LeistungsErbringer;
import at.medevit.elexis.kassen.edivka.rechnung.model.RollenArt;
import ch.elexis.data.Organisation;
import ch.elexis.data.Person;
import ch.elexis.data.Rechnung;
import ch.elexis.data.Rechnungssteller;

public class LeistungsErbringerParserUtil {

	public static LeistungsErbringer getLeistungsErbringer(Rechnung erechnung) {
		LeistungsErbringer erbringer = new LeistungsErbringer();
		
		Rechnungssteller rechnungssteller = erechnung.getMandant().getRechnungssteller();
		
		erbringer.setBetriebsStelleKey(StelleParserUtil.getBetriebsStelleKey(rechnungssteller));
		
		if(rechnungssteller.istPerson()) {
			RollenArt rolle = new RollenArt();
			rolle.setRollenCode("AHB");
			rolle.setRollenBezeichnung("Hauptbehandelnder Arzt");
			erbringer.setRollenArt(rolle);
			
			erbringer.setGesamterName(rechnungssteller.get(Person.TITLE) + " " +
					rechnungssteller.get(Person.FIRSTNAME) + " " +
					rechnungssteller.get(Person.NAME));
		} else if (rechnungssteller.istOrganisation()) {
			RollenArt rolle = new RollenArt();
			rolle.setRollenCode("AGM");
			rolle.setRollenBezeichnung("Ärztegemeinschaft");
			erbringer.setRollenArt(rolle);
			
			erbringer.setGesamterName(rechnungssteller.get(Organisation.FLD_NAME1) + " " +
					rechnungssteller.get(Organisation.FLD_NAME2));
		}
		erbringer.setAdresse(AdresseParserUtil.getTpcAdresse(rechnungssteller));
		erbringer.setSequenzNummer("01");
		erbringer.setVereinbarungsNummer("00");
		
		return erbringer;
	}

}

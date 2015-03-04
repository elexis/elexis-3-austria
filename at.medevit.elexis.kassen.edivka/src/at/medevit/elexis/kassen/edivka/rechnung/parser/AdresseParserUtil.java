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

import at.medevit.elexis.kassen.edivka.rechnung.model.TpcAdresse;
import ch.elexis.data.Anschrift;
import ch.elexis.data.Kontakt;

public class AdresseParserUtil {
	public static TpcAdresse getTpcAdresse(Kontakt ekontakt) {
		TpcAdresse adresse = new TpcAdresse();
		
		// set "Kontaktadresse"
		adresse.setAdresseArt("02");
		// set "Strasse mit Hausnummer gemeinsam"
		adresse.setAdresseStruktur("6");
		Anschrift an = ekontakt.getAnschrift();
		adresse.setPostLeitzahl(an.getPlz());
		adresse.setOrt(an.getOrt());
		adresse.setStrasse(an.getStrasse());
		adresse.setStaat(an.getLand());
		
		return adresse;
	}
}

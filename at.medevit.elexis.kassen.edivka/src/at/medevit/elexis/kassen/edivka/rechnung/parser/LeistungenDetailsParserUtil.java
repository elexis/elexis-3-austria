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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.LeistungenDetails;
import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.LeistungenDetails.Leistung;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Rechnung;
import ch.elexis.data.Verrechnet;

public class LeistungenDetailsParserUtil {
	
	public static LeistungenDetails getLeistungenDetails(Rechnung erechnung,
		String[] dontShowCodeSystem){
		// create a map for the generated leistungen, so we can handle konsultation on the same day
		// with the same leistung else the xslt logic grouping the leistungen will do endless
		// looping :(
		HashMap<String, Leistung> dayIdMap = new HashMap<String, Leistung>();
		
		LeistungenDetails details = new LeistungenDetails();
		
		List<Konsultation> konsultationen = erechnung.getKonsultationen();
		
		for (Konsultation kons : konsultationen) {
			List<Verrechnet> leistungen = kons.getLeistungen();
			for (int i = 0; i < leistungen.size(); i++) {
				boolean skip = false;
				if (dontShowCodeSystem != null) {
					for (int j = 0; j < dontShowCodeSystem.length; j++) {
						if (leistungen.get(i).getVerrechenbar().getCodeSystemName()
							.equals(dontShowCodeSystem[j])) {
							skip = true;
							break;
						}
					}
				}
				if (!skip) {
					String key = kons.getDatum() + "::" + leistungen.get(i).getCode();
					Leistung leistung = dayIdMap.get(key);
					if (leistung == null) {
						leistung =
							LeistungParserUtil.getLeistung(leistungen.get(i), i + 1,
								kons.getDatum(), kons.getDatum());
						dayIdMap.put(key, leistung);
					} else {
						leistung.setMengeLeistung(leistung.getMengeLeistung().add(
							new BigDecimal(leistungen.get(i).getZahl())));
					}
				}
			}
		}
		
		for (Leistung leistung : dayIdMap.values()) {
			details.getLeistung().add(leistung);
		}
		
		return details;
	}
}

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
import java.math.RoundingMode;

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.LeistungenDetails.Leistung;
import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.LeistungenDetails.Leistung.IdentifikationLeistung;
import at.medevit.elexis.kassen.edivka.rechnung.model.VerrechnungsArt;
import ch.elexis.core.data.interfaces.IVerrechenbar;
import ch.elexis.data.Verrechnet;

public class LeistungParserUtil {
	
	public static Leistung getLeistung(Verrechnet eleistung, int positionsNummer, String von,
		String bis){
		Leistung leistung = new Leistung();
		IVerrechenbar verrechenbar = eleistung.getVerrechenbar();
		
		leistung.setPositionsNummer(Integer.toString(positionsNummer));
		leistung.setReferenzSequenzNummer("01");
		
		IdentifikationLeistung identifikation = new IdentifikationLeistung();
		identifikation.setLeistungsIdentifikation(verrechenbar.getCode());
		identifikation.setLeistungsIdentifikationsBezeichnung(verrechenbar.getText());
		
		leistung.setIdentifikationLeistung(identifikation);
		
		String ueberId = verrechenbar.getCodeSystemName();
		leistung.setUebergeordneteLeistungsIdentifikation(ueberId);
		
		leistung.setLeistungsDatumZeitVon(von);
		leistung.setLeistungsDatumZeitBis(bis);
		
		VerrechnungsArt art = new VerrechnungsArt();
		art.setVerrechnungsArtCode("V");
		art.setVerrechnungsArtBezeichnung("verrechnet");
		leistung.setVerrechnungsArt(art);
		
		leistung.setMengeLeistung(new BigDecimal(eleistung.getZahl()));
		leistung.setPreisLeistung(new BigDecimal(eleistung.getNettoPreis().doubleValue()).setScale(
			2, RoundingMode.HALF_UP));
		
		return leistung;
	}
}

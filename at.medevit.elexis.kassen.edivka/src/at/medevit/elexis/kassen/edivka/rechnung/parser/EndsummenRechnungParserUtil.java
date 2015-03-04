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
import java.util.ArrayList;
import java.util.List;

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.EndsummenRechnung;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Rechnung;
import ch.elexis.data.Verrechnet;

public class EndsummenRechnungParserUtil {
	
	public static EndsummenRechnung getEndsummenRechnung(Rechnung erechnung){
		EndsummenRechnung rechnung = new EndsummenRechnung();
		
		List<Konsultation> konsultationen = erechnung.getKonsultationen();
		List<Verrechnet> leistungen = new ArrayList<Verrechnet>();
		
		for (Konsultation kons : konsultationen) {
			leistungen.addAll(kons.getLeistungen());
		}
		
		double brutto = 0.0;
		for (Verrechnet leistung : leistungen) {
			double anzahl = leistung.getZahl();
			brutto += leistung.getNettoPreis().doubleValue() * anzahl;
		}
		
		rechnung.setEndsummeBrutto(new BigDecimal(brutto).setScale(2, RoundingMode.HALF_UP));
		
		return rechnung;
	}
	
}

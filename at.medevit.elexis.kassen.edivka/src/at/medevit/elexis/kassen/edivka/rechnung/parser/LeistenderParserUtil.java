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

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Leistender;
import ch.elexis.data.Rechnung;

/**
 * Leistender is the Rechnungssteller in Elexis
 * 
 * @author thomas
 *
 */
public class LeistenderParserUtil {

	public static Leistender getLeistender(Rechnung erechnung) {
		Leistender leistender = new Leistender();
		
		leistender.setStelle(StelleParserUtil.getLeistenderStelle(erechnung));

		leistender.setAufenthalt(AufenthaltParserUtil.getAufenthalt(erechnung));
		
		leistender.setDokument(DokumentParserUtil.getTpcDokument(erechnung));
		
		return leistender;
	}

}

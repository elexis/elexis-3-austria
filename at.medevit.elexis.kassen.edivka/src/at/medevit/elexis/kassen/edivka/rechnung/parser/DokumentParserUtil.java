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

import at.medevit.elexis.kassen.edivka.rechnung.model.TpcDokument;
import ch.elexis.data.Rechnung;
import ch.elexis.data.Rechnungssteller;

public class DokumentParserUtil {

	public static TpcDokument getTpcDokument(Rechnung erechnung) {
		TpcDokument dokument = new TpcDokument();
		
		Rechnungssteller rechnungssteller = erechnung.getMandant().getRechnungssteller();
		
		dokument.setDokumentenKey(erechnung.getNr());
		dokument.setAufenthaltsKey(erechnung.getRnId());
		dokument.setBetriebsStelleKey(StelleParserUtil.getBetriebsStelleKey(rechnungssteller));
		dokument.setDokumentenZeit(erechnung.getDatumRn());
		// for now just set a code for "Ambulante Rechnung"
		dokument.setQualifier("P17");
		dokument.setQualifierText("Ambulante Rechnung");
		
		return dokument;
	}

}

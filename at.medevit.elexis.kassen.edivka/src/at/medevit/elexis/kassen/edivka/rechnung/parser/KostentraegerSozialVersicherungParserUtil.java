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
import at.medevit.elexis.kassen.edivka.rechnung.model.KostentraegerSozialVersicherung;
import at.medevit.elexis.kassen.edivka.rechnung.model.RollenArt;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Organisation;
import ch.elexis.data.Rechnung;

public class KostentraegerSozialVersicherungParserUtil {
	
	public static KostentraegerSozialVersicherung getKostentraegerSozialVersicherung(
		Rechnung erechnung){
		KostentraegerSozialVersicherung versicherung = new KostentraegerSozialVersicherung();
		
		String codesystem = erechnung.getFall().getCodeSystemName();
		String isPrivatInsurance =
				CoreHub.globalCfg.get(CorePreferenceConstants.CFG_KEY + "/" + codesystem
				+ CorePreferenceConstants.KASSE_ISPRIVATE, "false");
		
		if (!isPrivatInsurance.equalsIgnoreCase("true")) {
			String contactId =
					CoreHub.globalCfg.get(CorePreferenceConstants.CFG_KEY + "/" + codesystem
					+ CorePreferenceConstants.KASSE_CONTACT, "");
			if (contactId.length() < 1)
				return versicherung;
			
			RollenArt rolle = new RollenArt();
			rolle.setRollenCode("SVA");
			rolle.setRollenBezeichnung("Sozialversicherung");
			versicherung.setRollenArt(rolle);
			
			Kontakt kontakt = Kontakt.load(contactId);
			versicherung.setBetriebsStelleKey(StelleParserUtil.getBetriebsStelleKey(kontakt));
			
			versicherung.setGesamterName(kontakt.get(Organisation.FLD_NAME1) + " "
				+ kontakt.get(Organisation.FLD_NAME2));
		}
		
		return versicherung;
	}
	
}

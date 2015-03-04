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

import java.util.List;

import at.medevit.elexis.at.model.PatientKassenData;
import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.PrivatVersicherung;
import at.medevit.elexis.kassen.edivka.rechnung.model.RollenArt;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Organisation;
import ch.elexis.data.Rechnung;

public class PrivatVersicherungParserUtil {
	
	public static PrivatVersicherung getPrivatVersicherung(Rechnung erechnung){
		PrivatVersicherung versicherung = new PrivatVersicherung();
		
		String codesystem = erechnung.getFall().getCodeSystemName();
// String isPrivatInsurance = Hub.globalCfg.get(CorePreferenceConstants.CFG_KEY + "/" + codesystem +
// CorePreferenceConstants.KASSE_ISPRIVATE, "false");
//
// if(isPrivatInsurance.equalsIgnoreCase("true")) {
		
		RollenArt rolle = new RollenArt();
		
		// value of PreferenceConstants.PRIVAT_ABRECHNUNGSSYSNAME
		if (erechnung.getFall().getAbrechnungsSystem().equalsIgnoreCase("Privat")) {
			PatientKassenData data = null;
			List<PatientKassenData> datas =
				PatientKassenData.getByPatient(erechnung.getFall().getPatient());
			if (datas.size() > 0)
				data = datas.get(0);
			if (data != null) {
				String sysOneStr = data.get(PatientKassenData.FLD_SYSTEMONE);
				String sysTwoStr = data.get(PatientKassenData.FLD_SYSTEMTWO);
				if (isSystemPrivatKasse(sysOneStr))
					codesystem = sysOneStr;
				else if (isSystemPrivatKasse(sysTwoStr))
					codesystem = sysTwoStr;
			}
			rolle.setRollenCode("PKV");
			rolle.setRollenBezeichnung("Privatversicherung");
		} else {
			rolle.setRollenCode("SVA");
			rolle.setRollenBezeichnung("Sozialversicherung");
		}
		
		String contactId =
			CoreHub.globalCfg.get(CorePreferenceConstants.CFG_KEY + "/" + codesystem
				+ CorePreferenceConstants.KASSE_CONTACT, "");
		if (contactId.length() < 1)
			return versicherung;
		
		versicherung.setRollenArt(rolle);
		
		Kontakt kontakt = Kontakt.load(contactId);
		versicherung.setBetriebsStelleKey(StelleParserUtil.getBetriebsStelleKey(kontakt));
		
		versicherung.setGesamterName(kontakt.get(Organisation.FLD_NAME1) + " "
			+ kontakt.get(Organisation.FLD_NAME2));
		versicherung.setAdresse(AdresseParserUtil.getTpcAdresse(kontakt));
		versicherung.setLeistungsFallNummer("0");
// }
		
		return versicherung;
	}
	
	private static boolean isSystemPrivatKasse(String sys){
		String isPrivat =
				CoreHub.globalCfg.get(CorePreferenceConstants.CFG_KEY + "/" + sys
				+ CorePreferenceConstants.KASSE_ISPRIVATE, "");
		
		return isPrivat.equalsIgnoreCase("true");
	}
}

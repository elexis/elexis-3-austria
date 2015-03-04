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

import java.text.ParseException;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes.SpecialityCode;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.edivka.rechnung.model.Identifikationen;
import at.medevit.elexis.kassen.edivka.rechnung.model.Identifikationen.Identifikation;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Mandant;
import ch.elexis.data.Organisation;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Person;
import ch.elexis.data.Rechnung;
import ch.elexis.data.Rechnungssteller;
import ch.rgw.io.SqlSettings;

public class StelleParserUtil {
	
	public static at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Leistender.Stelle getLeistenderStelle(
		Rechnung erechnung){
		at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Leistender.Stelle stelle =
			new at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Leistender.Stelle();
		if (erechnung == null)
			throw new IllegalArgumentException();
		
		Rechnungssteller rechnungssteller = erechnung.getMandant().getRechnungssteller();
		
		stelle.setBetriebsStelleKey(getBetriebsStelleKey(rechnungssteller));
		
		if (rechnungssteller.istOrganisation()) {
			stelle.setGesamterName(rechnungssteller.get(Organisation.FLD_NAME1) + " "
				+ rechnungssteller.get(Organisation.FLD_NAME2));
			stelle.setEMail(rechnungssteller.get(Person.FLD_E_MAIL));
			stelle.setAdresse(AdresseParserUtil.getTpcAdresse(rechnungssteller));
			stelle.setIdentifikationen(getIdentifikationen(rechnungssteller));
		} else {
			stelle.setGesamterName(rechnungssteller.get(Person.TITLE) + " "
				+ rechnungssteller.get(Person.FIRSTNAME) + " " + rechnungssteller.get(Person.NAME));
			stelle.setFamilienName(rechnungssteller.get(Person.NAME));
			stelle.setVorName(rechnungssteller.get(Person.FIRSTNAME));
			stelle.setTitel(rechnungssteller.get(Person.TITLE));
			stelle.setEMail(rechnungssteller.get(Person.FLD_E_MAIL));
			stelle.setAdresse(AdresseParserUtil.getTpcAdresse(rechnungssteller));
			stelle.setGeschlecht(rechnungssteller.get(Person.SEX));
			stelle.setIdentifikationen(getIdentifikationen(rechnungssteller));
		}
		
		return stelle;
	}
	
	public static at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Anforderer.Stelle getAnfordererStelle(
		Rechnung erechnung){
		at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Anforderer.Stelle stelle =
			new at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Anforderer.Stelle();
		
		String codesystem = erechnung.getFall().getCodeSystemName();
		String contactId =
			CoreHub.globalCfg.get(CorePreferenceConstants.CFG_KEY + "/" + codesystem
				+ CorePreferenceConstants.KASSE_CONTACT, "");
		
		if (contactId.length() < 1)
			return stelle;
		
		Kontakt kontakt = Kontakt.load(contactId);
		stelle.setBetriebsStelleKey(getBetriebsStelleKey(kontakt));
		
		if (kontakt.istPerson()) {
			stelle.setGesamterName(kontakt.get(Person.TITLE) + " " + kontakt.get(Person.FIRSTNAME)
				+ " " + kontakt.get(Person.NAME));
			stelle.setFamilienName(kontakt.get(Person.NAME));
			stelle.setVorName(kontakt.get(Person.FIRSTNAME));
			stelle.setTitel(kontakt.get(Person.TITLE));
			stelle.setEMail(kontakt.get(Person.FLD_E_MAIL));
			stelle.setAdresse(AdresseParserUtil.getTpcAdresse(kontakt));
			stelle.setGeschlecht(kontakt.get(Person.SEX));
		} else if (kontakt.istOrganisation()) {
			stelle.setGesamterName(kontakt.get(Organisation.FLD_NAME1) + " "
				+ kontakt.get(Organisation.FLD_NAME2));
			stelle.setEMail(kontakt.get(Person.FLD_E_MAIL));
			stelle.setAdresse(AdresseParserUtil.getTpcAdresse(kontakt));
		}
		
		return stelle;
	}
	
	private static SpecialityCode getMandantFachgebiet(Mandant mandant){
		SqlSettings mandantCfg =
			new SqlSettings(PersistentObject.getConnection(), "USERCONFIG", "Param", "Value",
				"UserID=" + mandant.getWrappedId());
		IPreferenceStore store = new SettingsPreferenceStore(mandantCfg);
		String mandantFachgebiet = store.getString(CorePreferenceConstants.MANDANT_FACHGEBIET);
		if (mandantFachgebiet != "") {
			int actSpecialityCode = Integer.parseInt(mandantFachgebiet);
			return SpecialityCode.getByCode(actSpecialityCode);
		}
		return null;
	}
	
	private static String getMandantHVNummer(Mandant mandant){
		SqlSettings mandantCfg =
			new SqlSettings(PersistentObject.getConnection(), "USERCONFIG", "Param", "Value",
				"UserID=" + mandant.getWrappedId());
		IPreferenceStore store = new SettingsPreferenceStore(mandantCfg);
		return store.getString(CorePreferenceConstants.MANDANT_HVNUMBER);
	}
	
	private static String getOrganisationHVNummer(Rechnungssteller kontakt){
		return kontakt.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_HVNUMBER);
	}
	
	private static SpecialityCode getOrganisationFachgebiet(Rechnungssteller kontakt){
		List<SpecialityCode> specialities = null;
		try {
			specialities =
				KassenLeistung.getSpecialitiesForString(kontakt
					.getInfoString(CorePreferenceConstants.RECHNUNGSSTELLER_SPECIALITIES));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (specialities != null && specialities.size() > 0)
			return specialities.get(0);
		return null;
	}
	
	private static Identifikationen getIdentifikationen(Kontakt kontakt){
		Identifikationen identifikationen = new Identifikationen();
		
		if (kontakt.istOrganisation()) {
			Identifikation ident = new Identifikation();
			ident.setTyp("HauptverbandVPN");
			ident.setWert(getOrganisationHVNummer((Rechnungssteller) kontakt));
			identifikationen.getIdentifikation().add(ident);
			SpecialityCode fachgebiet = getOrganisationFachgebiet((Rechnungssteller) kontakt);
			if (fachgebiet != null) {
				ident = new Identifikation();
				ident.setTyp("FachgebietCode");
				ident.setWert(Integer.toString(fachgebiet.getCode()));
				identifikationen.getIdentifikation().add(ident);
				ident = new Identifikation();
				ident.setTyp("FachgebietName");
				ident.setWert(fachgebiet.getName());
				identifikationen.getIdentifikation().add(ident);
			}
		} else {
			Mandant mandant = Mandant.load(kontakt.getId());
			Identifikation ident = new Identifikation();
			ident.setTyp("HauptverbandVPN");
			ident.setWert(getMandantHVNummer(mandant));
			identifikationen.getIdentifikation().add(ident);
			SpecialityCode fachgebiet = getMandantFachgebiet(mandant);
			if (fachgebiet != null) {
				ident = new Identifikation();
				ident.setTyp("FachgebietCode");
				ident.setWert(Integer.toString(fachgebiet.getCode()));
				identifikationen.getIdentifikation().add(ident);
				ident = new Identifikation();
				ident.setTyp("FachgebietName");
				ident.setWert(fachgebiet.getName());
				identifikationen.getIdentifikation().add(ident);
			}
		}
		
		return identifikationen;
	}
	
	public static String getBetriebsStelleKey(Kontakt kontakt){
		// build the BetriebsStelleKey
		// (health Category (X) + healthCategoryOu (XX) + OrgUid)
		// see documentation ...
		StringBuilder betriebsStelleKeyBuilder = new StringBuilder();
		if (kontakt.istOrganisation()) {
			betriebsStelleKeyBuilder.append("Organisation");
		} else {
			Mandant mandant = Mandant.load(kontakt.getId());
// SpecialityCode code = getMandantFachgebiet(mandant);
// if(code == SpecialityCode.ALLGEMEIN)
// betriebsStelleKeyBuilder.append("A01");
// else if(code == SpecialityCode.DENTIST || code == SpecialityCode.DENT)
// betriebsStelleKeyBuilder.append("A03");
// else
// betriebsStelleKeyBuilder.append("A02");
			betriebsStelleKeyBuilder.append("Mandant");
		}
		// TODO append ÄK-Nr for Bundesland !?
		return betriebsStelleKeyBuilder.toString();
	}
}

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
package at.medevit.elexis.kassen.core.model;

/**
 * Codes specific to the austrian health insurance system. The codes are used for accounting.
 * 
 * @author thomas
 *
 */
public class KassenCodes {
	/**
	 * Codes defining Fachgebiete
	 */
	public enum SpecialityCode {
		ALLGEMEIN(1, "Allgemeinmedizin"),
		ANAESTHOLOGIE_INTENSIV(2, "Anästhesiologie und Intensivmedizin"),
		AUGEN_OTPOMETRIE(3, "Augenheilkunde und Optometrie"),
		CHIRURGIE(4, "Chirurgie"),
		HAUT_GESCHLECHT(5, "Haut- und Geschlechtskrankheiten"),
		FRAUEN_GEBURT(6, "Frauenheilkunde und Geburtshilfe"),
		INNERE(7, "Innere Medizin"),
		KINDER_JUGEND(8, "Kinder- und Jugendheilkunde"),
		HNO(9, "Hals-, Nasen und Ohrenkrankheiten"),
		LUNGEN(10, "Lungenkreankheiten"),
		NEURO_PHSYCHIATRIE(11, "Neurologie und Psychiatrie"),
		ORTHOPAEDIE(12, "Orthopädie und orthopädische Chirurgie"),
		PHYSIKALISCHE(13, "Physikalische Medizin"),
		RADIOLOGIE(14, "Radiologie"),
		UNFALLCHIRURGIE(15, "Unfallchirurgie"),
		UROLOGIE(16, "Urologie"),
		ZAHN_MUND_KIEFER(17, "Zahn-, Mund- Kieferheilkunde"),
		NEUROCHIRURGIE(18, "Neurochirurgie"),
		NEUROLOGIE(19, "Neurologie"),
		PSYCHIATRIE(20, "Psychiatrie"),
		PLASTISCHECHIRURGIE(21, "Plastische Chirurgie"),
		KINDERCHIRURGIE(22, "Kinderchirurgie"),
		MUND_KIEFER_GESICHTCHIRURGIE(23, "Mund-, Kiefer- und Gesichtschirurgie"),
		NUKLEAR(24, "Nuklearmedizin"),
		RDIALOGIE_DIAG(25, "Medizinische Radiologie-Diagnostik"),
		STRAHLEN_RADIOONKOLOGIE(26, "Strahlentherapie - Radioonkologie"),
		DENT(27, "Dr.med. dent"),
		BLUTGRUPPEN_TRANSFUSION(28, "Blutgruppenserologie und Transfusionsmedizin"),
		IMMUNOLOGIE(29, "Immunologie"),
		HISTO_EMBRYOLOGIE(33, "Histologie und Embryologie"),
		BIOLOGIE(34, "Medizinische Biologie"),
		VIROLOGIE(35, "Virologie"),
		PHARMA_TOXIKOLOGIE(38, "Pharmakologie und Toxikologie"),
		LAB_DIAGNOSTIK(50, "Medizische und Chemische Labordiagnostik"),
		LAB_EEG(51, "Labor, EEG"),
		LAB_ZYTO(52, "Labor, zytodiagnostisch"),
		PATHOLOGIE(53, "Pathologie"),
		MIKROBIOLOGIE(55, "Hygiene und Mikrobiologie"),
		DENTIST(62, "Dentist"),
		KRANKENANSTALT(80, "Krankenanstalt stationär, Krankenhausambulanz"),
		CT_MR(84, "CT, MR und andere Leistungen"),
		SELBST_AMBULATORIUM(85, "Selbstständiges Ambulatorium, ausgenommen ZMK und phys. Medizin"),
		SELBST_AMBULATORIUM_ZMK(86, "Selbstständiges Ambulatorium für ZMK"),
		HEIME(90, "Genesungsheim, Kurheim, sonstige Heime"),
		SELBST_AMBULATORIUM_PHYS(91, "Selbstständiges Ambulatorium für physikalische Medizin"),
		ANDERE(99, "Andere Vertragspartner");
		
		private int code;
		private String name;

		private SpecialityCode(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
		
		public static SpecialityCode getByCode(int code) {
			SpecialityCode[] values = values();
			
			for(int i = 0; i < values.length; i++) {
				if(values[i].getCode() == code)
					return values[i];
			}
			return null;
		}
	}
	
	/**
	 * Codes defining Scheinart (code) for accounting without e-card and
	 * Behandlungsfall (newCode) for accounting with e-card.
	 */
	public enum TicketCode {
		REGELFALL(1, "RF", "Regelfall"),
		UEBERWEISUNG(2, "ÜW", "Überweisung"),
		VB_ERSTE_HILFE(3, "EH", "Vertretung/Bereitschaft Erste-Hilfe"),
		VERTRETUNG(4, "", "Vertretung"),
		SONNTAG(5, "", "Sonntagsdienst"),
		VORSORGE(6, "VU", "Vorsorgeuntersuchung"),
		ZUWEISUNG(0, "ZW", "Zuweisung"),
		VB_BEREITSCHAFT(0, "BE", "Vertretung/Bereitschaft Bereitschaft"),
		MUTTERKIND(0, "MK", "Mutter-Kind-Pass-Untersuchung"),
		VB_URLAUB(0, "AU", "Vertretung/Bereitschaft Urlaub"),
		VB_KRANKHEIT(0, "KE", "Vertretung/Bereitschaft Krankheit"),
		VB_FORTBILDUNG(0, "FE", "Vertretung/Bereitschaft Fortbildung"),
		VB_NICHTERREICHBAR(0, "NE", "Vertretung/Bereitschaft Nichterreichbarkeit"),
		BH_ORDINATIONVERLEG(0, "OV", "Behandlungsübernahme Ordinationsverlegung"),
		BH_VERTRAGSENDE(0, "SE", "Behandlungsübernahme Vertragsende"),
		BH_TOD(0, "TE", "Behandlungsübernahme Tod"),
		BH_WOHNUNGSWECHSEL(0, "WW", "Behandlungsübernahme Wohnungswechsel"),
		BH_DIENSTREISE(0, "DP", "Behandlungsübernahme Deinstreise SV-Person"),
		URLAUB(0, "UR", "Urlaub");
		
		private int code;
		private String newCode;
		private String name;

		private TicketCode(int code, String newCode, String name) {
			this.code = code;
			this.newCode = newCode;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public String getNewCode() {
			return newCode;
		}
		
		public String getName() {
			return name;
		}
	}
	
	/**
	 * Codes defining Versicherungs Kategorie and codes for 
	 * zwischenstaatliche Betreuungsfälle.
	 */
	public enum InsuranceCategory {
		ERWERBSTAETIG(1, "Erwerbstätig"),
		PENSIONIST(5, "Pensionist"),
		KRIGSHINTERBLIEBENE(7, "Kriegshinterbliebene"),
		NICHTVERS_MUKI(25, "MUKI-Nichtversicherte"),
		NICHTVERS_VU(26, "VU-Nichtversicherte"),

		LAND_SERBIEN(31, "Serbien"),
		LAND_SERBIEN_MONTE(32, "Serbien-Montenegro"),
		LAND_MONTENEGRO(33, "Montenegro"),
		LAND_TUERKEI(34, "Türkei"),
		LAND_ISRAEL(41, "Israel"),
		LAND_TUNESIEN(47, "Tunesien"),
		LAND_KROATIEN(49, "Kroatien"),
		LAND_MAZEDONIEN(56, "Mazedonien"),
		LAND_BOSNIEN_HERZ(57, "Bosnien-Herzigovina"),
		LAND_BULGARIEN(63, "Bulgarien"),
		LAND_RUMAENIEN(64, "Rumänien"),
		
		LAND_SLOWAKEI(65, "Slowakei"),
		LAND_ESTLAND(66, "Estland"),
		LAND_LETTLAND(67, "Lettland"),
		LAND_LITAUEN(68, "Litauen"),
		LAND_MALTA(69, "Malta"),
		LAND_DEUTSCHLAND(70, "Deutschland"),
		LAND_ITALIEN(71, "Italien"),
		LAND_SPANIEN(73, "Spanien"),
		LAND_FRANKREICH(75, "Frankreich"),
		LAND_SCHWEDEN(76, "Schweden"),
		LAND_LIECHTENSTEIN(77, "Liechtenstein"),
		LAND_SCHWEIZ(78, "Schweiz"),
		LAND_LUXEMBURG(79, "Luxemburg"),
		LAND_NIEDERLANDE(80, "Niederlande"),
		LAND_BELGIEN(82, "Belgien"),
		LAND_GROSSBRIT(83, "Großbritannien (inkl. Nordirland)"),
		LAND_GRIECHENLAND(84, "Griechenland"),
		LAND_PORTUGAL(85, "Portugal"),
		LAND_FINNLAND(86, "Finnland"),
		LAND_SLOWENIEN(88, "Slowenien"),
		LAND_NORWEGEN(90, "Norwegen"),
		LAND_DAENEMARK(91, "Dänemark"),
		LAND_IRLAND(92, "Irland"),
		LAND_ISLAND(93, "Island"),
		LAND_POLEN(94, "Polen"),
		LAND_UNGARN(95, "Ungarn"),
		LAND_TSCHECHIEN(98, "Tschechien"),
		LAND_ZYPERN(99, "Zypern");
		
		private int code;
		private String name;

		private InsuranceCategory(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
	}
	
	/**
	 * Codes defining Bundesland (code) for accounting without e-card
	 */
	public enum FederalState {
		VORARLBERG(9, "Vorarlberg"),
		ALL(99, "Alle");
		
		private int code;
		private String name;

		private FederalState(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
	}
}

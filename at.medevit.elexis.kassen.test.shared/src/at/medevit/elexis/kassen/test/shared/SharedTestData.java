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
package at.medevit.elexis.kassen.test.shared;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import at.medevit.elexis.kassen.core.importer.CsvLeistungsImporter;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.LeistungBean;

public class SharedTestData {
	private static String leistungen =
		"\"GruppenId\";\"PositionGruppenId\";\"PositionId\";\"PositionNeuId\";\"ValidFromDate\";\"ValidToDate\";\"PositionTitle\";\"PositionHinweis\";\"PositionAusFach\";\"PositionFachgebiete\";\"PositionPunkteWert\";\"PositionGeldWert\";\"PositionZusatz\";\"PositionLogik\"\n"
			+ "1;;;;;;\"A. Ärztlicher Honorartarif für allgemeine Leistungen und Sonderleistungen\";;;;;;;\n"
			+ "\"1.1\";;;;;;\"I. GRUNDLEISTUNGEN\";;;;;;;\n"
			+ ";\"1.1\";\"A1\";;;;\"Erste Ordination\";\"Einmal im Monat je Behandlungsfall und nicht neben Pos. Nr. B1 verrechenbar.\";\"false\";\"ALL\";20;;;\n"
			+ ";\"1.1\";\"E1\";;;;\"Erste Ordination erforderlichenfalls einschließlich eingehender Untersuchung\";\"Einmal im Monat je Behandlungsfall und nicht neben Pos. Nr. F1 verrechenbar.\";\"true\";\"ALL\";20;;;\n"
			+ "\"1.2\";;;;;;\"II. DIAGNOSE- UND THERAPIEGESPRÄCHE\";;;;;;;\n"
			+ ";\"1.2\";\"TA\";;;;\"Ausführliche diagnostisch-therapeutische Aussprache zwischen Arzt und Patient als integrierter Therapiebestandteil ab 1.10.2003\";\"Die vorstehende Leistung ist unter folgenden Bedingungen verrechenbar: a) Mit der „Ausführlichen therapeutischen Aussprache“ soll grundsätzlich eine Erweiterung und Vertiefung der Therapie erreicht werden, darunter fällt jeden- falls nicht die Anamnese. b) Zur Verrechnung sind die Vertragsärzte für Allgemeinmedizin, die Vertrags- fachärzte, mit Ausnahme der Vertragsfachärzte für Labormedizin, Radiologie und für physikalische Medizin, berechtigt. c) Der Arzt hat die „Ausführliche therapeutische Aussprache“ persönlich zu füh- ren, die Verwendung medialer Hilfsmittel (z. B. Video) oder die „Ausführliche therapeutische Aussprache“ mit mehreren Patienten gleichzeitig ist unzuläs- sig. Die Gesprächsführung mit Eltern von Kindern bzw. mit Angehörigen von geistig eingeschränkten Patienten (Apoplexiepatienten) ist zulässig. d) Die „Ausführliche therapeutische Aussprache“ hat im Allgemeinen zwi schen 10 und 15 Minuten zu dauern. e) Die „Ausführliche therapeutische Aussprache“ ist grundsätzlich in der Ordina- tion zu führen. In medizinisch begründeten Fällen ist die „Ausführliche thera- peutische Aussprache“ auch im Rahmen einer Visite zulässig. f) Die „Ausführliche therapeutische Aussprache“ ist von den Vertragsärzten für Allgemeinmedizin, Vertragsfachärzten für Innere Medizin und Vertrags- fachärzten für Kinderheilkunde in höchstens 18% der Behandlungsfälle pro Quartal, von den übrigen Vertragsärzten (ausgenommen Vertragsfach ärzte für Labormedizin, Radiologie und physikalische Medizin) in höchs tens 11% der Behandlungsfälle pro Quartal verrechenbar. g) Die „Ausführliche therapeutische Aussprache“ ist grundsätzlich nur bei eige- nen Patienten verrechenbar. Eine Zuweisung zum Zweck einer „Ausführlichen therapeutischen Aussprache“ ist unzulässig. Bei zugewiesenen Patienten kann die „Ausführliche therapeutische Aussprache“ nur dann verrechnet wer- den, wenn dies im Zuge der weiteren Behandlung medizinisch notwendig ist. Vertragsfachärzte für Neurologie und Psychiatrie können bei zugewiesenen Patienten keine „Ausführliche therapeutische Aussprache“ verrechnen. h) Die gleichzeitige Verrechnung der „Ausführlichen therapeutischen Ausspra- che“ mit den Pos. Nrn. 36d, 36e und 36f für Fachärzte für Neurologie und Psychiatrie ist bei eigenen Patienten innerhalb eines Quartals nur mit Begrün- dung möglich. Die gleichzeitige Verrechnung der „Ausführlichen therapeutischen Ausspra- che“ mit der Pos. Nr. 34h innerhalb eines Quartals bzw. mit der Pos. Nr. 36a innerhalb eines Monats ist nicht möglich, es sei denn unter An gabe einer weiteren neuen Diagnose. Die gleichzeitige Verrechnung der „Ausführlichen therapeutischen Ausspra- che“ mit einer Basisuntersuchung im Rahmen des Vorsorgeunter suchungs- Gesamtvertrages innerhalb eines Abrechnungsmonates ist ausgeschlossen.\";\"false\";\"ALL\";;11,11;;\n"
			+ "\"1.3\";;;;;;\"III. ALLGEMEINE SONDERLEISTUNGEN\";;;;;;;\n"
			+ ";\"1.3\";\"10a\";;;;\"Blutabnahme aus der Vene\";;\"false\";\"ALL\";4;;;\n"
			+ "\"1.4\";;;;;;\"IV. SONDERLEISTUNGEN aus dem Gebiete der AUGENHEILKUNDE\";;;;;;;\n"
			+ ";\"1.4\";\"22a\";;;;\"Brillenbestimmung bei Astigmatismus (Javal)\";\"A.\";\"false\";\"OPHTH\";6;;;\n"
			+ "\"1.5\";;;;;;\"V. SONDERLEISTUNGEN aus dem Gebiete der CHIRURGIE, UNFALLCHIRURGIE und ORTHOPÄDIE\";;;;;;;\n"
			+ ";\"1.5\";\"25a\";;;;\"Kleine Wunde mit Naht (Klammer)\";;\"false\";\"CHIRU,ORTHO\";10;;;\n"
			+ "\"1.6\";;;;;;\"VI. SONDERLEISTUNGEN aus dem Gebiete der FRAUENHEILKUNDE und GEBURTSHILFE\";;;;;;;\n"
			+ ";\"1.6\";\"30a\";;;;\"Tamponade der Gebärmutter zur Blutstillung\";;\"false\";\"GYNEC\";12;;;\n"
			+ "\"1.7\";;;;;;\"VII. SONDERLEISTUNGEN aus dem Gebiete der HALS-, NASEN- und OHRENKRANKHEITEN\";;;;;;;\n"
			+ ";\"1.7\";\"32a\";;;;\"Eingehende Prüfung des statischen Gleichgewichtes, thermische Prüfung oder Drehprüfung, Prüfung des Provokationsnystagmus, Lage-, Lagerungs-, Schüttelnystagmus, maximal 2 Prüfungen\";;\"false\";\"HNO\";10;;;\n"
			+ "\"1.8\";;;;;;\"VIII. SONDERLEISTUNGEN aus dem Gebiete der INNEREN MEDIZIN, KINDERHEILKUNDE und LUNGENKRANKHEITEN Pos. Nr. Euro\";;;;;;;\n"
			+ ";\"1.8\";\"34a\";;;;\"EKG in Ruhe (Ableitungen I, II, III, AVR, AVL, AVF, V1-6)\";\"I.K.\";\"false\";\"INNER\";;38,5491;;\n"
			+ "\"1.9\";;;;;;\"IX. SONDERLEISTUNGEN aus dem Gebiete der NEUROLOGIE und PSYCHIATRIE\";;;;;;;\n"
			+ ";\"1.9\";\"35a\";;;;\"Elektrische Untersuchungen der Muskelerregbarkeit\";;\"false\";\"NEURO,PSYCH\";10;;;\n"
			+ "\"1.10\";;;;;;\"X. SONDERLEISTUNGEN aus dem Gebiete der HAUT- und GESCHLECHTSKRANKHEITEN und der UROLOGIE\";;;;;;;\n"
			+ ";\"1.10\";\"38a\";;;;\"Katheterismus der männlichen Harnblase\";;\"false\";\"DERMA,VENER,UROLO\";4;;;\n"
			+ "\"1.11\";;;;;;\"XI. PHYSIKALISCHE BEHANDLUNG durch Ärzte für Allgemeinmedizin und Fachärzte\";;;;;;;\n"
			+ ";\"1.11\";\"p 1a\";;;;\"Manuelle Massage\";;\"false\";\"PHYSI\";;3,24;;\n"
			+ "2;;;;;;\"B. Operationstarif für Ärzte für Allgemeinmedizin und Fachärzte\";\" 1. Soweit bei den einzelnen Tarifpositionen des Operationsgruppenschemas Fachgebiete (lt. Abkürzungsschlüssel bezeichnet) angeführt sind, können diese Leistungen bei Durchführung durch Fachärzte nur von Fachärzten der dort angeführten Fachgebiete verrechnet werden, es sei denn in begründeten Notfällen. 2. Kosmetische Operationen und Operationen zum Zwecke der Sterilisierung werden von der BVA nur honoriert, wenn eine Kostenübernahmeverpflich tung vorliegt. 3. Alle getätigten Leistungen sind mit Angabe der Positionsnummer zu verrech- nen, ansonsten werden in Zweifelsfällen nur die jeweils niedrigeren Positionen honoriert. 4. Bei Operationen, die in der Ordination des Arztes oder in einer Kranken anstalt durchgeführt werden, wird neben dem Operationshonorar das Honorar für die allgemeine Verrichtung (Beratung oder Krankenbesuch) nur dann vergütet, wenn es sich um die erste Beratung oder den ersten Krankenbesuch handelt. 5. Bei Eingriffen, die nach dem Operationsgruppenschema bewertet werden, dürfen Zuschlagspunkte für Leistungen, die einen integrierenden Bestandteil dieser Operation bilden, nicht verrechnet werden. 6. Bei besonderer Schwierigkeit einer Operation kann mit Begründung die nächsthöhere Operationsgruppe verrechnet werden. 7. Bei den mit + bezeichneten Operationen ist die Verrechnung von Assistenz unzulässig. Bei den mit ++ bezeichneten Operationen der Gruppe IV kann eine zweite Assistenz ohne besondere Begründung in Anspruch genommen werden. 8. Wird die Operation in einer Krankenanstalt durchgeführt, so ist diese unter Angabe der Verpflegsklasse in der hiefür vorgesehenen Rubrik der Anzeige über geleistete Arzthilfe anzuführen. 9. Regiezuschläge dürfen vom Arzt nur verrechnet werden, wenn die Operation in der Ordination des Arztes oder in der Wohnung des Patienten durch geführt wurde. 10. Das Honorar für Assistenz und Narkose wird den diese Leistungen er - bringenden Vertragsärzten auf Grund der vom Operateur auf der Honorarliste durchzuführenden Verrechnung unmittelbar von der BVA überwiesen. Vom operierenden Arzt ist daher in jedem Falle der Name und die Anschrift des Assistenten bzw. Narkotiseurs anzugeben. 11. Bei besonderer Schwierigkeit einer Narkose kann der Facharzt für Anäs- thesiologie mit Begründung die nächsthöhere Narkosegruppe ver rechnen, ausgenommen bei Anwendung des Punktes 6.\";;;;;;\n"
			+ "\"2.1\";;;;;;\"OPERATIONSHONORAR\";;;;;;;\n"
			+ ";\"2.1\";\"I\";;;;\"Operation\";;\"true\";\"OPERA\";55;;;\n"
			+ ";\"2.1\";\"II\";;;;\"Operation\";;\"true\";\"OPERA\";110;;;\n";
	
	public static InputStreamReader getLeistungen(){
		try {
			return new InputStreamReader(new ByteArrayInputStream(leistungen.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Imports test Leistungen as KassenLeistungImpl and prevents duplicate import.
	 */
	public static void importTestLeistungen(){
		List<LeistungBean> leistungen =
			CsvLeistungsImporter.getLeistungenFromCsvStream(SharedTestData.getLeistungen());
		for (LeistungBean bean : leistungen) {
			List<? extends KassenLeistung> list =
				KassenLeistung.getCurrentLeistungenByIds(bean.getGruppeId(),
					bean.getPositionGruppenId(), bean.getPositionId(), bean.getPositionNeuId(),
					KassenLeistungImpl.class);
			if (list.size() == 0) {
				new KassenLeistungImpl(bean);
			}
		}
	}
}

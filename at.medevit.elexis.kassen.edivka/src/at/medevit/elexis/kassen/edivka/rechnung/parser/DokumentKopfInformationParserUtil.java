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

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen;
import ch.elexis.data.Rechnung;

public class DokumentKopfInformationParserUtil {
	public static DokumentKopfInformationen getDokumentKopfInformation(Rechnung erechnung) {
		DokumentKopfInformationen kopfInfo = new DokumentKopfInformationen();
		
		kopfInfo.setLeistender(LeistenderParserUtil.getLeistender(erechnung));
		kopfInfo.setAnforderer(AnfordererParserUtil.getAnforderer(erechnung));
		kopfInfo.setPatient(PatientParserUtil.getDokumentKopfPatient(erechnung));
		
		return kopfInfo;
	}
}

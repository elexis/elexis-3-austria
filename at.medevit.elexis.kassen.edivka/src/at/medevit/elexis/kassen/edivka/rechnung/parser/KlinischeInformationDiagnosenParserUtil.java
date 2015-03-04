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

import java.util.ArrayList;
import java.util.List;

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.KlinischeInformationDiagnosen;
import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.KlinischeInformationDiagnosen.HauptDiagnose;
import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.KlinischeInformationDiagnosen.NebenDiagnose;
import ch.elexis.core.data.interfaces.IDiagnose;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Rechnung;

public class KlinischeInformationDiagnosenParserUtil {
	
	public static KlinischeInformationDiagnosen getKlinischeInformationDiagnosen(Rechnung erechnung){
		KlinischeInformationDiagnosen diagnosen = new KlinischeInformationDiagnosen();
		
		List<IDiagnose> ediagnosen = new ArrayList<IDiagnose>();
		List<Konsultation> konsultationen = erechnung.getKonsultationen();
		for (Konsultation kons : konsultationen) {
			ediagnosen.addAll(kons.getDiagnosen());
		}
		
		ArrayList<IDiagnose> setDiag = new ArrayList<IDiagnose>();
		boolean setHauptDiag = true;
		for (IDiagnose ediag : ediagnosen) {
			if (setHauptDiag) {
				setDiag.add(ediag);
				diagnosen.setHauptDiagnose(getHauptDiagnose(ediag));
				setHauptDiag = false;
			} else {
				boolean alreadySet = false;
				for (IDiagnose diagnose : setDiag) {
					if (ediag.equals(diagnose)) {
						alreadySet = true;
						break;
					}
				}
				if (!alreadySet)
					diagnosen.getNebenDiagnose().add(getNebenDiagnose(ediag));
			}
		}
		
		return diagnosen;
	}
	
	private static HauptDiagnose getHauptDiagnose(IDiagnose ediagnose){
		HauptDiagnose diagnose = new HauptDiagnose();
		
		diagnose.setDiagnoseCode(ediagnose.getCode());
		diagnose.setDiagnoseBezeichnung(ediagnose.getText());
		
		return diagnose;
	}
	
	private static NebenDiagnose getNebenDiagnose(IDiagnose ediagnose){
		NebenDiagnose diagnose = new NebenDiagnose();
		
		diagnose.setDiagnoseCode(ediagnose.getCode());
		diagnose.setDiagnoseBezeichnung(ediagnose.getText());
		
		return diagnose;
	}
}

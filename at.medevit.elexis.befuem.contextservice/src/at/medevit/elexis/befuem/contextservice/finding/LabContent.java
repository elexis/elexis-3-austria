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
package at.medevit.elexis.befuem.contextservice.finding;

import java.util.Date;
import java.util.List;

import ch.elexis.data.Patient;

public class LabContent extends AbstractFindingContent {
	
	@Override
	public Typ getTyp() {
		return Typ.LAB;
	}

	/**
	 * 
	 * @param results the lab results
	 * @param id a String describing the content
	 */
	public LabContent(String text, LabResultInfo result, ElexisFinding parent) {
		this.text = text;
		this.result = result;
		this.parent = parent;
	}

	public LabResultInfo getResult() {
		return result;
	}
	
	/**
	 * Create all LabResults contained in this object
	 * @param patient
	 * @param date
	 */
	public void createLabResults(Patient patient, Date date) {
		List<LabResultTest> tests = result.getTests();
		for (LabResultTest test : tests) {
			if (!test.isDuplicate(patient, date) && !test.isStorno()) {
				test.createLabResult(patient, date);
			}
		}
	}
	
	@Override
	public String getDescription() {
		return "Lab results for Patient " +
		parent.getPatientInfo().getLastname() + " " +
		parent.getPatientInfo().getFirstname() + ", " + 
		((parent.getPatientInfo().getBirthdate() == null) ? "" : parent.getPatientInfo().getBirthdate());
	}
}

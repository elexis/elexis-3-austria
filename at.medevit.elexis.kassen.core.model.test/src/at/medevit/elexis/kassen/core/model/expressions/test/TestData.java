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
package at.medevit.elexis.kassen.core.model.expressions.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.test.shared.KassenLeistungImpl;
import at.medevit.elexis.kassen.test.shared.SharedTestData;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.data.Fall;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Mandant;
import ch.elexis.data.Patient;

public class TestData {
	public static final String parserTestExpression =
		"expression0(expression1(parameter) composite expression2(parameter))";
	
	public static final String parserTestExpressionTree =
		"expression0(expression1(parameter-parameter) and expression2(parameter)) and expression3(parameter,parameter) or expression4(parameter)";
	
	public static final String parserTestExpressionCompositeTree =
		"expression0(expression1(parameter-parameter) and expression2(parameter)) and(expression3(parameter,parameter) or expression4(parameter))";
	
	public static final String factoryTestExpression =
		"not(positioningroup(1.1-1.2) or positioningroup(2)) and mandantspecialityis(HNO)";
	
	private static TestSzenario testSzenarioInstance = null;
	
	public static Date getDayAfterTomorrow(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 2);
		return cal.getTime();
	}
	
	public static Date getNow(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	public static Date getNowPlusOneSecond(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);
		return cal.getTime();
	}
	
	public static Date getDayBeforeYesterday(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -2);
		return cal.getTime();
	}
	
	public static TestSzenario getTestSzenarioInstance(){
		if (testSzenarioInstance == null)
			testSzenarioInstance = new TestSzenario();
		
		return testSzenarioInstance;
	}
	
	public static class TestSzenario {
		List<Mandant> mandanten = new ArrayList<Mandant>();
		List<Patient> patienten = new ArrayList<Patient>();
		List<Fall> faelle = new ArrayList<Fall>();
		List<Konsultation> konsultationen = new ArrayList<Konsultation>();
		
		TestSzenario(){
			createMandanten();
			for (int i = 0; i < mandanten.size(); i++) {
				createPatientWithFall("Patient" + i, "Patient" + i);
				for (int j = 0; j < faelle.size(); j++) {
					Konsultation kons =
						createKonsWithVerrechnetFor(faelle.get(j), mandanten.get(i));
					konsultationen.add(kons);
				}
			}
		}
		
		public List<Mandant> getMandanten(){
			return mandanten;
		}
		
		public List<Patient> getPatienten(){
			return patienten;
		}
		
		public List<Fall> getFaelle(){
			return faelle;
		}
		
		public List<Konsultation> getKonsultationen(){
			return konsultationen;
		}
		
		private void createMandanten(){
			mandanten.add(new Mandant("Mandant0", "Mandant0", "01.01.1900", "m"));
			mandanten.add(new Mandant("Mandant1", "Mandant1", "01.01.1900", "w"));
		}
		
		private void createPatientWithFall(String firstname, String lastname){
			Patient pat = new Patient(lastname, firstname, "01.01.1900", "w");
			patienten.add(pat);
			faelle.add(pat.neuerFall(Fall.getDefaultCaseLabel(), Fall.getDefaultCaseReason(),
				Fall.getDefaultCaseLaw()));
		}
		
		private Konsultation createKonsWithVerrechnetFor(Fall fall, Mandant mandant){
			Mandant tmp = CoreHub.actMandant;
			
			CoreHub.actMandant = mandant;
			Konsultation kons = new Konsultation(fall);
			
			SharedTestData.importTestLeistungen();
			List<? extends KassenLeistung> leistungen =
				KassenLeistung.getCurrentLeistungenByIds(null, "1.1", null, null,
					KassenLeistungImpl.class);
			for (KassenLeistung leistung : leistungen) {
				kons.addLeistung(leistung);
			}
			
			CoreHub.actMandant = tmp;
			return kons;
		}
	}
}

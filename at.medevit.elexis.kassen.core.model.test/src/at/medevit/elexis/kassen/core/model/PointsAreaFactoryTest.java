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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.core.model.expressions.test.TestData;
import at.medevit.elexis.kassen.test.shared.KassenLeistungImpl;
import at.medevit.elexis.kassen.test.shared.SharedTestData;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

public class PointsAreaFactoryTest {
	@BeforeClass
	public static void setup(){
		SharedTestData.importTestLeistungen();
	}
	
	@Test
	public void testGetPointsAreaForStringPositionInGroup(){
		IPointsArea area =
			PointsAreaFactory.getInstance().getPointsAreaForString(
				"positioningroup(1.1) or positioningroup(1.2) 1,2345", KassenLeistungImpl.class);
		assertNotNull(area);
		List<? extends KassenLeistung> included =
			KassenLeistung.getCurrentLeistungenByIds(null, "1.2", null, null,
				KassenLeistungImpl.class);
		for (KassenLeistung leistung : included) {
			assertTrue(area.includesPosition(leistung, TestData.getDayAfterTomorrow()));
			assertEquals(1.2345, area.getMoneyValue(), 0.0001);
		}
		List<? extends KassenLeistung> excluded =
			KassenLeistung.getCurrentLeistungenByIds(null, "2.1", null, null,
				KassenLeistungImpl.class);
		for (KassenLeistung leistung : excluded) {
			assertFalse(area.includesPosition(leistung, TestData.getDayAfterTomorrow()));
		}
	}
	
	@Test
	public void testGetPointsAreaForStringFachgebiet(){
		SettingsPreferenceStore store = new SettingsPreferenceStore(CoreHub.mandantCfg);
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.ALLGEMEIN.getCode()));
		store.flush();
		
		IPointsArea area =
			PointsAreaFactory.getInstance().getPointsAreaForString(
				"mandantspecialityis(ALLGEMEIN) 1,2345", KassenLeistungImpl.class);
		assertNotNull(area);
		List<? extends KassenLeistung> included =
			KassenLeistung.getCurrentLeistungenByIds(null, "1.2", null, null,
				KassenLeistungImpl.class);
		for (KassenLeistung leistung : included) {
			assertTrue(area.includesPosition(leistung, TestData.getDayAfterTomorrow()));
			assertEquals(1.2345, area.getMoneyValue(), 0.0001);
		}
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.HNO.getCode()));
		store.flush();
		for (KassenLeistung leistung : included) {
			assertFalse(area.includesPosition(leistung, TestData.getDayAfterTomorrow()));
		}
	}
	
	@Test
	public void testGetPointsAreaForStringPunktePro(){
		IPointsArea area =
			PointsAreaFactory.getInstance().getPointsAreaForString(
				"pointrange(0-10000, QUARTER) 1,2345", KassenLeistungImpl.class);
		assertNotNull(area);
		List<? extends KassenLeistung> included =
			KassenLeistung.getCurrentLeistungenByIds(null, "1.2", null, null,
				KassenLeistungImpl.class);
		for (KassenLeistung leistung : included) {
			assertTrue(area.includesPosition(leistung, TestData.getDayAfterTomorrow()));
			assertEquals(1.2345, area.getMoneyValue(), 0.0001);
		}
	}
	
	@Test
	public void testGetPointsAreaForStringMix(){
		SettingsPreferenceStore store = new SettingsPreferenceStore(CoreHub.mandantCfg);
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.ALLGEMEIN.getCode()));
		store.flush();
		
		IPointsArea area =
			PointsAreaFactory
				.getInstance()
				.getPointsAreaForString(
					"positioningroup(1.1) or positioningroup(1.2) and mandantspecialityis(ALLGEMEIN) 1,2345",
					KassenLeistungImpl.class);
		assertNotNull(area);
		List<? extends KassenLeistung> included =
			KassenLeistung.getCurrentLeistungenByIds(null, "1.2", null, null,
				KassenLeistungImpl.class);
		for (KassenLeistung leistung : included) {
			assertTrue(area.includesPosition(leistung, TestData.getDayAfterTomorrow()));
			assertEquals(1.2345, area.getMoneyValue(), 0.0001);
		}
		List<? extends KassenLeistung> excluded =
			KassenLeistung.getCurrentLeistungenByIds(null, "2.1", null, null,
				KassenLeistungImpl.class);
		for (KassenLeistung leistung : excluded) {
			assertFalse(area.includesPosition(leistung, TestData.getDayAfterTomorrow()));
		}
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.HNO.getCode()));
		store.flush();
		for (KassenLeistung leistung : included) {
			assertFalse(area.includesPosition(leistung, TestData.getDayAfterTomorrow()));
		}
	}
	
	@Test
	public void testGetPointsAreaForStringMixWeight(){
		SettingsPreferenceStore store = new SettingsPreferenceStore(CoreHub.mandantCfg);
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.ALLGEMEIN.getCode()));
		store.flush();
		
		IPointsArea area1 =
			PointsAreaFactory
				.getInstance()
				.getPointsAreaForString(
					"positioningroup(1.1) or positioningroup(1.2) and(mandantspecialityis(ALLGEMEIN) or mandantspecialityis(HNO)) 1,2345",
					KassenLeistungImpl.class);
		IPointsArea area2 =
			PointsAreaFactory.getInstance().getPointsAreaForString(
				"positioningroup(1.1) or positioningroup(1.2) 0,9876", KassenLeistungImpl.class);
		assertNotNull(area1);
		assertNotNull(area2);
		List<? extends KassenLeistung> included =
			KassenLeistung.getCurrentLeistungenByIds(null, "1.2", null, null,
				KassenLeistungImpl.class);
		for (KassenLeistung leistung : included) {
			assertTrue(area1.includesPosition(leistung, TestData.getDayAfterTomorrow()));
			assertTrue(area2.includesPosition(leistung, TestData.getDayAfterTomorrow()));
		}
		assertTrue(area1.getWeight() > area2.getWeight());
	}
	
	@Test
	public void testGroupGetLabel(){
		String definition = "positioningroup(1.1) or positioningroup(1.2) 1,2345";
		IPointsArea area =
			PointsAreaFactory.getInstance().getPointsAreaForString(definition,
				KassenLeistungImpl.class);
		String label = area.getLabel();
		assertTrue(definition.equals(label));
	}
	
	@Test
	public void testSpecialityGetLabel(){
		String definition = "mandantspecialityis(INNERE) or mandantspecialityis(CHIRURGIE) 1,2345";
		IPointsArea area =
			PointsAreaFactory.getInstance().getPointsAreaForString(definition,
				KassenLeistungImpl.class);
		String label = area.getLabel();
		assertTrue(definition.equals(label));
	}
	
	@Test
	public void testGetPositionAreaForStringFail(){
		try {
			PointsAreaFactory.getInstance().getPointsAreaForString("", KassenLeistungImpl.class);
			fail("Expected Exception not thrown.");
		} catch (IllegalArgumentException ie) {
			
		}
		
		try {
			PointsAreaFactory.getInstance().getPointsAreaForString("1,2345",
				KassenLeistungImpl.class);
			fail("Expected Exception not thrown.");
		} catch (IllegalArgumentException ie) {
			
		}
		
		try {
			PointsAreaFactory.getInstance().getPointsAreaForString("Kapitel(1.1) Kapitel(1.2)",
				KassenLeistungImpl.class);
			fail("Expected Exception not thrown.");
		} catch (IllegalArgumentException ie) {
			
		}
	}
}

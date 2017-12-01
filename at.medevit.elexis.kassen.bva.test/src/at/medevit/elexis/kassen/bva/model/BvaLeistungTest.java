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
package at.medevit.elexis.kassen.bva.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.bva.test.TestData;
import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.interfaces.IFall;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

public class BvaLeistungTest {
	@BeforeClass
	public static void setup(){
		TestData.importBvaTestLeistungen();
	}
	
	@Test
	public void testGetTP(){
		List<? extends KassenLeistung> leistungen =
			KassenLeistung.getCurrentLeistungenByIds(null, null, "A1", null, BvaLeistung.class);
		assertEquals(20, leistungen.get(0).getTP(null, (IFall) null) / 100, 0.0001);
		
		leistungen =
			KassenLeistung.getCurrentLeistungenByIds(null, null, "TA", null, BvaLeistung.class);
		assertEquals(11.11, ((double) leistungen.get(0).getTP(null, (IFall)  null)) / 100, 0.0001);
	}
	
	@Test
	public void testGetFactor(){
		SettingsPreferenceStore store = new SettingsPreferenceStore(CoreHub.mandantCfg);
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.HNO.getCode()));
		store.flush();
		
		List<? extends KassenLeistung> leistungen =
			KassenLeistung.getCurrentLeistungenByIds(null, null, "A1", null, BvaLeistung.class);
		double factor = leistungen.get(0).getFactor(null, null);
		assertEquals(0.8768, factor, 0.0001);
		
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.INNERE.getCode()));
		store.flush();
		
		assertEquals(1.2854, leistungen.get(0).getFactor(null, null), 0.0001);
		
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.LUNGEN.getCode()));
		store.flush();
		
		assertEquals(1.0381, leistungen.get(0).getFactor(null, null), 0.0001);
		
		leistungen =
			KassenLeistung.getCurrentLeistungenByIds(null, null, "TA", null, BvaLeistung.class);
		assertEquals(1, leistungen.get(0).getFactor(null, null), 0.0001);
	}
}

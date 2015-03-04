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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.test.shared.KassenLeistungImpl;
import at.medevit.elexis.kassen.test.shared.SharedTestData;

public class KassenLeistungGroupTest {
	
	@BeforeClass
	public static void setup(){
		SharedTestData.importTestLeistungen();
	}
	
	@Test
	public void testIsEqual(){
		List<? extends KassenLeistung> groups =
			KassenLeistung.getAllCurrentGroupLeistungen(KassenLeistungImpl.class);
		KassenLeistungGroup lastGroup = null;
		KassenLeistungGroup equal = null;
		for (KassenLeistung group : groups) {
			if (lastGroup == null) {
				lastGroup = new KassenLeistungGroup(group);
				continue;
			}
			equal = new KassenLeistungGroup(group);
			assertFalse(equal.isEqual(lastGroup));
			lastGroup = new KassenLeistungGroup(group);
			equal = lastGroup;
		}
		assertTrue(lastGroup.isEqual(equal));
	}
	
	@Test
	public void testIsHigher(){
		List<? extends KassenLeistung> groups =
			KassenLeistung.getAllCurrentGroupLeistungen(KassenLeistungImpl.class);
		KassenLeistungGroup lastGroup = null;
		KassenLeistungGroup equal = null;
		for (KassenLeistung group : groups) {
			if (lastGroup == null) {
				lastGroup = new KassenLeistungGroup(group);
				continue;
			}
			equal = new KassenLeistungGroup(group);
			assertTrue(equal.isHigherThan(lastGroup));
			assertFalse(lastGroup.isHigherThan(equal));
			lastGroup = new KassenLeistungGroup(group);
			equal = lastGroup;
		}
		// the objects are equal
		assertFalse(lastGroup.isHigherThan(equal));
	}
	
	@Test
	public void testIsLower(){
		List<? extends KassenLeistung> groups =
			KassenLeistung.getAllCurrentGroupLeistungen(KassenLeistungImpl.class);
		KassenLeistungGroup lastGroup = null;
		KassenLeistungGroup equal = null;
		for (KassenLeistung group : groups) {
			if (lastGroup == null) {
				lastGroup = new KassenLeistungGroup(group);
				continue;
			}
			equal = new KassenLeistungGroup(group);
			assertFalse(equal.isLowerThan(lastGroup));
			assertTrue(lastGroup.isLowerThan(equal));
			lastGroup = new KassenLeistungGroup(group);
			equal = lastGroup;
		}
		// the objects are equal
		assertFalse(lastGroup.isLowerThan(equal));
	}
}

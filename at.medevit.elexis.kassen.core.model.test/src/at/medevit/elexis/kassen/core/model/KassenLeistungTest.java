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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.test.shared.KassenLeistungImpl;
import at.medevit.elexis.kassen.test.shared.SharedTestData;

public class KassenLeistungTest {
	
	@BeforeClass
	public static void setup(){
		SharedTestData.importTestLeistungen();
	}
	
	@Test
	public void testGetRootLeistungen(){
		List<? extends KassenLeistung> root =
			KassenLeistung.getCurrentRootLeistungen(KassenLeistungImpl.class);
		assertNotNull(root);
		assertTrue(root.size() > 0);
	}
	
	@Test
	public void testGetAllGroupLeistungen(){
		List<? extends KassenLeistung> root =
			KassenLeistung.getAllCurrentGroupLeistungen(KassenLeistungImpl.class);
		assertNotNull(root);
		assertTrue(root.size() > 0);
	}
	
	@Test
	public void testIsGroup(){
		List<? extends KassenLeistung> root =
			KassenLeistung.getAllCurrentGroupLeistungen(KassenLeistungImpl.class);
		for (KassenLeistung leistung : root) {
			assertTrue(leistung.isGroup());
		}
	}
	
	@Test
	public void testGetChildren(){
		List<? extends KassenLeistung> root =
			KassenLeistung.getAllCurrentGroupLeistungen(KassenLeistungImpl.class);
		for (KassenLeistung leistung : root) {
			List<? extends KassenLeistung> children = leistung.getChildren();
			assertNotNull(children);
			assertTrue(children.size() > 0);
		}
	}
	
	@Test
	public void testGetValue(){
		List<? extends KassenLeistung> root =
			KassenLeistung.getAllCurrentGroupLeistungen(KassenLeistungImpl.class);
		for (KassenLeistung leistung : root) {
			List<? extends KassenLeistung> children = leistung.getChildren();
			for (KassenLeistung child : children) {
				if (child.isGroup())
					assertEquals(0, child.getPointValue(), 0.0001);
				else {
					double points = child.getPointValue();
					double money = child.getMoneyValue();
					assertTrue(money + points > 1);
				}
			}
		}
	}
}

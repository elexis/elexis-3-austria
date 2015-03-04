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

public class GroupPositionAreaTest {
	
	@BeforeClass
	public static void setup(){
		SharedTestData.importTestLeistungen();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testIncludesPosition(){
		// area specified by 2 groups
		KassenLeistung from =
			KassenLeistung.getCurrentLeistungenByIds("1.1", null, null, null,
				KassenLeistungImpl.class).get(0);
		KassenLeistung to =
			KassenLeistung.getCurrentLeistungenByIds("1.2", null, null, null,
				KassenLeistungImpl.class).get(0);
		
		PositionRange area = new PositionRange(from, to);
		
		List<KassenLeistung> included = (List<KassenLeistung>) from.getChildren();
		List<KassenLeistung> toChildren = (List<KassenLeistung>) to.getChildren();
		included.addAll(toChildren);
		for (KassenLeistung position : included) {
			assertTrue(area.includesPosition(position));
		}
		KassenLeistung above =
			KassenLeistung.getCurrentLeistungenByIds("2.1", null, null, null,
				KassenLeistungImpl.class).get(0);
		List<? extends KassenLeistung> excluded = above.getChildren();
		for (KassenLeistung position : excluded) {
			assertFalse(area.includesPosition(position));
		}
		// area specified by 1 group
		to =
			KassenLeistung.getCurrentLeistungenByIds("1", null, null, null,
				KassenLeistungImpl.class).get(0);
		area = new PositionRange(to);
		for (KassenLeistung position : included) {
			assertTrue(area.includesPosition(position));
		}
		for (KassenLeistung position : excluded) {
			assertFalse(area.includesPosition(position));
		}
	}
}

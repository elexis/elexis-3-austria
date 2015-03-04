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
package at.medevit.elexis.kassen.core.importer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.medevit.elexis.kassen.core.model.LeistungBean;
import at.medevit.elexis.kassen.test.shared.SharedTestData;

public class CsvLeistungsImporterTest {
	
	@Test
	public void testGetLeistungenFromCsvStream() {
		List<LeistungBean> leistungen = CsvLeistungsImporter.getLeistungenFromCsvStream(SharedTestData.getLeistungen());
		assertNotNull(leistungen);
		assertTrue(leistungen.size() > 3);
		
//		System.out.println(leistungen.get(0));
//		System.out.println(leistungen.get(1));
//		System.out.println(leistungen.get(2));
//		System.out.println(leistungen.get(3));
		
		assertTrue(leistungen.get(0).getGruppeId().equals("1"));
		assertTrue(leistungen.get(0).getPositionId().equals(""));
		assertTrue(leistungen.get(1).getGruppeId().equals("1.1"));
		assertTrue(leistungen.get(1).getPositionId().equals(""));
		assertTrue(leistungen.get(2).getGruppeId().equals(""));
		assertTrue(leistungen.get(2).getPositionGruppenId().equals("1.1"));
		assertTrue(leistungen.get(2).getPositionId().equals("A1"));
	}
}

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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.edivka.test.TestData;
import at.medevit.elexis.kassen.edivka.test.TestData.TestSzenario;

public class StelleParserUtilTest {
	
	private static TestSzenario data;

	@BeforeClass
	public static void setup() {
		data = TestData.getTestSzenarioInstance();
	}
	
	@Test
	public void testGetLeistenderStelle() {
		at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Leistender.Stelle stelle = 
			StelleParserUtil.getLeistenderStelle(data.getRechnungen().get(0));
		assertNotNull(stelle);
		assertTrue(stelle.getGesamterName().contains(TestSzenario.MANDANTNAME));
	}
	
	@Test
	public void testGetAnfordererStelle() {
		at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Anforderer.Stelle stelle = 
			StelleParserUtil.getAnfordererStelle(data.getRechnungen().get(0));
		assertNotNull(stelle);
		assertTrue(stelle.getGesamterName().contains(TestSzenario.ORGNAME));
	}
}

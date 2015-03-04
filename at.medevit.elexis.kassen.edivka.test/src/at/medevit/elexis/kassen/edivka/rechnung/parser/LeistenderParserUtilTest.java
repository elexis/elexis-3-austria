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

import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.DokumentKopfInformationen.Leistender;
import at.medevit.elexis.kassen.edivka.test.TestData;
import at.medevit.elexis.kassen.edivka.test.TestData.TestSzenario;

public class LeistenderParserUtilTest {
	private static TestSzenario data;

	@BeforeClass
	public static void setup() {
		data = TestData.getTestSzenarioInstance();
	}
	
	@Test
	public void testGetLeistenderStelle() {
		Leistender leistender = LeistenderParserUtil.getLeistender(data.getRechnungen().get(0));
		assertNotNull(leistender);
		assertNotNull(leistender.getStelle());
		assertNotNull(leistender.getAufenthalt());
		assertNotNull(leistender.getDokument());
	}
}

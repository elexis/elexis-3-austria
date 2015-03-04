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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.junit.Test;

import at.medevit.elexis.kassen.core.model.LeistungBean;
import at.medevit.elexis.kassen.test.shared.SharedTestData;

public class CsvLeistungsExporterTest {

	@Test
	public void testWriteLeistungenAsCsvToStream() throws IOException {
		List<LeistungBean> leistungen = CsvLeistungsImporter.getLeistungenFromCsvStream(SharedTestData.getLeistungen());
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
		
		CsvLeistungsExporter.internalWriteLeistungenAsCsvToStream(leistungen, writer);
		
		writer.close();
		out.flush();
		System.out.println(out.toString());
	}
}

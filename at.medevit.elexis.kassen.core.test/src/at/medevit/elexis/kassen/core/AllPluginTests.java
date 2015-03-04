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
package at.medevit.elexis.kassen.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import at.medevit.elexis.kassen.core.importer.CsvLeistungsExporterTest;
import at.medevit.elexis.kassen.core.importer.CsvLeistungsImporterTest;
import at.medevit.elexis.kassen.core.importer.KassenResourceImporterTest;
import at.medevit.elexis.kassen.core.importer.XmlPointAreasImporterTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({CsvLeistungsImporterTest.class,
		XmlPointAreasImporterTest.class, CsvLeistungsExporterTest.class,
		KassenResourceImporterTest.class})
public class AllPluginTests {

}

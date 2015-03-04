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

import org.junit.Test;

import at.medevit.elexis.kassen.core.TestData;
import at.medevit.elexis.kassen.core.importer.model.PointAreas;

public class XmlPointAreasImporterTest {

	@Test
	public void testGetPointAreas() {
		PointAreas areas = XmlPointAreasImporter.getPointAreas(TestData.getPointAreas());
		assertNotNull(areas);
		assertNotNull(areas.validfrom);
		assertNotNull(areas.pointarea);
		assertTrue(areas.pointarea.size() > 0);
		assertNotNull(areas.pointarea.get(0));
	}
	
}

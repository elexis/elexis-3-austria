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

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

import at.medevit.elexis.kassen.core.Activator;
import at.medevit.elexis.kassen.core.model.IPointsArea;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.PointsAreaFactory;
import at.medevit.elexis.kassen.test.shared.KassenLeistungImpl;

public class KassenResourceImporterTest {

	static Bundle bundle;
	
	@BeforeClass
	public static void setup() {
		 bundle = Activator.getBundle();
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(bundle);
		
		KassenResourceImporter importer = new KassenResourceImporter();
		assertNotNull(importer);
		importer.setClazz(KassenLeistungImpl.class);
		importer.setPreferencePrefix(KassenLeistungImpl.PREFERENCE_PREFIX);
		importer.setCatalogs(bundle.findEntries("rsc", "*catalog*", true));
		importer.setPoints(bundle.findEntries("rsc", "*points*", true));
		
		System.out.println(importer.getFilesAsString());
	}
	
	@Test
	public void testUpdate() {
		assertNotNull(bundle);

		List<KassenLeistung> leistungen = KassenLeistung.getAllCurrentLeistungen(KassenLeistungImpl.class);
		System.out.println("LEISTUNGEN: " + leistungen.size());
		
		KassenResourceImporter importer = new KassenResourceImporter();
		assertNotNull(importer);
		importer.setClazz(KassenLeistungImpl.class);
		importer.setPreferencePrefix(KassenLeistungImpl.PREFERENCE_PREFIX);
		importer.setCatalogs(bundle.findEntries("rsc", "*catalog*", true));
		importer.setPoints(bundle.findEntries("rsc", "*points*", true));

		importer.update();
		
		leistungen = KassenLeistung.getAllCurrentLeistungen(KassenLeistungImpl.class);
		System.out.println("LEISTUNGEN: " + leistungen.size());
		
		List<IPointsArea> areas = PointsAreaFactory.getInstance().getPointsAreasForClass(KassenLeistungImpl.class);
		System.out.println("POINTAREAS: " + areas.size());		
	}
}

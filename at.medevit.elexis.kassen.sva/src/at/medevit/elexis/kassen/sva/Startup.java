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
package at.medevit.elexis.kassen.sva;

import org.eclipse.ui.IStartup;
import org.osgi.framework.Bundle;

import at.medevit.elexis.kassen.core.importer.KassenResourceImporter;
import at.medevit.elexis.kassen.sva.model.SvaLeistung;
import at.medevit.elexis.kassen.sva.ui.SvaPreferenceInitializer;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		Bundle bundle = Activator.getDefault().getBundle();
		KassenResourceImporter importer = new KassenResourceImporter();
		importer.setClazz(SvaLeistung.class);
		importer.setPreferencePrefix(SvaPreferenceInitializer.SVAPREF);
		importer.setCatalogs(bundle.findEntries("rsc", "*catalog*", true));
		importer.setPoints(bundle.findEntries("rsc", "*points*", true));

		importer.update();
	}
}

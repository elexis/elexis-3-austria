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
package at.medevit.elexis.kassen.svb;

import org.eclipse.ui.IStartup;

import at.medevit.elexis.kassen.core.importer.KassenResourceImporter;
import at.medevit.elexis.kassen.svb.model.SvbLeistung;
import at.medevit.elexis.kassen.svb.ui.SvbPreferenceInitializer;

public class Startup implements IStartup {
	
	@Override
	public void earlyStartup(){
		KassenResourceImporter importer = new KassenResourceImporter();
		importer.setClazz(SvbLeistung.class);
		importer.setPreferencePrefix(SvbPreferenceInitializer.SVBPREF);
		importer.updateForeign();
		
		SvbPreferenceInitializer initializer = new SvbPreferenceInitializer();
		initializer.initializeDefaultPreferences();
	}
}

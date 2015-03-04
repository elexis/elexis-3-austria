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
package at.medevit.elexis.kassen.svb.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.svb.model.PreferenceConstants;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

public class SvbPreferenceInitializer extends AbstractPreferenceInitializer {
	
	public static String SVBPREF = CorePreferenceConstants.CFG_KEY
		+ PreferenceConstants.SVB_SYSTEMNAME;
	
	public SvbPreferenceInitializer(){
		super();
	}
	
	@Override
	public void initializeDefaultPreferences(){
		IPreferenceStore store = new SettingsPreferenceStore(CoreHub.globalCfg);
		
		store.setDefault(SVBPREF + CorePreferenceConstants.KASSE_HVCODE, "00"); //$NON-NLS-1$
		store.setDefault(SVBPREF + CorePreferenceConstants.KASSE_BILLINGINTERVAL, "4");
		store.setDefault(SVBPREF + CorePreferenceConstants.KASSE_CONTACT, "");
		store.setDefault(SVBPREF + CorePreferenceConstants.KASSE_STDINSURANCECATEGORIE,
			KassenCodes.InsuranceCategory.ERWERBSTAETIG.getCode());
		store.setDefault(SVBPREF + CorePreferenceConstants.KASSE_USEFOREIGNPOINTVALUES, "false");
	}
}

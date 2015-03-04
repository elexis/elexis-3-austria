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
package at.medevit.elexis.kassen.vgkk.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.vgkk.model.PreferenceConstants;
import at.medevit.elexis.kassen.vgkk.model.VgkkLeistung;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

public class VgkkPreferenceInitializer extends AbstractPreferenceInitializer {
	
	public static String VGKKPREF = CorePreferenceConstants.CFG_KEY
		+ PreferenceConstants.VGKK_SYSTEMNAME;
	
	public VgkkPreferenceInitializer(){
		super();
	}
	
	@Override
	public void initializeDefaultPreferences(){
		IPreferenceStore store = new SettingsPreferenceStore(CoreHub.globalCfg);
		
		store.setDefault(VGKKPREF + CorePreferenceConstants.KASSE_HVCODE, "19"); //$NON-NLS-1$
		store.setDefault(VGKKPREF + CorePreferenceConstants.KASSE_BILLINGINTERVAL, "4");
		store.setDefault(VGKKPREF + CorePreferenceConstants.KASSE_CONTACT, "");
		store.setDefault(VGKKPREF + CorePreferenceConstants.KASSE_STDINSURANCECATEGORIE,
			KassenCodes.InsuranceCategory.ERWERBSTAETIG.getCode());
		store.setDefault(VGKKPREF + CorePreferenceConstants.KASSE_ISREGION, "true");
		store.setDefault(VGKKPREF + CorePreferenceConstants.KASSE_CLASSNAME,
			VgkkLeistung.class.getName());
	}
}

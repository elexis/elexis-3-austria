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
package at.medevit.elexis.kassen.bva.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import at.medevit.elexis.kassen.bva.model.PreferenceConstants;
import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

public class BvaPreferenceInitializer extends AbstractPreferenceInitializer {
	
	public static String BVAPREF = CorePreferenceConstants.CFG_KEY
		+ PreferenceConstants.BVA_SYSTEMNAME;
	
	public BvaPreferenceInitializer(){
		super();
	}
	
	@Override
	public void initializeDefaultPreferences(){
		IPreferenceStore store = new SettingsPreferenceStore(CoreHub.globalCfg);
		
		store.setDefault(BVAPREF + CorePreferenceConstants.KASSE_HVCODE, "7"); //$NON-NLS-1$
		store.setDefault(BVAPREF + CorePreferenceConstants.KASSE_BILLINGINTERVAL, "1");
		store.setDefault(BVAPREF + CorePreferenceConstants.KASSE_CONTACT, "");
		store.setDefault(BVAPREF + CorePreferenceConstants.KASSE_STDINSURANCECATEGORIE,
			KassenCodes.InsuranceCategory.ERWERBSTAETIG.getCode());
	}
}

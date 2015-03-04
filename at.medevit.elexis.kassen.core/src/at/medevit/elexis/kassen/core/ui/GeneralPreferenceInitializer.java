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
package at.medevit.elexis.kassen.core.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

public class GeneralPreferenceInitializer extends AbstractPreferenceInitializer {

	public GeneralPreferenceInitializer() {
		super();
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = new SettingsPreferenceStore(CoreHub.mandantCfg);
		
		store.setDefault(CorePreferenceConstants.MANDANT_HVNUMBER, ""); //$NON-NLS-1$
		store.setDefault(CorePreferenceConstants.MANDANT_DVRNUMBER, "");
		store.setDefault(CorePreferenceConstants.MANDANT_FACHGEBIET, KassenCodes.SpecialityCode.ALLGEMEIN.getCode());
		store.setDefault(CorePreferenceConstants.MANDANT_BUNDESLAND, KassenCodes.FederalState.ALL.getCode());
	}

}

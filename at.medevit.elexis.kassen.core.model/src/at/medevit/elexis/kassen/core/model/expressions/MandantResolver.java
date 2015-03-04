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
package at.medevit.elexis.kassen.core.model.expressions;

import org.eclipse.core.expressions.IVariableResolver;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Mandant;
import ch.elexis.data.PersistentObject;
import ch.rgw.io.SqlSettings;

/**
 * IVariableResolver for all mandant specific variables.
 * 
 * <li>
 * MandantSpeciality
 * </li>
 * 
 * @author thomas
 *
 */
public class MandantResolver implements IVariableResolver {

	public static final String variableName = "Mandant"; 
	public enum ResolveVariableId{MANDANTOBJ, SPECIALITYCODEOBJECT};
	
	private Mandant mandant;
	
	public MandantResolver(Mandant mandant) {
		this.mandant = mandant;
	}
	
	@Override
	public Object resolve(String name, Object[] args) throws CoreException {
		
		if(name.equals(variableName)) {
			if(args[0] instanceof ResolveVariableId) {
				ResolveVariableId id = (ResolveVariableId)args[0];
				switch (id) {
				case SPECIALITYCODEOBJECT:
					return getSpecialityFromSettings();
				case MANDANTOBJ:
					return mandant;
				}
			}
		}
		
		return null;
	}
	
	private KassenCodes.SpecialityCode getSpecialityFromSettings() {
		SqlSettings mandantCfg = new SqlSettings(PersistentObject.getConnection(),
				"USERCONFIG", "Param", "Value", "UserID=" + mandant.getWrappedId());
		IPreferenceStore store = new SettingsPreferenceStore(mandantCfg);
		String mandantFachgebiet = store.getString(CorePreferenceConstants.MANDANT_FACHGEBIET);
		if(mandantFachgebiet != "") {
			int actSpecialityCode = Integer.parseInt(mandantFachgebiet);
			return KassenCodes.SpecialityCode.getByCode(actSpecialityCode);
		}
		return null;
	}
}

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
package at.medevit.elexis.kassen.privat.model;

import java.util.ArrayList;
import java.util.List;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.privat.Activator;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Kontakt;

public class AdditionalInsurance {
	private static SettingsPreferenceStore globalStore = new SettingsPreferenceStore(CoreHub.globalCfg,
		Activator.PLUGIN_ID);
	
	private String preferenceString;
	
	public AdditionalInsurance(String preferenceString){
		this.preferenceString = preferenceString;
	}
	
	public String getName(){
		return globalStore.getString(preferenceString + PreferenceConstants.PRIVAT_NAME);
	}
	
	public void setName(String name){
		globalStore.setValue(preferenceString + PreferenceConstants.PRIVAT_NAME, name);
	}
	
	public Kontakt getContact(){
		String contactId =
			globalStore.getString(preferenceString + PreferenceConstants.PRIVAT_CONTACT);
		if (contactId.length() > 0) {
			return Kontakt.load(contactId);
		}
		return null;
	}
	
	public void setContact(Kontakt kontakt){
		globalStore
			.setValue(preferenceString + PreferenceConstants.PRIVAT_CONTACT, kontakt.getId());
	}
	
	public double getValue(){
		String valueStr =
			globalStore.getString(preferenceString + PreferenceConstants.PRIVAT_VALUE);
		if (valueStr.length() > 0)
			return Double.parseDouble(valueStr);
		return 0.0;
	}
	
	public String getValueAsString(){
		return globalStore.getString(preferenceString + PreferenceConstants.PRIVAT_VALUE);
	}
	
	public void setValue(String value){
		globalStore.setValue(preferenceString + PreferenceConstants.PRIVAT_VALUE, value);
	}
	
	public PreferenceConstants.ValueType getValueType(){
		String valueTypeStr =
			globalStore.getString(preferenceString + PreferenceConstants.PRIVAT_VALUETYPE);
		if (valueTypeStr.length() > 0)
			return PreferenceConstants.ValueType.valueOf(valueTypeStr);
		return null;
	}
	
	public void setValueType(PreferenceConstants.ValueType type){
		globalStore.setValue(preferenceString + PreferenceConstants.PRIVAT_VALUETYPE,
			type.toString());
	}
	
	public static List<AdditionalInsurance> getAll(){
		ArrayList<AdditionalInsurance> ret = new ArrayList<AdditionalInsurance>();
		String[] systems = CoreHub.globalCfg.nodes(CorePreferenceConstants.CFG_ADDITIONAL_KEY);
		if (systems != null) {
			for (String system : systems) {
				ret.add(new AdditionalInsurance(CorePreferenceConstants.CFG_ADDITIONAL_KEY + "/"
					+ system));
			}
		}
		return ret;
	}
	
	public static AdditionalInsurance getByName(String name){
		String[] systems = CoreHub.globalCfg.nodes(CorePreferenceConstants.CFG_ADDITIONAL_KEY);
		if (systems != null) {
			for (String system : systems) {
				if (name.equalsIgnoreCase(system)) {
					return new AdditionalInsurance(CorePreferenceConstants.CFG_ADDITIONAL_KEY + "/"
						+ system);
				}
			}
		}
		return null;
	}
}

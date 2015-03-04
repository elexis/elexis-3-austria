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
import ch.elexis.core.constants.Preferences;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Kontakt;

public class PrivatKasse {
	private static SettingsPreferenceStore globalStore = new SettingsPreferenceStore(CoreHub.globalCfg,
		Activator.PLUGIN_ID);
	
	private String preferenceString;
	
	public PrivatKasse(String preferenceString){
		String privatStr = globalStore.getString(Preferences.LEISTUNGSCODES_CFG_KEY + "/Privat/name");
		if (privatStr.length() == 0) {
			globalStore.setValue(Preferences.LEISTUNGSCODES_CFG_KEY + "/Privat/name",
				PreferenceConstants.PRIVAT_ABRECHNUNGSSYSNAME);
		}
		this.preferenceString = preferenceString;
		globalStore.setValue(preferenceString + CorePreferenceConstants.KASSE_ISPRIVATE, "true");
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
	
	public List<Catalog> getCatalogs(){
		ArrayList<Catalog> ret = new ArrayList<Catalog>();
		String catalogsStr =
			globalStore.getString(preferenceString + PreferenceConstants.PRIVAT_CATALOGS);
		if (catalogsStr.length() > 0) {
			List<Catalog> catalogs = Catalog.getAll();
			String[] parts = catalogsStr.split(",");
			for (Catalog catalog : catalogs) {
				for (int i = 0; i < parts.length; i++) {
					if (catalog.getSystemName().equalsIgnoreCase(parts[i])) {
						ret.add(catalog);
						break;
					}
				}
			}
		}
		return ret;
	}
	
	public String getCatalogsAsString(){
		return globalStore.getString(preferenceString + PreferenceConstants.PRIVAT_CATALOGS);
	}
	
	public void setCatalog(Object[] catalogs){
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < catalogs.length; i++) {
			Catalog catalog = (Catalog) catalogs[i];
			if (i == 0)
				sb.append(catalog.getSystemName());
			else
				sb.append("," + catalog.getSystemName());
		}
		globalStore.setValue(preferenceString + PreferenceConstants.PRIVAT_CATALOGS, sb.toString());
	}
	
	public boolean includesCatalog(String codeSystemName){
		boolean ret = false;
		String catalogsStr =
			globalStore.getString(preferenceString + PreferenceConstants.PRIVAT_CATALOGS);
		if (catalogsStr.length() > 0) {
			String[] parts = catalogsStr.split(",");
			for (int i = 0; i < parts.length; i++) {
				if (codeSystemName.equalsIgnoreCase(parts[i])) {
					ret = true;
					break;
				}
			}
		}
		return ret;
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
	
	public static List<PrivatKasse> getAll(){
		ArrayList<PrivatKasse> ret = new ArrayList<PrivatKasse>();
		String[] systems = CoreHub.globalCfg.nodes(CorePreferenceConstants.CFG_KEY);
		if (systems != null) {
			for (String system : systems) {
				String isPrivat =
					globalStore.getString(CorePreferenceConstants.CFG_KEY + "/" + system
						+ CorePreferenceConstants.KASSE_ISPRIVATE);
				if (isPrivat.equalsIgnoreCase("true")) {
					ret.add(new PrivatKasse(CorePreferenceConstants.CFG_KEY + "/" + system));
				}
			}
		}
		return ret;
	}
	
	public static PrivatKasse getByName(String name){
		String[] systems = CoreHub.globalCfg.nodes(CorePreferenceConstants.CFG_KEY);
		if (systems != null) {
			for (String system : systems) {
				String isPrivat =
					globalStore.getString(CorePreferenceConstants.CFG_KEY + "/" + system
						+ CorePreferenceConstants.KASSE_ISPRIVATE);
				if (isPrivat.equalsIgnoreCase("true") && name.equalsIgnoreCase(system)) {
					return new PrivatKasse(CorePreferenceConstants.CFG_KEY + "/" + system);
				}
			}
		}
		return null;
	}
}

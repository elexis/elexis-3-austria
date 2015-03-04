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

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;

public class PreferenceConstants {
	public static final String PRIVAT_ABRECHNUNGSSYSNAME = "Privat";
	
	public static final String PRIVAT_NAME = "/PrivatName";
	public static final String PRIVAT_CONTACT = CorePreferenceConstants.KASSE_CONTACT;
	public static final String PRIVAT_CATALOGS = "/PrivatCatalogs";
	public static final String PRIVAT_VALUE = "/PrivatValue";
	public static final String PRIVAT_VALUETYPE = "/PrivatValueType";
	
	public enum ValueType {
		ABSOLUT, PERCENT;
		
		public String getPostfix(){
			if (equals(ABSOLUT))
				return "€";
			if (equals(PERCENT))
				return "%";
			return "?";
		}
	}
}

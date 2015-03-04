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
package at.medevit.elexis.kassen.core.model;


public class KassenLeistungGroup {
	int major = -1;
	int minor = -1;
	KassenLeistung leistung;
	
	public KassenLeistungGroup(KassenLeistung group) {
		leistung = group;
		major = getGroupMajorIndex();
		minor = getGroupMinorIndex();
	}
	
	public KassenLeistungGroup() {
	}
	
	public void setGroup(KassenLeistung group) {
		leistung = group;
		major = getGroupMajorIndex();
		minor = getGroupMinorIndex();
	}
	
	@Override
	public String toString() {
		if(leistung.isGroup())
			return leistung.getGroup();
		else
			return leistung.getPositionGroup();
	}

	public boolean isSubgroupOf(KassenLeistungGroup comp) {
		if(comp.major == major && comp.minor == -1 && minor > -1)
			return true;

		return false;
	}
	
	public boolean isHigherThan(KassenLeistungGroup comp) {
		if(comp.major < major)
			return true;
		else if (comp.major == major && (comp.minor < minor || (comp.minor == -1 && minor > -1)))
			return true;
		
		return false;
	}
	
	public boolean isLowerThan(KassenLeistungGroup comp) {
		if(comp.major > major)
			return true;
		else if(comp.major == major && comp.minor > minor)
			return true;
		
		return false;
	}
	
	public boolean isEqual(KassenLeistungGroup comp) {
		if(comp.major == major && comp.minor == minor)
			return true;
		
		return false;
	}
	
	public boolean isGroup() {
		return leistung.isGroup();
	}
	
	private int getGroupMajorIndex() {
		String group = "";
		if(isGroup()) 
			group = leistung.getGroup();
		else
			group = leistung.getPositionGroup();
		
		int sepIdx = group.indexOf('.');
		if(sepIdx == -1)
			return Integer.parseInt(group);
		else
			return Integer.parseInt(group.substring(0, sepIdx));
	}
	
	private int getGroupMinorIndex() {
		String group = "";
		if(isGroup()) 
			group = leistung.getGroup();
		else
			group = leistung.getPositionGroup();
		
		int sepIdx = group.indexOf('.');
		if(sepIdx == -1)
			return -1;
		else
			return Integer.parseInt(group.substring(sepIdx + 1, group.length()));
	}
}

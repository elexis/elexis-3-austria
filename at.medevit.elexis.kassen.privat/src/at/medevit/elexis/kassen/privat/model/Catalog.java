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

import org.eclipse.core.runtime.IConfigurationElement;

import ch.elexis.core.data.util.Extensions;

public class Catalog {
	private String systemName;
	
	Catalog(String systemName){
		this.systemName = systemName;
	}
	
	public String getSystemName(){
		return systemName;
	}
	
	@Override
	public String toString(){
		return systemName;
	}
	
	public static List<Catalog> getAll(){
		ArrayList<Catalog> ret = new ArrayList<Catalog>();
		
		List<IConfigurationElement> verrechnungscodes =
			Extensions.getExtensions("ch.elexis.Verrechnungscode");
		
		for (IConfigurationElement code : verrechnungscodes) {
			String codeName = code.getAttribute("name");
			if (codeName != null && codeName.length() > 0)
				ret.add(new Catalog(codeName));
		}
		return ret;
	}
}

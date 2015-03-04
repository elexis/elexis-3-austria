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
package at.medevit.elexis.at.rezepte.properties;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import ch.elexis.data.Prescription;

public class PrescriptionAdapterFactory implements IAdapterFactory {
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType){
		if (adapterType== IPropertySource.class && adaptableObject instanceof Prescription){
			return new PrescriptionPropertyAdapter((Prescription) adaptableObject);
		}

		return null;
	}
	
	@Override
	public Class[] getAdapterList(){
		return new Class[] { IPropertySource.class };
	}
	
}

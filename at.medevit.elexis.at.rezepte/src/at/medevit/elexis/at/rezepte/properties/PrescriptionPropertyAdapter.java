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

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import ch.elexis.data.Prescription;

public class PrescriptionPropertyAdapter implements IPropertySource {
	
	private final Prescription pres;
	
	public PrescriptionPropertyAdapter(Prescription adaptableObject){
		this.pres = adaptableObject;
	}

	@Override
	public Object getEditableValue(){
		return this;
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors(){
		return new IPropertyDescriptor[] {
			new TextPropertyDescriptor("artikelname", "Artikelname"),
			new TextPropertyDescriptor("dosierung", "Dosierung") };
	}
	
	@Override
	public Object getPropertyValue(Object id){
		if (id.equals("artikelname")) {
			return pres.getArtikel().getName();
		}
		if (id.equals("dosierung")) {
			return pres.getDosis();
		}
		return null;

	}
	
	@Override
	public boolean isPropertySet(Object id){
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void resetPropertyValue(Object id){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setPropertyValue(Object id, Object value){
		// TODO Auto-generated method stub
		
	}
	
}

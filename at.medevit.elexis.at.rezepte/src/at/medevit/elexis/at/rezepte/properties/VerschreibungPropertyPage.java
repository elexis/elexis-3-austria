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


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import ch.elexis.data.Prescription;

public class VerschreibungPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {
	
	public VerschreibungPropertyPage(){
		super();
	}
	
	@Override
	protected Control createContents(Composite parent){
		IAdaptable adapt = getElement();
		Prescription pres = (Prescription) adapt.getAdapter(ch.elexis.data.Prescription.class);
		
		
		Composite comp = new Composite(parent, SWT.None);
		comp.setLayout(new GridLayout(1, false));
		Label lblDiscoDiscoParty = new Label(comp, SWT.None);
		lblDiscoDiscoParty.setText(pres.getLabel());
		
		return comp;
	}
}

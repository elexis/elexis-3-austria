/*******************************************************************************
 * Copyright (c) 2007, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    
 *******************************************************************************/
package ch.elexis.artikel_at.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ZeichenErklaerung extends ViewPart {
	public static final String ID = "elexis-artikel-oesterreich.zeichenerklaerung";
	Browser browser;
	
	public ZeichenErklaerung(){
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void createPartControl(Composite parent){
		browser = new Browser(parent, SWT.NONE);
		browser.setUrl("http://root.ami-info.at/company/ami-info/html/ZEICHEN.HTM");
		
	}
	
	@Override
	public void setFocus(){
		// TODO Auto-generated method stub
		
	}
	
}

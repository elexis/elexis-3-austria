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
package at.medevit.elexis.kassen.edivka;

import org.w3c.dom.Document;

import ch.elexis.data.Rechnung;

public interface IEdivkaService {
	public Document getPKVRechnungXMLDocumentForElexisRechnung(Rechnung erechnung);
	
	public Document getPKVRechnungXMLDocumentForElexisRechnung(Rechnung erechnung,
		String[] dontShowCodeSystems);
}

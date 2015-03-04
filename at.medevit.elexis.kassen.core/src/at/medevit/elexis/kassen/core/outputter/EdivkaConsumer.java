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
package at.medevit.elexis.kassen.core.outputter;

import org.w3c.dom.Document;

import at.medevit.elexis.kassen.edivka.IEdivkaService;
import ch.elexis.data.Rechnung;

public class EdivkaConsumer {
	private static IEdivkaService edivkaService;
	
	public Document getRechnungForElexisRechnung(Rechnung rechnung) {
		return edivkaService.getPKVRechnungXMLDocumentForElexisRechnung(rechnung);
	}
	
	// Method will be used by DS to set the quote service
	public synchronized void setEdivkaService(IEdivkaService service) {
		edivkaService = service;
	}

	// Method will be used by DS to unset the quote service
	public synchronized void unsetEdivkaService(IEdivkaService service) {
		if (edivkaService == service) {
			edivkaService = null;
		}
	}
}

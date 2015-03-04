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
package at.medevit.elexis.befuem.netservice;

import at.medevit.elexis.befuem.netservice.gnv.GNVClient;

public class NetClientServiceFactory implements INetClientServiceFactory {

	public static final String[] impls = { "GNV" };
	
	@Override
	public String[] getNetClientImplementations() {
		return impls;
	}

	@Override
	public INetClient getNetClientImplementation(String name) {
		if(name.equalsIgnoreCase(impls[0])) {
			return GNVClient.getInstance();
		}
		return null;
	}

}

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
package at.medevit.elexis.befuem.ui.netserviceconsumer;

import at.medevit.elexis.befuem.netservice.INetClient;
import at.medevit.elexis.befuem.netservice.INetClientServiceFactory;
import at.medevit.elexis.befuem.ui.model.PreferenceConstants;
import ch.elexis.core.data.activator.CoreHub;
import ch.rgw.tools.Log;

public class NetServiceConsumer {
	private Log log = Log.get(NetServiceConsumer.class.getName());
	private static INetClientServiceFactory clientFactory;
	
	public static String[] getClientImplementations() {
		return clientFactory.getNetClientImplementations();
	}

	public static INetClient getClientImplementation(String name) {
		INetClient impl = clientFactory.getNetClientImplementation(name);

		return impl;
	}

	public static INetClient getConfiguredClientImplementation() {
		return getClientImplementation(CoreHub.globalCfg.get(PreferenceConstants.AT_MEDEVIT_ELEXIS_BEFUEM_SELECTED_NET, "GNV")); //$NON-NLS-1$
	}
	
	// Method will be used by DS to set the service
	public synchronized void setNetClientServiceFactory(INetClientServiceFactory factory) {
		log.log("DS set service: " + factory, Log.DEBUGMSG); //$NON-NLS-1$
		clientFactory = factory;
	}

	// Method will be used by DS to unset the service
	public synchronized void unsetNetClientServiceFactory(INetClientServiceFactory factory) {
		if (clientFactory == factory) {
			log.log("DS unset service: " + factory, Log.DEBUGMSG); //$NON-NLS-1$
			clientFactory = null;
		}
	}
}
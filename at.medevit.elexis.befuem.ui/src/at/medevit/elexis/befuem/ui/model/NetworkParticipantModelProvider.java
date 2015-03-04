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
package at.medevit.elexis.befuem.ui.model;

import java.util.List;

import at.medevit.elexis.befuem.contextservice.networkparticipant.NetworkParticipant;

public class NetworkParticipantModelProvider {
	
	private static NetworkParticipantModelProvider content;
	private List<NetworkParticipant> networkParticipants;
	
	private NetworkParticipantModelProvider() {
	}
	
	public static synchronized NetworkParticipantModelProvider getInstance() {
		if (content != null ) {
			return content;
		}
			content = new NetworkParticipantModelProvider();
			return content;
	}
	
	public List<NetworkParticipant> getNetworkParticipants() {
		updateModel();
		return networkParticipants;
	}
	
	public void updateModel() {
		if(networkParticipants == null) {
			networkParticipants = NetworkParticipant.getAll();
		} else {
			List<NetworkParticipant> current = NetworkParticipant.getAll();
			networkParticipants.clear();
			networkParticipants.addAll(current);
		}
	}
}

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
package at.medevit.elexis.befuem.ui.contextserviceconsumer;

import java.io.IOException;
import java.util.List;

import at.medevit.elexis.befuem.contextservice.IContextService;
import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import ch.rgw.tools.Log;

public class ContextServiceConsumer {
	private Log log = Log.get(ContextServiceConsumer.class.getName());
	private static IContextService contextService;
	
	public static void fillClientFindingHeader(List<NetClientFinding> findings) {
		for(NetClientFinding finding : findings) {
			try {
				contextService.fillClientFindingHeader(finding);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void fillClientFindingDOM(NetClientFinding finding) {
		contextService.fillClientFindingDOM(finding);
	}
	
	public static void fillClientFindingElexisFindings(NetClientFinding finding) {
		contextService.fillClientFindingElexisFindings(finding);
		// apply date information to the ElexisFindings
		List<ElexisFinding> efindings = finding.getElexisFindings();
		for(ElexisFinding efinding : efindings) {
			efinding.setDate(finding.getCreation());
		}
	}
	
	// Method will be used by DS to set the service
	public synchronized void setContextService(IContextService context) {
		log.log("DS set service: " + context, Log.DEBUGMSG); //$NON-NLS-1$
		contextService = context;
	}

	// Method will be used by DS to unset the service
	public synchronized void unsetContextService(IContextService context) {
		if (contextService == context) {
			log.log("DS unset service: " + context, Log.DEBUGMSG); //$NON-NLS-1$
			contextService = null;
		}
	}
	
}

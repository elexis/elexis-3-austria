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
package at.medevit.elexis.befuem.contextservice;

import java.io.IOException;

import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;

public interface IContextService {
	// The plug-in ID
	public static final String PLUGIN_ID = "at.medevit.elexis.befuem.contextservice"; //$NON-NLS-1$
	
	void fillClientFindingHeader(NetClientFinding finding) throws IOException;
	
	void fillClientFindingDOM(NetClientFinding finding);
	
	void fillClientFindingElexisFindings(NetClientFinding finding);
}

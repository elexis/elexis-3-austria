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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.medevit.elexis.befuem.contextservice.dom.DomParser;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.contextservice.header.HeaderParser;
import at.medevit.elexis.befuem.contextservice.smooks.SmooksParser;

public class DefaultContextService implements IContextService {

	private static Logger log = LoggerFactory.getLogger(DefaultContextService.class);
	
	private HeaderParser headerParser = new HeaderParser();
	private DomParser domParser = new DomParser();
	
	@Override
	public void fillClientFindingHeader(NetClientFinding finding) throws IOException {
		log.debug("Fill in header information for " + finding.getFindingName());
		headerParser.fillClientFindingHeader(finding);
	}

	@Override
	public void fillClientFindingDOM(NetClientFinding finding) {
		log.debug("Fill in DOM for " + finding.getFindingName());
		finding.setDOM(SmooksParser.getDOMForFinding(finding));
		if(finding.getDOM() == null)
			log.warn("DefaultContextService: no DOM created for " + finding.getFindingName());
	}

	@Override
	public void fillClientFindingElexisFindings(NetClientFinding finding) {
		log.debug("Fill in ElexisFindings for " + finding.getFindingName());
		finding.setElexisFindings(domParser.getElexisFindings(finding));
		if(finding.getElexisFindings().size() == 0)
			log.warn("DefaultContextService: no ElexisFindings found for " + finding.getFindingName());
	}
}

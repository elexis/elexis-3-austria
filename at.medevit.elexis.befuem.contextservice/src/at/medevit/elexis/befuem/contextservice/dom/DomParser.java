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
package at.medevit.elexis.befuem.contextservice.dom;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding;
import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Format;
import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Typ;
import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;

public class DomParser {
	private static Logger log = LoggerFactory.getLogger(DomParser.class);
	
	AbstractDomParser[] parsers = { new EdiTextDomParser(), new EdiLabDomParser(), new MailboxHL7DomParser() };
	
	/**
	 * <p>
	 * 
	 * </p>
	 * @param finding
	 * @throws IOException
	 */
	public List<ElexisFinding> getElexisFindings(AbstractFinding finding) {
		
		if(finding.getFindingFormat() == Format.EDI && finding.getFindingTyp() == Typ.FINDING) {
			return parsers[0].getElexisFindings(finding.getDOM());
		} else if (finding.getFindingFormat() == Format.EDI && finding.getFindingTyp() == Typ.FINDING_REQ) {
			return parsers[0].getElexisFindings(finding.getDOM());
		} else if (finding.getFindingFormat() == Format.EDI && finding.getFindingTyp() == Typ.LAB) {
			return parsers[1].getElexisFindings(finding.getDOM());
		} else if (finding.getFindingFormat() == Format.MAILBOXHL7 && finding.getFindingTyp() == Typ.LAB) {
			return parsers[2].getElexisFindings(finding.getDOM());
		} else {
			log.error("DomParser: unknown finding format (" + finding.getFindingFormat() + ") typ (" + finding.getFindingTyp() + ")");
		}
		return null;
	}
}

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
package at.medevit.elexis.befuem.contextservice.header;

import java.io.IOException;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Format;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;

public class HeaderParser {
	IHeaderParser[] parsers = { new EdiHeaderParser(), new MailboxHL7HeaderParser() };
	
	/**
	 * <p>
	 * Parse the header of the source of the {@link ClientFinding} and
	 * fill its fields with that information.
	 * </p>
	 * <p>
	 * The following fields will be set:
	 * <li>String sender</li>
	 * <li>NetworkParticipant senderParticipant</li>
	 * <li>String receiver</li>
	 * <li>NetworkParticipant receiverParticipant</li>
	 * <li>byte[] messageDigest</li>
	 * </p>
	 * @param finding
	 * @throws IOException
	 */
	public void fillClientFindingHeader(NetClientFinding finding) throws IOException {
		Format format = getFindingFormat(finding);
		finding.setFindingFormat(format);
		
		if(format == Format.EDI) {
			parsers[0].fillClientFindingHeader(finding);
		} else if(format == Format.EDI_TRANSPORT) {
			parsers[0].fillClientFindingHeader(finding);
		} else if(format == Format.MAILBOXHL7) {
			parsers[1].fillClientFindingHeader(finding);
		} else {
			// TODO throw some meaningful exception here, or how to handle as unknown ?
		}
	}
	
	protected Format getFindingFormat(NetClientFinding finding) throws IOException {
		Format ret = Format.UNKNOWN;
		
		for(IHeaderParser parser : parsers) {
			Format guess = parser.isFileFormat(finding);
			if(guess != Format.UNKNOWN)
				ret = guess;
		}
		return ret;	
	}
}

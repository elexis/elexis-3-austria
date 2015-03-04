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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Format;
import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Typ;
import at.medevit.elexis.befuem.contextservice.finding.NetClientFinding;
import at.medevit.elexis.befuem.contextservice.networkparticipant.NetworkParticipant;

class MailboxHL7HeaderParser extends AbstractHeaderParser {

	private static final String EDI_DEFAULT_FIELD_DELIMITER = "\\+";
	
	@Override
	public Format isFileFormat(NetClientFinding finding) throws IOException {
		return getFileFormat(finding.getHeaderAsByteArray());
	}

	private Format getFileFormat(byte[] header) {
		if(header == null)
			return Format.UNKNOWN;
		
		Format ret = Format.UNKNOWN;
		String headerStr = new String(header);
		
		String []lines = headerStr.split("\n");
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].startsWith("H;") && lines[i+1].startsWith("E;"))
				ret = Format.MAILBOXHL7;
		}
		return ret;
	}
	
	@Override
	public void fillClientFindingHeader(NetClientFinding finding) throws IOException {
		setHeaderFields(finding);
		finding.setFindingTyp(Typ.LAB);
	}

	private void setHeaderFields(NetClientFinding finding) {
		String headerStr = new String(finding.getHeaderAsByteArray());
		String fieldDelimiter = EDI_DEFAULT_FIELD_DELIMITER;
		
		if(headerStr.startsWith("UNA")) {
			String newDelimiters = headerStr.substring(3, 8);
			fieldDelimiter = new String("\\" + newDelimiters.charAt(1));
		}
		// fill in sender and receiver
		int unbStart = headerStr.indexOf("UNB");
		String unbString = headerStr.substring(unbStart);
		String[] unbFields = unbString.split(fieldDelimiter);
		if(unbFields.length >= 3) {
			// set the network sender / receiver
			finding.setSender(correctMeNumber(unbFields[2]));
			NetworkParticipant parti = NetworkParticipant.getWithHVNummer(correctMeNumber(unbFields[2]));
			if(parti != null)
				finding.setSenderParticipant(parti);
			
			finding.setReceiver(correctMeNumber(unbFields[3]));
			parti = NetworkParticipant.getWithHVNummer(correctMeNumber(unbFields[3]));
			if(parti != null)
				finding.setReceiverParticipant(parti);
		}
		if(unbFields.length >= 5) {
			SimpleDateFormat format = null;
			if(unbFields[4].length() == 6)
				format = new SimpleDateFormat("yyMMddHHmm");
			else if(unbFields[4].length() == 8) {
				format = new SimpleDateFormat("yyyyMMddHHmm");
			}
			if(format != null) {
				try {
					Date date = format.parse(unbFields[4] + unbFields[5]);
					finding.setCreation(date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	String correctMeNumber(String menumber) {
		if(menumber.startsWith("ME"))
			return menumber;
		else
			return "ME" + menumber;
	}
}

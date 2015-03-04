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
package at.medevit.elexis.befuem.contextservice.finding;

import java.io.File;
import java.util.Date;

import at.medevit.elexis.befuem.contextservice.networkparticipant.NetworkParticipant;
import at.medevit.elexis.befuem.netservice.INetClient;
import at.medevit.elexis.befuem.netservice.INetClient.Location;

public class NetClientFinding extends AbstractFinding {
	public final static String[] inboxViewerProperties = new String[] {
		"findingTyp",
		"senderName",
		"visited",
		"size",
		"reception"};
	
	public final static String[] outboxViewerProperties = new String[] {
		"receiverName",
		"findingTyp",
		"findingName",
		"size",
		"reception"};
	
	public final static String[] archiveViewerProperties = new String[] {
		"findingTyp",
		"senderName",
		"receiverName",
		"visited",
		"size",
		"reception"};

	private String sender; // Sender (MExxxxxx)
	private NetworkParticipant senderParticipant;
	private String receiver; // Empfänger (MExxxxxx)
	private NetworkParticipant receiverParticipant;
	
	private String findingName;

	private Location location;
	/**
	 * Date of message reception
	 */
	private Date reception;
	/**
	 * Date of message creation
	 */
	private Date creation;
	
	/**
	 * Representation of a finding, for transport via a {@link INetClient}
	 * implementation.
	 */
	public NetClientFinding(FindingSource findingSource, Location loc, Date reception) {
		source = findingSource;
		location = loc;
		this.reception = reception;
	}
	
	/**
	 * Representation of a finding, for transport via a {@link INetClient}
	 * implementation.
	 */
	public NetClientFinding(FindingSource findingSource, Location loc) {
		source = findingSource;
		location = loc;
	}

	public String getSender() {
		if(sender != null)
			return sender;
		else
			return "";
	}

	public String getSenderName() {
		if(senderParticipant != null) {
			return senderParticipant.getNachname() + " " +
				senderParticipant.getVorname() +
				" [" + sender + "]";
		} else {
			return getSender();
		}
	}	
	
	public String getReceiver() {
		if(receiver != null)
			return receiver;
		else
			return "";
	}

	public String getReceiverName() {
		if(receiverParticipant != null) {
			return receiverParticipant.getNachname() + " " +
				receiverParticipant.getVorname() +
				" [" + receiver + "]";
		}
		else
			return getReceiver();
	}

	public String getFindingName() {
		if(findingName == null && source.getId() != null) {
			String path = source.getId();
			int pathEnd = path.lastIndexOf(File.separator);
			if(pathEnd > 0) {
				findingName = path.substring(pathEnd + 1);
			}
		}
		if(findingName != null)
			return findingName;
		else
			return "";
	}

	public Location getLocation() {
		return location;
	}

	public Date getReception() {
		return reception;
	}
	
	public Date getCreation() {
		return creation;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}

	public NetworkParticipant getSenderParticipant() {
		return senderParticipant;
	}
	
	public void setSenderParticipant(NetworkParticipant sender) {
		senderParticipant = sender;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setReceiverParticipant(NetworkParticipant receiver) {
		receiverParticipant = receiver;
	}

	public void setLocation(Location location) {
		propertyChangeSupport.firePropertyChange("location", this.location,
				this.location = location);
	}

	public void setReception(Date reception) {
		this.reception = reception;
	}
	
	public void setCreation(Date creation) {
		this.creation = creation;
	}
	
	@Override
	public String toString() {
		return "NetClientFinding: digest[" + getMessageDigestAsString() +
		"] sender[" + sender +
		"] receiver[" + receiver +
		"] reception[" + reception +
		"] size[" + getSize() + "]";
	}
}

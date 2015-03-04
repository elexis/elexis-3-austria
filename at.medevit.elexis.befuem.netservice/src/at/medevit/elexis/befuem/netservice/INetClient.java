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

import java.io.File;
import java.io.IOException;
import java.util.List;


public interface INetClient {
	public static final String AT_MEDEVIT_ELEXIS_BEFUEM_GNVCLIENT_PROGLOCATION="MEDEVIT/befuem/gnvclient/progLocation";
	
	public enum State { IDLE, RUNNING, SUCCESS, ERROR }
	
	public enum Location { INBOX, OUTBOX, ARCHIVE, ARCHIVE_INBOX, ARCHIVE_OUTBOX }
	
	/**
	 * Name of the network zB.: Gesundheitsnetz Vorarlberg
	 * 
	 * @return network name
	 */
	public String getNetName();
	
	/**
	 * Short name of the network zB.: GNV
	 * 
	 * @return network short name
	 */
	public String getNetShortName();
	
	/**
	 * Synchronize (send and receive) the findings over the network.
	 * 
	 * @return SUCCESS
	 * @throws IOException 
	 */
	public boolean synchronizeFindings() throws IOException;
	
	/**
	 * Get last successful synchronization time
	 * 
	 * @return time
	 */
	public String getLastConnectionTime();
	
	/**
	 * Get all findings for a specified location, of this client
	 * 
	 * @return array of findings in location
	 * @throws IOException 
	 */
	public List<NetClientFile> getFindings(Location loc) throws IOException;
	
	/**
	 * Archive a finding of this client
	 * 
	 * @return moved File on success or null if failed
	 * @throws IOException 
	 */
	public File archiveFinding(File finding) throws IOException;
	
	/**
	 * Get a description of the configuration for this INetClient
	 * 
	 * @return current state
	 */
    public NetClientPreferenceDescritpion getPreferenceDescription();

    /**
     * Set preference value of the client
     * 
     */
    public void setPreferenceValue(String key, String value);
    
	/**
	 * Get the current State of this INetClient
	 * 
	 * @return current state
	 */
    public State getState();

	/**
	 * Get the current status message of this INetClient
	 * 
	 * @return current status message
	 */
    public String getStatusMessage();
}

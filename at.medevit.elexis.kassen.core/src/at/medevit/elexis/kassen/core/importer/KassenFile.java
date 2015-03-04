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
package at.medevit.elexis.kassen.core.importer;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


class KassenFile implements Comparable<KassenFile> {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yy");
	
	private String versionString;
	private int versionNumber;
	private Date versionDate;
	private URL url;

	protected static Date getVersionDate(String versionString) {
		String version;
		
		int numberbegin = versionString.lastIndexOf('_');
		
		if(numberbegin == -1)
			throw new IllegalArgumentException("Invalid versionString. " + versionString);
		
		version = versionString.substring(0, numberbegin);
		try {
			return dateFormat.parse(version);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid versionString. " + versionString);
		}
	}
	
	protected static int getVersionNumber(String versionString) {
		String version;
		
		int numberbegin = versionString.lastIndexOf('_');
		
		if(numberbegin == -1)
			throw new IllegalArgumentException("Invalid filename. " + versionString);
		
		version = versionString.substring(numberbegin + 1);
		return Integer.parseInt(version);
	}
	
	protected KassenFile(URL url) {
		this.url = url;
		String[] parts = url.toString().split("/");
		String version = parts[parts.length - 1];
		int versionbegin = version.indexOf('_');
		int versionend = version.indexOf('.');
		
		if(versionbegin == -1)
			throw new IllegalArgumentException("Invalid filename. " + version);
		if(versionend == -1)
			throw new IllegalArgumentException("Invalid filename. " + version);
		
		versionString = version.substring(versionbegin + 1, versionend);
		versionDate = getVersionDate(versionString);
		versionNumber = getVersionNumber(versionString);
	}

	protected Date getVersionDate() {
		return versionDate;
	}

	protected int getVersionNumber() {
		return versionNumber;
	}

	protected URL getURL() {
		return url;
	}
	
	protected String getVersionString() {
		return versionString;
	}
	
	@Override
	public String toString() {
		return "KassenFile: " + versionString + " Date: " + versionDate + " Number: " + versionNumber;
	}

	@Override
	public int compareTo(KassenFile other) {
		if(other.versionDate.before(versionDate))
			return 1;
		if(other.versionDate.after(versionDate))
			return -1;
		if(other.versionDate.equals(versionDate)) {
			if(other.versionNumber < versionNumber)
				return 1;
			if(other.versionNumber > versionNumber)
				return -1;
		}
		return 0;
	}
}
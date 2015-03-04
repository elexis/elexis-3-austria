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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.dom.DOMResult;

import ch.elexis.data.Patient;

public abstract class AbstractFinding {
	public enum Typ {
		UNDEFINED, FINDING, FINDING_REQ, LAB
	}
	
	public enum Format {
		UNKNOWN, EDI, EDI_TRANSPORT, HL7, MAILBOXHL7 
	}
	
	protected DOMResult documentModel;
	protected List<ElexisFinding> elexisFindings;
	
	protected Typ findingTyp = Typ.UNDEFINED;
	protected Format findingFormat = Format.UNKNOWN;

	protected byte[] digest;
	protected String digestStr;
	
	protected FindingSource source;
	
	protected boolean resolved = false;
	protected boolean visited = false;
	
	private static Object lock = new Object();
	
	/**
	 * Create a PersistentFinding object if non existent.
	 * All methods using the persistence should access it
	 * via this method, for lazy initialization
	 * 
	 * @return a new or existing PersistentFinding object
	 */
	public PersistentFinding getPersistence() {
		PersistentFinding result = null;
		String digest = getMessageDigestAsString();
		// prevent simultaneous creation of persistent findings
		synchronized (lock) {
			result = PersistentFinding.getWithDigest(digest);
			if (result == null) {
				result = new PersistentFinding(source.getId(), digest);
			}
		}
		return result;
	}
	
	public String getEncoding() {
		return source.getEncoding();
	}
	
	public byte[] getHeaderAsByteArray() {
		try {
			return source.getHeaderAsByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	public InputStream getContentAsStream() {
		try {
			return source.getContentAsStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ByteArrayInputStream(new byte[0]);
	}
	
	public byte[] getContentAsByteArray() {
		try {
			return source.getContentAsByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	public byte[] getMessageDigest() {
		if(digest == null)
			digest = source.getMD5MessageDigest();
		return digest;
	}

	public String getMessageDigestAsString() {
		if(digest == null)
			digest = source.getMD5MessageDigest();
		if(digestStr == null)
			digestStr = convertToHex(digest);
		return digestStr;
	}	
	
	public int getSize() {
		return source.getSize();
	}
	
	public String getSourceId() {
		return source.getId();
	}
	
	public DOMResult getDOM() {
		return documentModel;
	}

	public void setDOM(DOMResult dom) {
		this.documentModel = dom;
	}
	
	public List<ElexisFinding> getElexisFindings() {
		if(elexisFindings == null)
			return new ArrayList<ElexisFinding>();
		return elexisFindings;
	}

	public void setElexisFindings(List<ElexisFinding> elexisFindings) {
		this.elexisFindings = elexisFindings;
	}

	/**
	 * Try to resolve all ElexisFindings and return true only if all
	 * are resolved.
	 * 
	 * @return resolving success
	 * @throws LabResultNonUniqueException 
	 */
	public boolean resolve() throws LabResultNonUniqueException {
		for(ElexisFinding f : elexisFindings) {
			if(!f.resolveContacts())
				return false;
			if(!f.resolveLabResults())
				return false;
		}
		setResolved(true);
		return true;
	}

	private void setResolved(boolean resolved) {
		propertyChangeSupport.firePropertyChange("resolved", this.resolved,
				this.resolved = resolved);
	}

	public boolean isResolved() {
		return resolved;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean visited) {
		propertyChangeSupport.firePropertyChange("visited", this.visited,
				this.visited = visited);
	}
	
	public String getUnresolvedAsString() {
		StringBuilder str = new StringBuilder();
		
		for(ElexisFinding f : elexisFindings) {
			str.append(f.getUnresolvedAsString());
		}
		
		return str.toString();
	}
	
	public Format getFindingFormat() {
		return findingFormat;
	}

	public void setFindingFormat(Format findingFormat) {
		propertyChangeSupport.firePropertyChange("findingFormat", this.findingFormat,
				this.findingFormat = findingFormat);
	}
	
	public void setFindingTyp(Typ findingTyp) {
		propertyChangeSupport.firePropertyChange("findingTyp", this.findingTyp,
				this.findingTyp = findingTyp);
	}
	
	public Typ getFindingTyp() {
		return findingTyp;
	}
	
	public void setArchived(boolean value) {
		getPersistence().setArchived(value);
	}
	
	public void setImported(boolean value) {
		getPersistence().setImported(value);
	}
	
	public void setPatient(Patient pat) {
		getPersistence().setPatient(pat);
	}
	
	public boolean isArchived() {
		return getPersistence().isArchived();
	}
	
	public boolean isImported() {
		return getPersistence().isImported();
	}

	// bean
	protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}
	
    private static String convertToHex(byte[] data) { 
        if(data == null)
        	return "null";
    	StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    }
}

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFindingContent.Typ;
import at.medevit.elexis.befuem.contextservice.formattedoutputconsumer.FormattedOutputConsumer;
import at.medevit.elexis.befuem.contextservice.networkparticipant.NetworkParticipant;
import at.medevit.elexis.formattedoutput.IFormattedOutput;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory.ObjectType;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory.OutputType;
import ch.elexis.core.ui.exchange.KontaktMatcher;
import ch.elexis.data.Kontakt;
import ch.elexis.data.LabItem;
import ch.elexis.data.Patient;

@XmlRootElement(name = "finding")
@XmlAccessorType(XmlAccessType.FIELD)
public class ElexisFinding {

	@XmlElement(name = "date")
	String date;
	@XmlElement(name = "source")
	ContactInfo sourceInfo;
	@XmlElement(name = "patient")
	ContactInfo patientInfo;

	@XmlTransient
	Kontakt source;
	@XmlTransient
	Patient patient;
	@XmlTransient
	String keywords;

	@XmlElementWrapper(name = "content")
	@XmlElement(name = "contentpart")
	List<AbstractFindingContent> content;

	@XmlTransient
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	@XmlTransient
	public static String PROPERTY_DESCRIPTION = "description";
	
	@XmlTransient
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);
	
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	public ElexisFinding() {

	}

	public ElexisFinding(ContactInfo patientInfo, ContactInfo sourceInfo) {
		this.patientInfo = patientInfo;
		this.sourceInfo = sourceInfo;
	}

	/**
	 * Get Kontakt objects for the patientInfo and sourceInfo fields.
	 * 
	 * If a single match could be found the field patient or source will refer
	 * to it, else the field will be null.
	 * 
	 * @return true if both fields are set after completion
	 */
	boolean resolveContacts() {
		if (sourceInfo == null || patientInfo == null)
			return false;

		patient = KontaktMatcher.findPatient(patientInfo.getLastname(),
				patientInfo.getFirstname(), patientInfo.getBirthdate(),
				patientInfo.getGender(), patientInfo.getStreet1(),
				patientInfo.getZip(), patientInfo.getCity(),
				patientInfo.getTel(), KontaktMatcher.CreateMode.FAIL);
		source = KontaktMatcher.findOrganisation(sourceInfo.getFirstname(),
				sourceInfo.getLastname(), sourceInfo.getStreet1(),
				sourceInfo.getZip(), sourceInfo.getCity(),
				KontaktMatcher.CreateMode.FAIL);
		// if the source is no organization try looking for a person
		if (source == null) {
			source = KontaktMatcher.findPerson(sourceInfo.getLastname(),
					sourceInfo.getFirstname(), sourceInfo.getBirthdate(),
					sourceInfo.getGender(), sourceInfo.getStreet1(),
					sourceInfo.getZip(), sourceInfo.getCity(),
					sourceInfo.getTel(), KontaktMatcher.CreateMode.FAIL);
		}
		// update contact info with found data
		if(patient != null)
			setPatient(patient);
		if(source != null)
			setSource(source);

		return (patient != null) && (source != null);
	}

	/**
	 * Look for the LabItems referred by the tests of the LabResult objects. The
	 * Items are connected to the source Kontakt in Elexis.
	 * 
	 * @return true if LabItems are found for ALL tests
	 * @throws LabResultNonUniqueException 
	 */
	boolean resolveLabResults() throws LabResultNonUniqueException {
		if (source == null)
			return false;

		for (AbstractFindingContent contentObj : content) {
			if (contentObj.getTyp() == Typ.TEXT)
				continue;
			LabResultInfo labResult = contentObj.result;
			if (source != null && patient != null) {
				for (LabResultTest test : labResult.tests) {
					LabItem item = test.getLabItem(source,
							patient.getGeschlecht());
					if (item == null) {
						return false;
					} else {
						test.setLabItem(item);
					}
				}
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get a Text description why resolve failed
	 * 
	 * @return text
	 */
	public String getUnresolvedAsString() {
		StringBuilder str = new StringBuilder();
		if (patient == null) {
			str.append("No Patient contact set.\n");
		}
		if (source == null) {
			str.append("No Source contact set.\n");
		}
		if (source != null) {
			List<LabResultTest> unresolved = getUnresolvedLabResults();
			
			for (LabResultTest test : unresolved) {
				str.append("LabItem " + test.getItemShortDescription()
						+ " not found.\n");
			}
		}
		return str.toString();
	}

	/**
	 * Get a list of all {@link LabResultTest} objects of the finding which
	 * could not be resolved to a {@link LabItem} object.
	 * 
	 * @return
	 */
	public List<LabResultTest> getUnresolvedLabResults() {
		List<LabResultTest> ret = new ArrayList<LabResultTest>();
		for (AbstractFindingContent contentObj : content) {
			if (contentObj.getTyp() == Typ.TEXT)
				continue;
			LabResultInfo labResult = contentObj.result;
			if (source != null && patient != null) {
				for (LabResultTest test : labResult.tests) {
					LabItem item = null;
					try {
						item = test.getLabItem(source,
								patient.getGeschlecht());
					} catch (LabResultNonUniqueException e) {
						// ignore if multiple items are found for one result
					}
					if (item == null) {
						ret.add(test);
					} else {
						test.setLabItem(item);
					}
				}
			} else {
				for (LabResultTest test : labResult.tests) {
					ret.add(test);
				}
			}
		}
		return ret;
	}

	public List<AbstractFindingContent> getContent() {
		if (content == null)
			return new ArrayList<AbstractFindingContent>();
		return content;
	}

	public void addContent(AbstractFindingContent content) {
		if (this.content == null)
			this.content = new ArrayList<AbstractFindingContent>();

		this.content.add(content);
	}

	public ContactInfo getPatientInfo() {
		return patientInfo;
	}

	public void setPatientInfo(ContactInfo patientInfo) {
		this.patientInfo = patientInfo;
	}

	public ContactInfo getSourceInfo() {
		return sourceInfo;
	}

	public void setSourceInfo(ContactInfo sourceInfo) {
		this.sourceInfo = sourceInfo;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
		// update the patient info data
		if (patient != null)
			if (patientInfo == null)
				patientInfo = new ContactInfo();
		patientInfo.setPatient(patient);
		
		propertyChangeSupport.firePropertyChange("patient", null,
				this.patient);
		propertyChangeSupport.firePropertyChange("description", null,
				getDescription());
	}

	public Kontakt getSource() {
		return source;
	}

	public void setSource(Kontakt source) {
		this.source = source;
		// update the source info data
		if (source != null) {
			if (sourceInfo == null)
				sourceInfo = new ContactInfo();
			sourceInfo.setKontakt(source);
		}
		
		propertyChangeSupport.firePropertyChange("source", null,
				this.source);
		propertyChangeSupport.firePropertyChange("description", null,
				getDescription());
	}

	public void setSourceInfo(NetworkParticipant sender) {
		if (sender == null)
			return;

		if (sourceInfo == null)
			sourceInfo = new ContactInfo();

		sourceInfo.setLastname(sender.getNachname());
		sourceInfo.setFirstname(sender.getVorname());
		sourceInfo.setCity(sender.getOrtschaft());
		sourceInfo.setMenumber(sender.getHVNummer());
		// look for an attached kontakt of the sender
		Kontakt kontakt = sender.getKontakt();
		if(kontakt != null)
			setSource(kontakt);
	}

	public void setDate(Date date) {
		this.date = DATE_FORMAT.format(date);
	}

	public String getDate() {
		return date;
	}
	
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		if(source != null) {
			sb.append(source.getLabel());
		} else if(sourceInfo != null) {
			sb.append(sourceInfo.getLastname());
			sb.append(" "); //$NON-NLS-1$
			sb.append(sourceInfo.getFirstname());
			sb.append(", ");
			sb.append("[" + sourceInfo.getMenumber() + "]");
		} else {
			sb.append("unknown source");
		}
		sb.append(" -> ");
		if(patient != null) {
			sb.append(patient.getLabel());
		} else if(patientInfo != null) {
			sb.append(patientInfo.getLastname());
			sb.append(" "); //$NON-NLS-1$
			sb.append(patientInfo.getFirstname());
			sb.append(", ");
			sb.append(patientInfo.getBirthdate());
		} else {
			sb.append("unknown patient");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getDescription();
	}

	/**
	 * Get a pdf file containing the information of this finding.
	 * @return pdf file as ByteArrayOutputStream
	 * @throws {@link IOException}
	 * @throws {@link FormattedOutputException}
	 */
	public ByteArrayOutputStream getPdfFile() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		URL xslt = null;
		Bundle bundle = Platform
				.getBundle("at.medevit.elexis.befuem.contextservice");
		xslt = bundle.getEntry("/rsc/xslt/elexisfinding2fo.xslt");

		IFormattedOutput output = FormattedOutputConsumer
				.getOutputImplementations(ObjectType.JAXB, OutputType.PDF);
		output.transform(this, xslt.openStream(), out);

		return out;
	}
	
	public String getKeywords() {
		if(keywords != null)
			return keywords;
		return "";
	}
	
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
}

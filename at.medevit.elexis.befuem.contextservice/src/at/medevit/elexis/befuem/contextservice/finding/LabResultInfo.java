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

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LabResultInfo {
	@XmlElement(name="resultdate")
	String resultDateString;
	@XmlTransient
	Date resultDate;
	@XmlElement(name="materialdate")
	String materialDateString;
	@XmlTransient
	Date materialDate;
	@XmlElement(name="resultinfo")
	String resultInfo;
	@XmlTransient
	String referenceNumber;
	
	@XmlElementWrapper(name="test")
	@XmlElement(name="testresult")
	List<LabResultTest> tests;
	
	@XmlTransient
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	public Date getResultDate() {
		return resultDate;
	}

	public String getResultDateString() {
		return resultDateString;
	}	
	
	public void setResultDate(Date resultDate) {
		this.resultDate = resultDate;
		// keep actual date as string
		this.resultDateString = DATE_FORMAT.format(resultDate);
	}

	public void setResultDateString(String resultDateString) {
		this.resultDateString = resultDateString;
	}	
	
	public Date getMaterialDate() {
		return materialDate;
	}

	public String getMaterialDateString() {
		return materialDateString;
	}	
	
	public void setMaterialDate(Date materialDate) {
		this.materialDate = materialDate;
		// keep actual date as string
		this.materialDateString = DATE_FORMAT.format(materialDate);
	}

	public void setMaterialDateString(String materialDateString) {
		this.materialDateString = materialDateString;
	}	
	
	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public List<LabResultTest> getTests() {
		return tests;
	}

	public void addTest(LabResultTest test) {
		if(tests == null)
			tests = new ArrayList<LabResultTest>();
		tests.add(test);
	}
}

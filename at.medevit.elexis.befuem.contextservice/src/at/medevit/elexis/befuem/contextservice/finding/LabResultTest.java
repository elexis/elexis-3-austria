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

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import ch.elexis.data.Kontakt;
import ch.elexis.data.LabItem;
import ch.elexis.data.LabMapping;
import ch.elexis.data.LabResult;
import ch.elexis.data.Patient;
import ch.rgw.tools.TimeTool;

/**
 * Representation of a test from a lab result.
 * Supports XML transformation.
 * 
 * @author thomas
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LabResultTest {
	@XmlElement(name="itemcode")
	String itemCode;
	@XmlElement(name="itemshortdesc")
	String itemShortDescription;
	@XmlElement(name="itemdesc")
	String itemDescription;
	
	@XmlElement(name="data")
	String data;
	@XmlElement(name="pathologiccode")
	String pathologic;
	@XmlElement(name="dataunit")
	String dataUnit;
	@XmlElement(name="dataunitdesc")
	String dataUnitDescription;
	@XmlElement(name="datainfo")
	String dataInfo;
	
	@XmlElement(name="refmin")
	String refMin;
	@XmlElement(name="refmax")
	String refMax;
	@XmlElement(name="refunit")
	String refUnit;
	@XmlElement(name="refunitdesc")
	String refUnitDescritption;
	@XmlElement(name="refinfo")
	String refInfo;
	@XmlTransient
	private LabItem labItem;
	@XmlTransient
	private static final String empty = "";

	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(itemShortDescription);
		sb.append(", ");
		sb.append(getRefString());
		sb.append(", ");
		sb.append(dataUnit);
		return sb.toString();
	}

	public String getItemCode() {
		if(itemCode == null)
			return empty;
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		if(itemCode != null)
			this.itemCode = itemCode.trim();
		else
			this.itemCode = itemCode;
	}

	public String getItemShortDescription() {
		if(itemShortDescription == null)
			return empty;
		return itemShortDescription;
	}

	public void setItemShortDescription(String itemShortDescription) {
		if(itemShortDescription != null)
			this.itemShortDescription = itemShortDescription.trim();
		else
			this.itemShortDescription = itemShortDescription;
	}

	public String getItemDescription() {
		if(itemDescription == null)
			return empty;
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		if(itemDescription != null)
			this.itemDescription = itemDescription.trim();
		else
			this.itemDescription = itemDescription;
	}

	public String getData() {
		if(data == null)
			return empty;
		return data;
	}

	public void setData(String data) {
		if(data != null)
			this.data = data.trim();
		else
			this.data = data;
	}

	public String getPathologic() {
		if(pathologic == null)
			return empty;
		return pathologic;
	}

	public void setPathologic(String pathologic) {
		if(pathologic != null)
			this.pathologic = pathologic.trim();
		else
			this.pathologic = pathologic;
	}

	public String getDataUnit() {
		if(dataUnit == null)
			return empty;
		return dataUnit;
	}

	public void setDataUnit(String dataUnit) {
		if(dataUnit != null)
			this.dataUnit = dataUnit.trim();
		else
			this.dataUnit = dataUnit;
	}

	public String getDataUnitDescription() {
		if(dataUnitDescription == null)
			return empty;
		return dataUnitDescription;
	}

	public void setDataUnitDescription(String dataUnitDescription) {
		if(dataUnitDescription != null)
			this.dataUnitDescription = dataUnitDescription.trim();
		else
			this.dataUnitDescription = dataUnitDescription;
	}

	public String getRefMin() {
		if(refMin == null)
			return empty;
		return refMin;
	}

	public void setRefMin(String refMin) {
		if(refMin != null)
			this.refMin = refMin.trim();
		else
			this.refMin = refMin;
	}

	public String getRefMax() {
		if(refMax == null)
			return empty;
		return refMax;
	}

	public void setRefMax(String refMax) {
		if(refMax != null)
			this.refMax = refMax.trim();
		else
			this.refMax = refMax;
	}

	public String getRefUnit() {
		if(refUnit == null)
			return empty;
		return refUnit;
	}

	public void setRefUnit(String refUnit) {
		if(refUnit != null)
			this.refUnit = refUnit.trim();
		else
			this.refUnit = refUnit;
	}

	public String getRefUnitDescritption() {
		if(refUnitDescritption == null)
			return empty;
		return refUnitDescritption;
	}

	public void setRefUnitDescription(String refUnitDescritption) {
		if(refUnitDescritption != null)
			this.refUnitDescritption = refUnitDescritption.trim();
		else
			this.refUnitDescritption = refUnitDescritption;
	}

	public String getDataInfo() {
		if(dataInfo == null)
			return empty;
		return dataInfo;
	}

	public void setDataInfo(String dataInfo) {
		if(dataInfo != null)
			this.dataInfo = dataInfo.trim();
		else
			this.dataInfo = dataInfo;
	}
	
	public String getRefInfo() {
		if(refInfo == null)
			return empty;
		return refInfo;
	}

	public void setRefInfo(String refInfo) {
		if(refInfo != null)
			this.refInfo = refInfo.trim();
		else
			this.refInfo = refInfo;
	}

	public void setLabItem(LabItem labItem) {
		this.labItem = labItem;
	}
	
	/**
	 * Getter method for field.
	 * @return
	 */
	public LabItem getLabItem() {
		return labItem;
	}
	
	/**
	 * Get the reference values as one string.
	 * <li>
	 * If both are set refMin-refMax is returned.
	 * </li>
	 * <li>
	 * If only one is set that value is returned.
	 * </li>
	 * <li>
	 * If none is set null is returned.
	 * </li>
	 * @return String or null
	 */
	public String getRefString() {
		String refMin = null;
		String refMax = null;
		
		if(getRefMin() != null && getRefMin().length() > 0)
			refMin = getRefMin();
		if(getRefMax() != null && getRefMax().length() > 0)
			refMax = getRefMax();
		
		if(refMin != null && refMax != null)
			return refMin + "-" + refMax;
		else if (refMin != null)
			return refMin;
		else if (refMax != null)
			return refMax;
		else
			return empty;
	}
	
	/**
	 * Check if the result is storno
	 * @return
	 */
	public boolean isStorno() {
		return data.startsWith("!!") && (data.contains("Storno") || data.contains("storno"));
	}
	
	/**
	 * Check if the result already exists in the database
	 * @param patient
	 * @param date
	 * @return
	 */
	public boolean isDuplicate(Patient patient, Date date) {
		if(labItem == null || patient == null || date == null)
			return false;
		LabResult result = LabResult.getForDate(patient,
				new TimeTool(date.getTime()), labItem);
		return result != null;
	}
	
	/**
	 * Create a LabResult for this test.
	 * @param patient
	 * @param date
	 * @return the new LabResult
	 */
	public LabResult createLabResult(Patient patient, Date date) {
		LabResult lr = new LabResult(patient, new TimeTool(
				date.getTime()), labItem, data, dataInfo);
		if (pathologic != null && pathologic.length() > 0) {
			lr.setFlag(LabResult.PATHOLOGIC, true);
		}
		return lr;
	}
	
	/**
	 * Create a LabItem for this test. If an LabItem can be found by
	 * getLabItem, no new LabItem is created, and the found LabItem is returned.
	 * If a LabItem for the other gender exists and has no reference value set for
	 * the actual gender. Set the new reference value for the LabItem and return it.
	 * 
	 * @param laborId
	 * @param patientGender
	 * @return LabItem or null
	 */
	public LabItem createLabItem(Kontakt labor, String patientGender, int sequenz) {
		if(labor == null || patientGender == null)
			return null;
		// look for a existing item
		LabItem item = null;
		try {
			item = getLabItem(labor, patientGender);
		} catch (LabResultNonUniqueException e) {
			// if more than one item found, dont create new item but return first matching item
			return e.getItems().get(0);
		}
		if(item != null)
			return item;
		// look for a existing LabItem ignoring the gender
		// if found and no ref value set yet set the new ref value
		LabItem itemIgnoreGender = null;
		try {
			itemIgnoreGender = getLabItem(labor, null);
		} catch (LabResultNonUniqueException e) {
			// if more than one item found, dont create new item but return first matching item
			itemIgnoreGender = e.getItems().get(0);
		}
		if(itemIgnoreGender != null) {
			if(patientGender.equalsIgnoreCase("w")) {
				String ref = itemIgnoreGender.getRefW();
				if(!(ref != null && ref.length() > 0)) {
					itemIgnoreGender.setRefW(getRefString());
					return itemIgnoreGender;
				}
			} else if (patientGender.equalsIgnoreCase("m")) {
				String ref = itemIgnoreGender.getRefM();
				if(!(ref != null && ref.length() > 0)) {
					itemIgnoreGender.setRefM(getRefString());
					return itemIgnoreGender;
				}
			}
		}
		// create a new LabItem
		// substitute description with short description if its not set
		String desc = getItemDescription();
		if(desc.length() < 1)
			desc = getItemShortDescription();
		
		if(patientGender.equalsIgnoreCase("w")) {
			item = new LabItem(getItemShortDescription(), desc,
					labor, null, getRefString(), getDataUnit(),
					LabItem.typ.NUMERIC, "auto created", Integer.toString(sequenz));
		} else if (patientGender.equalsIgnoreCase("m")) {
			item = new LabItem(getItemShortDescription(), desc,
					labor, getRefString(), null, getDataUnit(),
					LabItem.typ.NUMERIC, "auto created", Integer.toString(sequenz));			
		}
		return item;
	}
	
	/**
	 * Get the corresponding {@link LabItem} for this test. If there is more than one LabItem
	 * matching this result a {@link LabResultNonUniqueException} containing the LabItems is thrown.
	 * @param laborId			Id of the labor sending the test
	 * @param patientGender		gender of the patient this test is for
	 * @return LabItem or null
	 */
	public LabItem getLabItem(Kontakt labor, String patientGender) throws LabResultNonUniqueException {
		if(labor == null)
			return null;
		List<LabItem> items = null;
		LabItem item = null;
		
		if (labor != null) {
			// consider LabMapping to resolve the LabItem
			LabMapping mapping =
				LabMapping.getByContactAndItemName(labor.getId(), getItemShortDescription());
			if (mapping != null) {
				return mapping.getLabItem();
			}
		}
		
		if(patientGender != null) {
			if(patientGender.equalsIgnoreCase("w")) {
				items = LabItem.getLabItems(labor.getId(),
						getItemShortDescription(),
						null,
						getRefString(),
						getDataUnit());
			} else if (patientGender.equalsIgnoreCase("m")){
				items = LabItem.getLabItems(labor.getId(),
						getItemShortDescription(),
						getRefString(),
						null,
					    getDataUnit());
			} 
		}else {
			items = LabItem.getLabItems(labor.getId(),
					getItemShortDescription(),
					null,
					null,
				    getDataUnit());
		}
		if(items != null) {
			if(items.size() == 1)
				item = items.get(0);
			else if(items.size() > 0)
				throw new LabResultNonUniqueException(items);
		}
		return item;
	}
}

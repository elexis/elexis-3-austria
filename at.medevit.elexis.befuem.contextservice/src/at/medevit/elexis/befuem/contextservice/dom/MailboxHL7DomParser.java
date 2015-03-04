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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.medevit.elexis.befuem.contextservice.finding.ContactInfo;
import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.LabContent;
import at.medevit.elexis.befuem.contextservice.finding.LabResultInfo;
import at.medevit.elexis.befuem.contextservice.finding.LabResultTest;

public class MailboxHL7DomParser extends AbstractDomParser {

	@Override
	public List<ElexisFinding> getElexisFindings(DOMResult dom) {
		ArrayList<ElexisFinding> result = new ArrayList<ElexisFinding>();
		// there is no info about the source in this format
		ContactInfo source = new ContactInfo();
		
		Node csvset = getCsvSet(dom);
		if(csvset != null) {
			List<Node> records = getChildElementsByTagName(csvset, "csv-record");
			if(records.size() > 0) {
				ContactInfo patient = null;
				ElexisFinding ef = null;
				Date findingDate = null;
				// find header line and create patient
				for(Node record : records) {
					Node linecode = getChildElementByTagName(record, "linecode");
					if(getTextForNode(linecode).equalsIgnoreCase("H")) {
						patient = getPatientInfo(record);
						findingDate = getFindingDate(record);
						break;
					}
				}
				ef = new ElexisFinding(patient, source);
				LabResultInfo labResult = new LabResultInfo();
				if(findingDate != null)
					labResult.setResultDate(findingDate);
				// create LabResultTest objects for the content
				LabResultTest currentTest = null;
				StringBuilder comment = null;
				for(Node record : records) {
					Node linecode = getChildElementByTagName(record, "linecode");
					if(getTextForNode(linecode).equalsIgnoreCase("E")) {
						// a linenr != 1 signals the following records are comment to lastrecord
						Node linenr = getChildElementByTagName(record, "linenr");
						String linenrStr = getTextForNode(linenr);
						if(linenrStr.equalsIgnoreCase("1")) {
							if(comment != null) {
								if(currentTest != null)
									currentTest.setDataInfo(comment.toString());
								comment = null;
							}
							LabResultTest testObj = getLabResultTest(record);
							labResult.addTest(testObj);
							currentTest = testObj;
						} else {
							if(comment == null)
								comment = new StringBuilder();
							comment.append(getComment(record));
							continue;
						}
					}
				}
				LabContent content = new LabContent("", labResult, ef);
				ef.addContent(content);
				result.add(ef);
			}
		}
		return result;
	}

	private Node getCsvSet(DOMResult dom) {
		Node result = null;
		if(dom != null && (dom.getNode() != null)) {
			Document doc = (Document)dom.getNode();
			NodeList sets = doc.getElementsByTagName("csv-set");
			int length = sets.getLength();
			if(length > 0) {
				result = sets.item(0);
			}
		}
		return result;
	}
	
	private ContactInfo getPatientInfo(Node record) {
		ContactInfo result = new ContactInfo();

		result.setLastname(getTextForNode(getChildElementByTagName(record, "lastname")));
		result.setFirstname(getTextForNode(getChildElementByTagName(record, "firstname")));
		result.setGender(getTextForNode(getChildElementByTagName(record, "gender")));
		result.setBirthdate(getTextForNode(getChildElementByTagName(record, "birthdate")));

		return result;
	}
	
	private Date getFindingDate(Node record) {
		String fdate = getTextForNode(getChildElementByTagName(record, "indate"));
		Date ret = null;
		
		if(fdate != null && fdate.length() > 0) {
			ret = getDateForString(fdate, "yyMMdd");
		}
		return ret;
	}
	
	private String getComment(Node record) {
		return getTextForNode(getChildElementByTagName(record, "data"));
	}
	
	private LabResultTest getLabResultTest(Node record) {
		LabResultTest result = new LabResultTest();

		result.setItemShortDescription(
			getTextForNode(getChildElementByTagName(record, "shorttext")));
		
		Node node = getChildElementByTagName(record, "ispathologic");
		if(node != null) {
			if(getTextForNode(node).equalsIgnoreCase("*")) {
				result.setPathologic(getTextForNode(getChildElementByTagName(record, "pathologiccode")));
			}
		}
		
		result.setData(getTextForNode(getChildElementByTagName(record, "data")));
		result.setDataUnit(getTextForNode(getChildElementByTagName(record, "unit")));
		String refStr = getTextForNode(getChildElementByTagName(record, "refdata"));
		String[] refs = refStr.split("-");
		if(refs.length > 0) {
			if(refs.length == 1) {
				result.setRefMax(refs[0]);
			} else if (refs.length == 2) {
				result.setRefMin(refs[0]);
				result.setRefMax(refs[1]);
			}
		}
		return result;
	}
}

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class EdiLabDomParser extends AbstractDomParser {

	@Override
	public List<ElexisFinding> getElexisFindings(DOMResult dom) {
		
//		System.out.println(XmlUtil.serialize(dom.getNode().getChildNodes(), true));
		
		ArrayList<ElexisFinding> result = new ArrayList<ElexisFinding>();
		ContactInfo source = getSourceInfo(dom);
		
		List<Node> findings = getFindings(dom);
		if(findings.size() > 0) {
			for(Node finding : findings) {
				ContactInfo patient = getPatientInfo(finding);
				ElexisFinding ef = new ElexisFinding(patient, source);
				String text = getTextSegmentForNode(finding, null);
				LabResultInfo info = getLabResultInfo(finding);
				LabContent content = new LabContent(text, info, ef);
				ef.addContent(content);
				result.add(ef);
			}

		}
		return result;
	}

	/**
	 * get textsegment as string
	 * 
	 * FTX
	 * 
	 * if description is null all textsegments for this node a are
	 * combined to one String
	 * 
	 * if description is not null all textsegments with a matching
	 * description field are combined to one String
	 * 
	 * @param node
	 * @param description
	 * @return
	 */
	private String getTextSegmentForNode(Node node , String description) {
		StringBuilder text = new StringBuilder();
		
		if(description == null) {
			List<Node> textsegments = getChildElementsByTagName(node, "textsegment");
			int length = textsegments.size();
			for(int i = 0; i < length; i++) {
				Node segment = textsegments.get(i);
				Node field = getChildElementByTagName(segment, "textfield");
				List<Node> texts = getChildElementsByTagName(field, "text");
				int size = texts.size();
				for(int j = 0; j < size; j++) {
					text.append(getTextForNode(texts.get(j)));
				}
			}
		} else {
			List<Node> textsegments = getChildElementsByTagName(node, "textsegment");
			int length = textsegments.size();
			for(int i = 0; i < length; i++) {
				Node segment = textsegments.get(i);
				Node desc = getChildElementByTagName(segment, "description");
				if(getTextForNode(desc).equalsIgnoreCase(description)) {
					Node field = getChildElementByTagName(segment, "textfield");
					List<Node> texts = getChildElementsByTagName(field, "text");
					int size = texts.size();
					for(int j = 0; j < size; j++) {
						text.append(getTextForNode(texts.get(j)));
					}
				}
			}
		}
		return text.toString();
	}

	/**
	 * get source information
	 * 
	 * UNB
	 * 
	 * @param dom
	 * @return
	 */
	private ContactInfo getSourceInfo(DOMResult dom) {
		ContactInfo result = null;
		if(dom != null && (dom.getNode() != null)) {
			Document doc = (Document)dom.getNode();
			NodeList infos = doc.getElementsByTagName("docinfo");
			int length = infos.getLength();
			if(length > 0) {
				Node info = infos.item(0);
				result = new ContactInfo();
				result.setMenumber(getTextForNode(getChildElementByTagName(info, "sender")));
			}
		}
		return result;
	}
	
	/**
	 * get all finding elements
	 * 
	 * UNH
	 * 
	 * @param dom
	 * @return
	 */
	private List<Node> getFindings(DOMResult dom) {
		ArrayList<Node> result = new ArrayList<Node>();
		if(dom != null && (dom.getNode() != null)) {
			Document doc = (Document)dom.getNode();
			NodeList findings = doc.getElementsByTagName("finding");
			int length = findings.getLength();
			if(length > 0) {
				for(int i = 0; i < length; i++) {
					result.add(findings.item(i));
				}
			}
		}
		return result;
	}
	
	private ContactInfo getPatientInfo(Node finding) {
		ContactInfo result = null;
		Node patient = getChildElementByTagName(finding, "patient");
		if(patient != null) {
			result = new ContactInfo();
			Node id = getChildElementByTagName(patient, "id");
			if(id != null) {
				result.setLastname(getTextForNode(getChildElementByTagName(id, "lastname")));
				result.setFirstname(getTextForNode(getChildElementByTagName(id, "firstname")));
			}
			Node street = getChildElementByTagName(patient, "street");
			if(street != null) {
				result.setStreet1(getTextForNode(getChildElementByTagName(street, "street1")));
				result.setStreet2(getTextForNode(getChildElementByTagName(street, "street2")));
			}
			result.setCity(getTextForNode(getChildElementByTagName(patient, "city")));
			result.setZip(getTextForNode(getChildElementByTagName(patient, "zip")));
			result.setGender(getTextForNode(getChildElementByTagName(patient, "gender")));
			String birthdate = getTextForNode(getChildElementByTagName(patient, "birthdate"));
			if(birthdate != null && birthdate.length() > 0) {
				// check for optional patient number
				int sepIdx = birthdate.indexOf('/');
				if(sepIdx != -1) {
					birthdate = birthdate.substring(sepIdx + 1);
				}
				birthdate = getFormattedDateForString(birthdate, "yyyyMMdd", "dd.MM.yyyy");
			}
			result.setBirthdate(birthdate);
			
			// look for the insurance number
			List<Node> references = getChildElementsByTagName(finding, "reference");
			for(Node reference : references) {
				Node refcode = getChildElementByTagName(reference, "refcode");
				Node code = getChildElementByTagName(refcode, "code");
				if(getTextForNode(code).equalsIgnoreCase("VNP")) {
					Node value = getChildElementByTagName(refcode, "value");
					result.setInsurancenumber(getTextForNode(value));
				}
			}
		}
		return result;
	}
	
	private LabResultInfo getLabResultInfo(Node finding) {
		LabResultInfo result = null;
		
		List<Node> tests = getChildElementsByTagName(finding, "test");
		if(tests.size() > 0) {
			result = new LabResultInfo();
			for(Node test : tests) {
				Node description = getChildElementByTagName(test, "testdescription");
				Node value = getChildElementByTagName(test, "testvalue");
				if(description != null) {
					fillTestDescription(description, result);
				}
				if(value != null) {
					LabResultTest testObj = getLabResultTest(value);
					result.addTest(testObj);
				}
				// TODO add possible info text
			}
		}
		return result;
	}
	
	private LabResultTest getLabResultTest(Node testvalue) {
		LabResultTest result = null;
		Node description = getChildElementByTagName(testvalue, "description");
		List<Node> values = getChildElementsByTagName(testvalue, "value");
		if(description != null && values.size() > 0) {
			result = new LabResultTest();
			// description
			result.setItemCode(getTextForNode(getChildElementByTagName(description, "code")));
			Node itemText = getChildElementByTagName(description, "text");
			if(itemText != null) {
				result.setItemShortDescription(
						getTextForNode(getChildElementByTagName(itemText, "shorttext")));
				result.setItemDescription(
						getTextForNode(getChildElementByTagName(itemText, "text")));
			}
			// test value and possible ref value
			for(Node value : values) {
				Node valueId = getChildElementByTagName(value, "id1");
				if(valueId != null) {
					if(getTextForNode(valueId).equalsIgnoreCase("1")) {
						Node node = getChildElementByTagName(value, "measure");
						result.setData(
								getTextForNode(getChildElementByTagName(node, "data")));
						
						node = getChildElementByTagName(value, "entity");
						result.setDataUnit(
								getTextForNode(getChildElementByTagName(node, "unit")));
						result.setDataUnitDescription(
								getTextForNode(getChildElementByTagName(node, "unittext")));
						
						result.setDataInfo(getTextSegmentForNode(testvalue, "ZEG"));
					} else if(getTextForNode(valueId).equalsIgnoreCase("2")) {
						Node node = getChildElementByTagName(value, "measure");
						result.setRefMin(
								getTextForNode(getChildElementByTagName(node, "data")));
						
						node = getChildElementByTagName(value, "referencemax");
						result.setRefMax(
								getTextForNode(getChildElementByTagName(node, "data")));
						
						node = getChildElementByTagName(value, "entity");
						result.setRefUnit(
								getTextForNode(getChildElementByTagName(node, "unit")));
						result.setRefUnitDescription(
								getTextForNode(getChildElementByTagName(node, "unittext")));
						
						result.setRefInfo(getTextSegmentForNode(testvalue, "ZRE"));
					}
				}
			}
			// todo add textsegment as info if present ...
		}
		return result;
	}
	
	private void fillTestDescription(Node description, LabResultInfo result) {
		// dates
		List<Node> dates = getChildElementsByTagName(description, "date");
		if(dates.size() > 0) {
			for(Node date : dates) {
				Node datevalue = getChildElementByTagName(date, "datevalue");
				Node code = getChildElementByTagName(datevalue, "code");
				if(getTextForNode(code).equalsIgnoreCase("MDT")) {
					Date materialDate = getDateValue(datevalue);
					if(materialDate != null) {
						result.setMaterialDate(materialDate);
					}
					result.setMaterialDateString(
							getTextForNode(
									getChildElementByTagName(datevalue, "value")));
				} else if (getTextForNode(code).equalsIgnoreCase("119")) {
					Date resultDate = getDateValue(datevalue);
					if(resultDate != null) {
						result.setResultDate(resultDate);
					}
					result.setResultDateString(
							getTextForNode(
									getChildElementByTagName(datevalue, "value")));
				}
			}
		}
		// reference
		Node reference = getChildElementByTagName(description, "reference");
		if(reference != null) {
			Node refcode = getChildElementByTagName(reference, "refcode");
			Node refvalue = getChildElementByTagName(refcode, "value");
			result.setReferenceNumber(getTextForNode(refvalue));
		}
		// text
		result.setResultInfo(getTextSegmentForNode(description, null));
	}
	
	private Date getDateValue(Node datevalue) {
		Date result = null;
		SimpleDateFormat dateformat = null;
		
		Node format = getChildElementByTagName(datevalue, "format");
		Node value = getChildElementByTagName(datevalue, "value");
		if(format != null && value != null) {
			try {
				if(getTextForNode(format).equalsIgnoreCase("101")) {
					// JJMMTT
					dateformat = new SimpleDateFormat("yyMMdd");
					result = dateformat.parse(getTextForNode(value));
				} else if(getTextForNode(format).equalsIgnoreCase("102")) {
					// JJJJMMTT
					dateformat = new SimpleDateFormat("yyyyMMdd");
					result = dateformat.parse(getTextForNode(value));
				}
			} catch (ParseException e) {
				// quietly ignore we will set the string value anyway
				return null;
			}
		}
		return result;
	}
	
	@Override
	protected String getTextForNode(Node node) {
		String ret = super.getTextForNode(node);
		if(ret != null) {
			ret = ret.replaceAll("\\?.", ".");
			ret = ret.replaceAll("\\?:", ":");
			ret = ret.replaceAll("\\a", "");
			ret = ret.replaceAll("\\e", "");
			ret = ret.replaceAll("\\f", "");
			ret = ret.replaceAll("\\v", "");
		}
		return ret;
	}
}

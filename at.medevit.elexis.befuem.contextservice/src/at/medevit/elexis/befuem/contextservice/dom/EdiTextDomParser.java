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
import java.util.List;

import javax.xml.transform.dom.DOMResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.medevit.elexis.befuem.contextservice.finding.ContactInfo;
import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;
import at.medevit.elexis.befuem.contextservice.finding.TextContent;

public class EdiTextDomParser extends AbstractDomParser {
	private static Logger log = LoggerFactory.getLogger(EdiTextDomParser.class);
	
	public List<ElexisFinding> getElexisFindings(DOMResult dom) {
		ArrayList<ElexisFinding> result = new ArrayList<ElexisFinding>();
		ContactInfo source = getSourceInfo(dom);
		
		List<Node> findings = getFindings(dom);
		if(findings.size() > 0) {
			for(Node finding : findings) {
				ContactInfo patient = getPatientInfo(finding);
				ElexisFinding ef = new ElexisFinding(patient, source);
				ef.addContent(new TextContent(getTextForFinding(finding), ef));
				result.add(ef);
			}
		} else {
			log.warn("EdiTextDomParser: no findings found in DOM.");
		}
		return result;
	}
	
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
	
	private String getTextForFinding(Node finding) {
		StringBuilder text = new StringBuilder();
		
		List<Node> textsegments = getChildElementsByTagName(finding, "textsegment");
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
		return text.toString();
	}
	
	private ContactInfo getPatientInfo(Node finding) {
		ContactInfo result = null;
		Node patient = getChildElementByTagName(finding, "patient");
		Node id = getChildElementByTagName(patient, "id");
		result = new ContactInfo();
		if(id != null) {
			result.setLastname(getTextForNode(getChildElementByTagName(id, "lastname")));
			result.setFirstname(getTextForNode(getChildElementByTagName(id, "firstname")));
			result.setStreet1(getTextForNode(getChildElementByTagName(id, "street1")));
			result.setStreet2(getTextForNode(getChildElementByTagName(id, "street2")));
			result.setCity(getTextForNode(getChildElementByTagName(patient, "city")));
			result.setZip(getTextForNode(getChildElementByTagName(patient, "zip")));
		}
		id = getChildElementByTagName(finding, "findingid");
		if(id != null) {
			String birthdate = getTextForNode(getChildElementByTagName(id, "birthdate"));
			if(birthdate != null && birthdate.length() > 0) {
				birthdate = getFormattedDateForString(birthdate, "yyyyMMdd", "dd.MM.yyyy");
				result.setBirthdate(birthdate);
			}
			String insurance = getTextForNode(getChildElementByTagName(id, "insurance"));
			if(insurance != null && insurance.length() > 0) {
				result.setInsurancenumber(insurance);
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
			// remove bad characters ... leave formatting characters
			ret = ret.replaceAll("[\\x00-\\x08]", "");
			ret = ret.replaceAll("[\\x0B-\\x0C]", "");
			ret = ret.replaceAll("[\\x0E-\\x1F]", "");
		}
		return ret;
	}
}

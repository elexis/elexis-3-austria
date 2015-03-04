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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.medevit.elexis.befuem.contextservice.finding.ElexisFinding;

public abstract class AbstractDomParser {
	
	public abstract List<ElexisFinding> getElexisFindings(DOMResult dom);
	
	protected String getTextForNode(Node node) {
		if(node == null)
			return null;
		else
			return node.getNodeValue();
	}
	
	protected Node getChildElementByTagName(Node node, String name) {
		if(node != null) {
			NodeList nodes = node.getChildNodes();
			if(nodes != null) {
				for(int i = 0; i < nodes.getLength(); i++) {
					Node subnode = nodes.item(i);
					if(subnode != null && subnode.getNodeType() == Node.ELEMENT_NODE) {
						if(subnode.getNodeName().equalsIgnoreCase(name)) {
							return subnode;
						}
					}
				}
			}
		}
		return null;
	}
	
	protected List<Node> getChildElementsByTagName(Node node, String name) {
		ArrayList<Node> result = new ArrayList<Node>();
		if(node != null) {
			NodeList nodes = node.getChildNodes();
			if(nodes != null) {
				for(int i = 0; i < nodes.getLength(); i++) {
					Node subnode = nodes.item(i);
					if(subnode != null && subnode.getNodeType() == Node.ELEMENT_NODE) {
						if(subnode.getNodeName().equalsIgnoreCase(name)) {
							result.add(subnode);
						}
					}
				}
			}
		}
		return result;
	}
	
	protected String getFormattedDateForString(String value, String fromFormat, String toFormat) {
		SimpleDateFormat fromformat = new SimpleDateFormat(fromFormat);
		SimpleDateFormat toformat = new SimpleDateFormat(toFormat);
		Date tmp;
		try {
			tmp = fromformat.parse(value);
			return toformat.format(tmp);
		} catch (ParseException e) {
			// on error just return the input ...
			return value;
		}
	}
	
	protected Date getDateForString(String value, String fromFormat) {
		SimpleDateFormat fromformat = new SimpleDateFormat(fromFormat);
		Date tmp = null;
		try {
			tmp = fromformat.parse(value);
			return tmp;
		} catch (ParseException e) {
			return null;
		}
	}
}

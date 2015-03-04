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
package at.medevit.elexis.kassen.core.ui;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import ch.elexis.core.data.interfaces.IRnOutputter;
import ch.elexis.core.data.util.Extensions;
import ch.elexis.data.Rechnung;
import ch.rgw.tools.Money;

public abstract class WahlarztRnOutputter implements IRnOutputter {
	
	protected CheckboxTableViewer catalogsTable;
	
	public Control createSettingsControl(Composite parent){
		Composite ret = new Composite(parent, SWT.NONE);
		ret.setLayout(new FormLayout());
		
		// catalog
		Label lbl = new Label(ret, SWT.NONE);
		lbl.setText("Nicht aufzuführende Leistungen nach Katalog.");
		Table table = new Table(ret, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.CHECK);
		catalogsTable = new CheckboxTableViewer(table);
		catalogsTable.setContentProvider(new ArrayContentProvider());
		List<String> catalogs = getAllCatalogs();
		catalogsTable.setInput(catalogs);
		catalogsTable.setAllChecked(false);
		
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		lbl.setLayoutData(fd);
		
		fd = new FormData();
		fd.top = new FormAttachment(lbl, 5);
		fd.left = new FormAttachment(0, 5);
		fd.right = new FormAttachment(100, -5);
		catalogsTable.getControl().setLayoutData(fd);
		
		return ret;
	}
	
	protected void setOffenerBetragAsEndsumme(Document xmlRechnung, Rechnung rechnung){
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			XPathExpression expr = xpath.compile("/PKVRechnung/EndsummenRechnung/EndsummeBrutto");
			
			NodeList elemList = (NodeList) expr.evaluate(xmlRechnung, XPathConstants.NODESET);
			if (elemList != null && elemList.getLength() == 1) {
				Money offen = rechnung.getOffenerBetrag();
				elemList.item(0).setNodeValue(offen.getAmountAsString());
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	protected void checkXmlRechnung(Document rechnung, String[] checkValues){
		try {
			for (int i = 0; i < checkValues.length; i++) {
				if (!checkDomElementNotEmpty(checkValues[i], rechnung))
					throw new IllegalStateException(checkValues[i] + " is empty.");
			}
		} catch (XPathExpressionException ex) {
			ex.printStackTrace();
		}
	}
	
	private boolean checkDomElementNotEmpty(String id, Document doc)
		throws XPathExpressionException{
		
		boolean ret = true;
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(id);
		
		NodeList elemList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		
		if (elemList.getLength() == 0)
			return false;
		
		for (int i = 0; i < elemList.getLength(); i++) {
			String content = elemList.item(i).getNodeValue();
			if (content == null)
				ret = false;
			else if (content.length() == 0)
				return false;
		}
		
		return ret;
	}
	
	private static ArrayList<String> getAllCatalogs(){
		ArrayList<String> ret = new ArrayList<String>();
		
		List<IConfigurationElement> verrechnungscodes =
			Extensions.getExtensions("ch.elexis.Verrechnungscode");
		
		for (IConfigurationElement code : verrechnungscodes) {
			String codeName = code.getAttribute("name");
			if (codeName != null && codeName.length() > 0)
				ret.add(codeName);
		}
		return ret;
	}
}

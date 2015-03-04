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
package at.medevit.elexis.formattedoutput.config;

import java.io.StringWriter;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import at.medevit.elexis.formattedoutput.Activator;
import at.medevit.elexis.formattedoutput.FormattedOutputException;



public class ConfigFile {
	
	Element root;
	Document doc;
	
	public Element getRootElement(){
		return root;
	}
	
	public ConfigFile(){
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			
			root = doc.createElement("fop");
			root.setAttribute("version", "1.0");
			doc.appendChild(root);
			
			// Target resolution in dpi (dots/pixels per inch) for specifying the target resolution for generated bitmaps, default: 72dpi
//			Element targetResolution = doc.createElement("target-resolution");
//			targetResolution.appendChild(doc.createTextNode("432"));
//			root.appendChild(targetResolution);
			
			Element renderers = doc.createElement("renderers");
			root.appendChild(renderers);
			
			Element rendererPDF = doc.createElement("renderer");
			rendererPDF.setAttribute("mime", "application/pdf");
			renderers.appendChild(rendererPDF);
			
			Element rendererPS = doc.createElement("renderer");
			rendererPS.setAttribute("mime", "application/postscript");
			renderers.appendChild(rendererPS);
			
			Element fontsPDF = doc.createElement("fonts");
			Element fontsPS = doc.createElement("fonts");
			
			IConfigurationElement[] config =
				Platform.getExtensionRegistry().getConfigurationElementsFor(
					Activator.PLUGIN_ID + ".fontRegistry");
			
			for(IConfigurationElement e : config) {			
				Element fontTriple = doc.createElement("font-triplet");
				fontTriple.setAttribute("name",	 e.getAttribute("name"));
				fontTriple.setAttribute("style", e.getAttribute("style"));
				fontTriple.setAttribute("weight", e.getAttribute("weight"));
				
				IConfigurationElement[] fontFilesPdfRenderer = e.getChildren("PdfRendererFontFile");
				for(IConfigurationElement f : fontFilesPdfRenderer) {
					Element fontPDF = doc.createElement("font");
					String metricsUrl = f.getAttribute("metrics-url");
					if(metricsUrl != null) fontPDF.setAttribute("metrics-url", metricsUrl);
					fontPDF.setAttribute("embed-url", f.getAttribute("embed-url"));
					String kerning = f.getAttribute("kerning");
					if (kerning.equalsIgnoreCase("true")) {
						fontPDF.setAttribute("kerning", "yes");
					} else {
						fontPDF.setAttribute("kerning", "no");
					}
					fontPDF.appendChild(fontTriple);
					fontsPDF.appendChild(fontPDF);
				}
				
				IConfigurationElement[] fontFilesPsRenderer = e.getChildren("PsRendererFontFile");
				for(IConfigurationElement g : fontFilesPsRenderer) {
					Element fontPS = doc.createElement("font");
					String metricsUrl = g.getAttribute("metrics-url");
					if(metricsUrl != null) fontPS.setAttribute("metrics-url", metricsUrl);
					fontPS.setAttribute("embed-url", g.getAttribute("embed-url"));
					String kerning = g.getAttribute("kerning");
					if (kerning.equalsIgnoreCase("true")) {
						fontPS.setAttribute("kerning", "yes");
					} else {
						fontPS.setAttribute("kerning", "no");
					}
					fontPS.appendChild(fontTriple.cloneNode(true));
					fontsPS.appendChild(fontPS);
				}
			}
			
			rendererPDF.appendChild(fontsPDF);
			rendererPS.appendChild(fontsPS);		
		} catch (ParserConfigurationException e) {
			Activator.log.log(Level.SEVERE, "Error during XML tranformation.", e);
			throw new FormattedOutputException(e);
		}
//		System.out.println(outputElement());
	}
	
	public String outputElement(){	
		try {
			// set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			// create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			return sw.toString();
		} catch (TransformerException e) {
			Activator.log.log(Level.SEVERE, "EError on XML output transformation.", e);
			throw new FormattedOutputException(e);
		}
	}
	
}

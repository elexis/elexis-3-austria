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
package at.medevit.elexis.formattedoutput.dom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.JAXB;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;

import at.medevit.elexis.formattedoutput.Activator;
import at.medevit.elexis.formattedoutput.FormattedOutputException;
import at.medevit.elexis.formattedoutput.config.ConfigFile;
import at.medevit.elexis.formattedoutput.internal.XSLTUtil;

public class DomToMimeType {
	private static DomToMimeType instance;
	
	private DomToMimeType(){
		
	}
	
	public static DomToMimeType getInstance(){
		if (instance == null)
			instance = new DomToMimeType();
		return instance;
	}
	
	/**
	 * Transform a given DOM Document object into a specific output object
	 * 
	 * @param jaxbObject
	 *            a {@link JAXB} annotated element as source
	 * @param xslt
	 *            the XSLT stylesheet as {@link InputStream} element
	 * @param outputStream
	 *            the {@link OutputStream} to output to
	 * @param outputFormat
	 *            the requested output format {@link MimeConstants}
	 * @param transformerParameters
	 *            key/value parameters to be passed to the transformer, can be <code>null</code>
	 */
	public void transform(Object documentObject, InputStream xslt, OutputStream outputStream,
		String outputFormat, Map<String, String> transformerParameters){
		if (!(documentObject instanceof Document))
			return;
		// configure fopFactory as desired
		FopFactory fopFactory = FopFactory.newInstance();
		Configuration cfg = ConfigurationUtil.toConfiguration(new ConfigFile().getRootElement());
		try {
			fopFactory.setUserConfig(cfg);
		} catch (FOPException e1) {
			Activator.log.log(Level.SEVERE, "Error during XML tranformation.", e1);
			throw new FormattedOutputException(e1);
		}
		
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// configure foUserAgent as desired
		
		// Setup output
		try {
			// Construct fop with desired output format
			Fop fop = fopFactory.newFop(outputFormat, foUserAgent, outputStream);
			// Setup XSLT
			Transformer transformer = XSLTUtil.getTransformerForXSLT(xslt);
			
			if (transformerParameters != null && transformerParameters.keySet().size() > 0) {
				for (String keyParameter : transformerParameters.keySet()) {
					transformer.setParameter(keyParameter, transformerParameters.get(keyParameter));
				}
			}
			
			// Setup input for XSLT transformation
			DOMSource src = new DOMSource((Document) documentObject);
			
			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(fop.getDefaultHandler());
			
			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
		} catch (TransformerException e) {
			Activator.log.log(Level.SEVERE, "Error during XML tranformation.", e);
			throw new FormattedOutputException(e);
		} catch (FOPException e) {
			Activator.log.log(Level.SEVERE, "Error during XML tranformation.", e);
			throw new FormattedOutputException(e);
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				throw new FormattedOutputException(e);
			}
		}
	}
}

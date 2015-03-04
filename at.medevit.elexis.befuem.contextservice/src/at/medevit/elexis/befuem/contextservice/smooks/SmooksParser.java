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
package at.medevit.elexis.befuem.contextservice.smooks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.runtime.Platform;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.container.ExecutionContext;
import org.milyn.csv.CSVReaderConfigurator;
import org.milyn.smooks.edi.EDIReaderConfigurator;
import org.milyn.xml.XmlUtil;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.medevit.elexis.befuem.contextservice.IContextService;
import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding;
import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Format;
import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding.Typ;

public class SmooksParser {
	private static Logger log = LoggerFactory.getLogger(SmooksParser.class);
	
	public static DOMResult getDOMForFinding(AbstractFinding finding) {
		if(finding.getFindingFormat() == Format.EDI) {
			return getDOMForEdiFinding(finding);
		} else if (finding.getFindingFormat() == Format.MAILBOXHL7) {
			return getDOMForMailboxHL7Finding(finding);
		} else {
			log.error("SmooksParser: unknown finding format (" + finding.getFindingFormat() + ")");			
		}
		return null;
	}
	
	private static DOMResult getDOMForMailboxHL7Finding(AbstractFinding finding) {
		DOMResult domResult = null;
        // Instantiate Smooks with the config ...
    	Smooks smooks = null;
		try {
			smooks = new Smooks();

			byte[] header = getMailboxHL7HeaderLines(finding);
			CSVReaderConfigurator headerConf = new CSVReaderConfigurator(
					"linecode,lastname,firstname,gender,birthdate,protocolnr,indate,outdate");
			headerConf.setSeparatorChar(';');
			smooks.setReaderConfig(headerConf);

			// Create an exec context - no profiles ...
			ExecutionContext executionContext = smooks.createExecutionContext();

			DOMResult headerDomResult = new DOMResult();

			smooks.filterSource(executionContext, new StreamSource(
					new InputStreamReader(new ByteArrayInputStream(header))), headerDomResult);

			// done with header parsing reset smooks for content
			smooks = new Smooks();
			byte[] content = getMailboxHL7ContentLines(finding);
			CSVReaderConfigurator contentConf = new CSVReaderConfigurator(
					"linecode,linenr,shorttext,ispathologic,pathologiccode,data,unit,refdata");
			contentConf.setSeparatorChar(';');
			contentConf.setStrict(false);
			smooks.setReaderConfig(contentConf);

			// Create an exec context - no profiles ...
			executionContext = smooks.createExecutionContext();
			
			DOMResult contentDomResult = new DOMResult();
			
			smooks.filterSource(executionContext, new StreamSource(
					new InputStreamReader(new ByteArrayInputStream(content))), contentDomResult);
			
			// create a result containing of both previous results
			// TODO maybe there is a better way than serialize into a string ...
			TransformerFactory tfactory = TransformerFactory.newInstance();
			Transformer tx   = tfactory.newTransformer();
			StringBuilder combine = new StringBuilder();
			// serialize header and skip closing tag
			String headerStr = XmlUtil.serialize(headerDomResult.getNode().getChildNodes(), true);
			int skipIdx = headerStr.lastIndexOf("</csv-set>");
			combine.append(headerStr.substring(0, skipIdx));
			// serialize the content and skip opening tag
			String contentStr = XmlUtil.serialize(contentDomResult.getNode().getChildNodes(), true);
			skipIdx = contentStr.indexOf("\n");
			combine.append(contentStr.substring(skipIdx));
			domResult = new DOMResult();
			StringReader reader = new StringReader(combine.toString());
			tx.transform(new StreamSource(reader),domResult);


		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmooksException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (smooks != null)
				smooks.close();
		}
        
        return domResult;		
//		else if(finding.getFindingFormat() == Format.MAILBOXHL7) {
//			CSVReaderConfigurator conf = new CSVReaderConfigurator("linecode,");
//			smooks.setReaderConfig(new CSVReaderConfigurator(""));
//		}
	}
	
	private static byte[] getMailboxHL7HeaderLines(AbstractFinding finding) throws IOException {
		byte[] content = finding.getContentAsByteArray();
		String contentStr = new String(content, finding.getEncoding());
		
		String[] lines = contentStr.split("\n");
		for(String line : lines) {
			if(line.startsWith("H;")) {
				return line.getBytes();
			}
		}
		return new byte[0];
	}

	private static byte[] getMailboxHL7ContentLines(AbstractFinding finding) throws IOException {
		byte[] content = finding.getContentAsByteArray();
		String contentStr = new String(content, finding.getEncoding());
		
		ArrayList<String> headerLines = new ArrayList<String>();
		String[] lines = contentStr.split("\n");
		for(String line : lines) {
			if(line.startsWith("E;"))
				headerLines.add(line);
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for(String line : headerLines) {
			out.write(line.getBytes());
			// restore \r\n ending
			out.write('\n');
		}
		out.close();
		
		return out.toByteArray();
	}

	private static DOMResult getDOMForEdiFinding(AbstractFinding finding) {
		DOMResult domResult = null;
        // Instantiate Smooks with the config ...
    	Smooks smooks = null;
		try {
			String mappingFilename = getMappingFile(finding.getFindingFormat(), finding.getFindingTyp());
			if(mappingFilename != null) {
				Bundle bundle = Platform.getBundle(IContextService.PLUGIN_ID);
				if (bundle != null) {
					smooks = new Smooks();
					URL url = SmooksParser.class.getResource(mappingFilename);
					smooks.setReaderConfig(new EDIReaderConfigurator(url.toString()));
				} else {
					// this code is NEEDED for the test fragment
					String mappingPath = "../at.medevit.elexis.befuem.contextservice/" + mappingFilename;
					smooks = new Smooks();
					smooks.setReaderConfig(new EDIReaderConfigurator(mappingPath));
				}

				// Create an exec context - no profiles ...
				ExecutionContext executionContext = smooks.createExecutionContext();

				domResult = new DOMResult();
				
				// DEBUG CODE
//				StringResult dbgresult = new StringResult();
//				executionContext.setEventListener(new HtmlReportGenerator("c:\\tmp\\smooks-report.html"));
//				smooks.filterSource(executionContext, new StreamSource(
//						new InputStreamReader(finding.getContentAsStream(), "ISO-8859-1")), dbgresult);
				
				// PRODUCTION CODE
				// Filter the input message to the outputWriter, using the execution
				// context ...
				smooks.filterSource(executionContext, new StreamSource(
						new InputStreamReader(finding.getContentAsStream(), finding.getEncoding())), domResult);
			}
		} catch (IllegalArgumentException | SmooksException | UnsupportedEncodingException e) {
			log.error("SmooksParser: " + e.getMessage());
			return null;
		} finally {
			if (smooks != null)
				smooks.close();
		}
        
        return domResult;
	}
	
	private static String getMappingFile(Format findingFormat, Typ findingTyp) {
		if(findingFormat == Format.EDI) {
			switch(findingTyp) {
			case FINDING:
				return "/rsc/mapping/edi-to-xml-text-finding-mapping.xml";
			case LAB:
				return "/rsc/mapping/edi-to-xml-lab-finding-mapping.xml";
			case FINDING_REQ:
				return "/rsc/mapping/edi-to-xml-text-finding-mapping.xml";
			}
		}
		return null;
	}
}

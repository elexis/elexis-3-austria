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
package at.medevit.elexis.kassen.edivka;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung;
import at.medevit.elexis.kassen.edivka.rechnung.parser.DokumentAusstellerParserUtil;
import at.medevit.elexis.kassen.edivka.rechnung.parser.DokumentEmpfaengerParserUtil;
import at.medevit.elexis.kassen.edivka.rechnung.parser.DokumentKopfInformationParserUtil;
import at.medevit.elexis.kassen.edivka.rechnung.parser.EndsummenRechnungParserUtil;
import at.medevit.elexis.kassen.edivka.rechnung.parser.KlinischeInformationDiagnosenParserUtil;
import at.medevit.elexis.kassen.edivka.rechnung.parser.LeistungenDetailsParserUtil;
import at.medevit.elexis.kassen.edivka.rechnung.parser.LeistungsErbringerParserUtil;
import at.medevit.elexis.kassen.edivka.rechnung.parser.PatientParserUtil;
import at.medevit.elexis.kassen.edivka.rechnung.parser.PrivatVersicherungParserUtil;
import ch.elexis.data.Rechnung;

public class EdivkaService implements IEdivkaService {
	public static final Logger log = Logger.getLogger("at.medevit.elexis.kassen.edivka");
	
	@Override
	public Document getPKVRechnungXMLDocumentForElexisRechnung(Rechnung erechnung){
		PKVRechnung rechnung = getPKVRechnungForElexisRechnung(erechnung, null);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document ret = null;
		try {
			ret = dbf.newDocumentBuilder().newDocument();
			
			JAXBContext jc = JAXBContext.newInstance(rechnung.getClass());
			Marshaller m = jc.createMarshaller();
			m.setProperty("jaxb.encoding", "UTF-8");
			m.marshal(rechnung, ret);
		} catch (ParserConfigurationException e) {
			log.log(Level.SEVERE, "Error during XML tranformation.", e);
		} catch (JAXBException e) {
			log.log(Level.SEVERE, "Error during XML tranformation.", e);
		}
		
// printDomDocumentToConsole(ret);
		
		return ret;
	}
	
	@Override
	public Document getPKVRechnungXMLDocumentForElexisRechnung(Rechnung erechnung,
		String[] dontShowCodeSystem){
		PKVRechnung rechnung = getPKVRechnungForElexisRechnung(erechnung, dontShowCodeSystem);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document ret = null;
		try {
			ret = dbf.newDocumentBuilder().newDocument();
			
			JAXBContext jc = JAXBContext.newInstance(rechnung.getClass());
			Marshaller m = jc.createMarshaller();
			m.setProperty("jaxb.encoding", "UTF-8");
			m.marshal(rechnung, ret);
		} catch (ParserConfigurationException e) {
			log.log(Level.SEVERE, "Error during XML tranformation.", e);
		} catch (JAXBException e) {
			log.log(Level.SEVERE, "Error during XML tranformation.", e);
		}
		
// printDomDocumentToConsole(ret);
		
		return ret;
	}
	
	private PKVRechnung getPKVRechnungForElexisRechnung(Rechnung erechnung,
		String[] dontShowCodeSystem){
		PKVRechnung rechnung = new PKVRechnung();
		
		rechnung.setDokumentKopfInformationen(DokumentKopfInformationParserUtil
			.getDokumentKopfInformation(erechnung));
		
		rechnung.setSchemaIdentifikation("RECHNUNG05.0");
		rechnung.setNachrichtenArt("P17");
		rechnung.setNachrichtenArtBezeichnung("Ambulante Rechnung");
		rechnung.setNachrichtenNummer("1");
		rechnung.setRechnungsNummer(erechnung.getNr());
		rechnung.setRechnungsDatum(erechnung.getDatumRn());
		rechnung.setWaehrung("EUR");
		
		rechnung.setDokumentAussteller(DokumentAusstellerParserUtil
			.getDokumentAussteller(erechnung));
		rechnung.setDokumentEmpfaenger(DokumentEmpfaengerParserUtil
			.getDokumentEmpfaenger(erechnung));
		rechnung.setLeistungsErbringer(LeistungsErbringerParserUtil
			.getLeistungsErbringer(erechnung));
		// nur eine versicherung und die information dazu im privat versicherungs feld ...
		rechnung.setPrivatVersicherung(PrivatVersicherungParserUtil
			.getPrivatVersicherung(erechnung));
// rechnung.setKostentraegerSozialVersicherung(KostentraegerSozialVersicherungParserUtil.getKostentraegerSozialVersicherung(erechnung));
		rechnung.setPatient(PatientParserUtil.getPKVRechnungPatient(erechnung));
		
		rechnung.setKlinischeInformationDiagnosen(KlinischeInformationDiagnosenParserUtil
			.getKlinischeInformationDiagnosen(erechnung));
		
		rechnung.setLeistungenDetails(LeistungenDetailsParserUtil.getLeistungenDetails(erechnung,
			dontShowCodeSystem));
		
		rechnung.setEndsummenRechnung(EndsummenRechnungParserUtil.getEndsummenRechnung(erechnung));
		
		return rechnung;
	}
	
	private void printDomDocumentToConsole(Document doc){
		try {
			System.out
				.println("\n############################# XML DOCUMENT #############################\n");
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			System.out.println(xmlString);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

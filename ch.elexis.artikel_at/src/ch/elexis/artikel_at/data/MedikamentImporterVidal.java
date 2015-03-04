/*******************************************************************************
 * Copyright (c) 2007-2011, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    M. Descher - Adaption (Import works; tested on RpInfo_M08_FED.xml)
 *    			   Imports Substances and Medikamente
 *******************************************************************************/
package ch.elexis.artikel_at.data;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.admin.AccessControlDefaults;
import ch.elexis.artikel_at.PreferenceConstants;
import ch.elexis.artikel_at.preferences.Utilities;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.util.ImporterPage;
import ch.elexis.core.ui.util.SWTHelper;
import ch.elexis.data.Artikel;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.rgw.tools.JdbcLinkException;
import ch.rgw.tools.Money;
import ch.rgw.tools.TimeTool;



public class MedikamentImporterVidal extends ImporterPage {

	private static Logger log = LoggerFactory.getLogger(MedikamentImporterVidal.class);
	
	private static final String ENTRYTYPE_DELETE = "D";
	private static final String ENTRYTYPE_INSERT = "I";
	private static final String ENTRYTYPE_UPDATE = "U";
	private static final String ENTRYTYPE_NAME = "Vidal2";
	
	public MedikamentImporterVidal(){}
	
	@Override
	public Composite createPage(Composite parent){
		Composite ret = new ImporterPage.FileBasedImporter(parent, this);
		ret.setLayoutData(SWTHelper.getFillGridData(1, true, 1, true));
		return ret;
	}
	
	@Override
	public IStatus doImport(IProgressMonitor monitor) throws Exception{
		// TODO: Test cancellation check / what to do in case of cancellation?
		// TODO: Versioning of the Vidal files? Was tested on Full Set only!
		// TODO: Validate file before import?
		// TODO: Zuerst ein Info-Feld anzeigen, dass bestätigt werden kann!
		if (!CoreHub.acl.request(AccessControlDefaults.DELETE_MEDICATION)) {
			SWTHelper.showError("Zugriffsfehler",
				"Sie verfügen nicht über genügend Rechte, benötigt Recht Fixmedikation löschen.");
			return Status.CANCEL_STATUS;
		}
		String importFilename = results[0];
		monitor.beginTask("Importiere Vidal", 6);
		
		SAXBuilder builder = new SAXBuilder();
		monitor.subTask("Lese Datei ein");
		Document doc = builder.build(new File(importFilename));
		monitor.worked(1);
		monitor.subTask("Analysiere Datei");
		Element eRoot = doc.getRootElement();
		monitor.worked(1);
		
		Element RpHeader = eRoot.getChild("RpHeader");
		
		// Wenn Datensatz älter oder gleich gegenwärtig, abbrechen!
		TimeTool currentts = new TimeTool();
		TimeTool newtts = new TimeTool();
		boolean currenttsok =
			currentts.set(CoreHub.globalCfg.get(PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBDATE,
				"20010101"));
		boolean newttsok = newtts.set(RpHeader.getChildText("PubDate"));
		if (!currenttsok || !newttsok) {
			SWTHelper.showError("ERR", "Error parsing date information");
			return Status.CANCEL_STATUS;
		}
		if (newtts.diff(currentts, 0) <= 0) {
			SWTHelper.showError("Import file is older or equal current import",
				"Import file is older or equal current import");
			return Status.CANCEL_STATUS;
		}
		
		// Substanzen
		// Search in DB, if exist leave, else append
		// --------------Exception--------------
		// java.sql.SQLException
		// ERROR: duplicate key value violates unique constraint
		// "ch_elexis_austriamedi_substance_pkey"
		// 23505
		// Kann ignoriert werden
		Element eSubstances = eRoot.getChild("RpSubstRefs");
		if (eSubstances != null) {
			monitor.subTask("Lese Substanzen ein");
			int noOfSubstances = eSubstances.getChildren("SubstRef").size();
			int counter = 1;
			for (Element eSubstance : (List<Element>) eSubstances.getChildren("SubstRef")) {
				String SubstID = eSubstance.getAttributeValue("SubstID");
				String SubstSalt = eSubstance.getAttributeValue("SubstSalt");
				String Name = eSubstance.getTextTrim();
				monitor.subTask("Lese Substanzen ein " + "[" + counter + "/" + noOfSubstances
					+ "]: " + Name);
				try {
					// Should check wether substance already exists
					new Substance(SubstID, Name, SubstSalt);
				} catch (JdbcLinkException e) {
					// INFO: Loading database driver org.postgresql.Driver
					// Fehler bei: INSERT INTO CH_ELEXIS_AUSTRIAMEDI_SUBSTANCE(ID) VALUES ('100')
					// (SQLState: 23505)
					// 10.03.2011 18:01:56 ch.rgw.tools.Log log
					// INFO: Loading database driver org.postgresql.Driver
					// Fehler bei: INSERT INTO CH_ELEXIS_AUSTRIAMEDI_SUBSTANCE(ID) VALUES ('1002')
					// (SQLState: 23505)
					// 10.03.2011 18:01:56 ch.rgw.tools.Log log
					// INFO: Loading database driver org.postgresql.Driver
					// Fehler bei: INSERT INTO CH_ELEXIS_AUSTRIAMEDI_SUBSTANCE(ID) VALUES ('1003')
					// (SQLState: 23505)
					// 10.03.2011 18:01:56 ch.rgw.tools.Log log
					// INFO: Loading database driver org.postgresql.Driver
					System.out.println(e.getMessage());
				}
				counter++;
			}
		}
		monitor.worked(1);
		
		// Medikamente
		Element eMedis = eRoot.getChild("RpData");
		String RpDataType = eMedis.getAttributeValue("DataType"); // F(ull),U(pdate),S(ample)
		if (RpDataType.equalsIgnoreCase("S")) {
			SWTHelper.showError("Sample Data",
				"Dieses Dokument beinhaltet Beispieldaten, Import wird abgebrochen.");
			return Status.CANCEL_STATUS;
		}
		if (eMedis != null) {
			monitor.subTask("Lese Medikamente ein");
			int noOfMedicaments = eMedis.getChildren("RpEntry").size();
			int counter = 1;
			NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
			Money.setLocale(Locale.GERMAN);
			for (Element eMedi : (List<Element>) eMedis.getChildren("RpEntry")) {
				Medikament medi = null;
				
				String actType = eMedi.getAttributeValue("EntryType"); // D(elete)|I(nsert)|U(pdate)
				String mediStatus = eMedi.getAttributeValue("Status"); // H(zugelassen, im Handel,
																		// lieferbar)
				// L(zugelassen, im Handel, nicht lieferbar)
				// Z(zugelassen, nicht im Handel)
				String SName = eMedi.getChildText("SName");
				String PhZNr = eMedi.getChildText("PhZNr");
				log.trace("Import of " + SName);
				
				Money AVP = new Money();
				Number AVPDouble = null;
				try {
					AVPDouble = nf.parse(eMedi.getChildText("AVP").trim());
					double AVPout = AVPDouble.doubleValue() * 100;
					int AVPint = (int) AVPout;
					AVP.addCent(AVPint);
				} catch (ParseException ex) {
					AVP.addCent(eMedi.getChildText("AVP").replaceFirst("[,\\.]", ""));
				}
				Money KVP = new Money();
				Number KVPDouble = null;
				try {
					KVPDouble = nf.parse(eMedi.getChildText("KVP").trim());
					double KVPout = KVPDouble.doubleValue() * 100;
					int KVPint = (int) KVPout;
					KVP.addCent(KVPint);
				} catch (ParseException ex) {
					KVP.addCent(eMedi.getChildText("KVP").replaceFirst("[,\\.]", ""));
				}
				
				String codeClass = eMedi.getChild("SSigns").getAttribute("Box").getValue().trim();
				monitor.subTask("Lese Medikamente ein " + "[" + counter + "/" + noOfMedicaments
					+ "]: " + SName);
				
				Hashtable<String, Object> act = new Hashtable<String, Object>();
				act.put("PhZNr", PhZNr); // Pharmazentralnummer
				act.put("SName", SName); // Kurzname
				act.put("OName", eMedi.getChildText("OName")); // offizieller Produktname
				act.put("Status", mediStatus);
				act.put("DoLC", eMedi.getChildText("DoLC")); // Date of Last Change
				String Storage = eMedi.getChildText("Storage");
				if (Storage != null)
					act.put("Storage", Storage);
				act.put("Quantity", eMedi.getChildText("Quantity"));
				act.put("SUnitDesc", eMedi.getChildText("Unit"));
				act.put("SUnit", eMedi.getChild("Unit").getAttributeValue("SUnit"));
				String EnhUnitDesc = eMedi.getChildText("EnhUnitDesc");
				if (EnhUnitDesc != null)
					act.put("EnhUnitDesc", EnhUnitDesc);
				act.put("ZInh", eMedi.getChildText("ZInh"));
				act.put("ZNr", eMedi.getChildText("ZNr"));
				act.put("KVP", KVP.getCentsAsString());
				act.put("AVP", AVP.getCentsAsString());
				
				act.put("ZNrNum", eMedi.getChild("ZNr").getAttributeValue("ZNrNum"));
				act.put("Remb", eMedi.getChild("SSigns").getAttributeValue("Remb"));
				Hashtable<String, Object> RSigns = new Hashtable<String, Object>();
				for (String val : Medikament.RSIGNS) {
					String cont = eMedi.getChild("RSigns").getAttributeValue(val);
					RSigns.put(val, cont == null ? "0" : cont);
				}
				act.put("RSigns", RSigns);
				Hashtable<String, Object> SSigns = new Hashtable<String, Object>();
				for (String val : Medikament.SSIGNS) {
					String cont = eMedi.getChild("SSigns").getAttributeValue(val);
					SSigns.put(val, cont == null ? "0" : cont);
				}
				act.put("SSigns", SSigns);
				String INDText = eMedi.getChild("SSigns").getChildText("INDText");
				if (INDText != null)
					act.put("INDText", INDText);
				String RuleText = eMedi.getChild("SSigns").getChildText("RuleText");
				if (RuleText != null)
					act.put("RuleText", RuleText);
				String RemarkText = eMedi.getChild("SSigns").getChildText("RemarkText");
				if (RemarkText != null)
					act.put("RemarkText", RemarkText);
				
				// Substances: n/SN/SN/SN/... where n = Anzahl elemente, SN = Substanz
				int noOfSubstances = eMedi.getChild("Substances").getChildren("Substance").size();
				StringBuilder eMediSubstances = new StringBuilder();
				eMediSubstances.append(noOfSubstances);
				for (Element eSubstance : (List<Element>) eMedi.getChild("Substances").getChildren(
					"Substance")) {
					eMediSubstances.append("/").append(eSubstance.getValue().trim());
				}
				act.put("Substances", eMediSubstances.toString());
				
				if (actType.equals(ENTRYTYPE_INSERT)) { // Datensatz ist zum anfuegen markiert
					medi = new Medikament(SName, ENTRYTYPE_NAME, PhZNr);
					
				} else { // Datensatz ist zum loeschen/aktualisieren markiert
					Query<Medikament> qbe = new Query<Medikament>(Medikament.class);
					qbe.clear();
					qbe.add("Typ", "=", ENTRYTYPE_NAME);
					qbe.add("SubID", "=", PhZNr);
					List<Medikament> list = qbe.execute();
					if (list.size() > 0) {
						// TODO: Check selection.. is this right?
						medi = list.get(0);
						if (actType.equals(ENTRYTYPE_DELETE)) {
							// Set blackbox, will be cleaned by cleanMedikamente script, if no more
							// references to it!
							medi.set(Medikament.FLD_CODECLASS, "B");
							continue;
						}
					} else {
						medi = new Medikament(SName, ENTRYTYPE_NAME, PhZNr);
					}
				}
				StringBuilder sb = new StringBuilder();
				sb.append(SName).append(" (").append(eMedi.getChildText("Quantity")).append(")");
				// sb.append(SName).append(" (").append(eMedi.getChildText("Quantity")).append(")").append("/").append(eMedi.getChild("SSigns").getAttributeValue("Remb"));
				
				medi.set(new String[] {
					Artikel.FLD_CODECLASS, Artikel.FLD_VK_PREIS, Artikel.FLD_EK_PREIS,
					Artikel.EIGENNAME
				}, codeClass, AVP.getCentsAsString(), KVP.getCentsAsString(), sb.toString());
				
				Map extInfo = medi.getMap(Medikament.FLD_EXTINFO);
				extInfo.putAll(act);
				medi.setMap(Medikament.FLD_EXTINFO, extInfo);
				
				if ((counter % 128) == 1) {
					if (monitor.isCanceled())
						return Status.CANCEL_STATUS;
					// System.gc();
					PersistentObject.clearCache();
				}
				counter++;
			}
		}
		monitor.subTask("Referenzen auf Pharmazentral-Nr. aktualisieren");
		Utilities.updateMediReferences();
		monitor.worked(1);
		
		monitor.subTask("Nicht mehr benötigte Medikamente löschen");
		Utilities.cleanMedikamente();
		monitor.worked(1);
		
		monitor.subTask("Cache updaten");
		Artikel_AT_Cache.updateCache();
		monitor.worked(1);
		
		// Set Information on current imported set
		monitor.subTask("Konfiguration abschliessen");
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_RPHEADER_FILENAME,
			RpHeader.getChildText("Filename"));
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_RPHEADER_GENERATOR,
			RpHeader.getChildText("Generator"));
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBTITLE,
			RpHeader.getChildText("PubTitle"));
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBTITLE_COUNTRY, RpHeader
			.getChild("PubTitle").getAttributeValue("Country"));
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBAUTHOR,
			RpHeader.getChildText("PubAuthor"));
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBDATE,
			RpHeader.getChildText("PubDate"));
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_RPHEADER_PUBCOPYRIGHT,
			RpHeader.getChildText("PubCopyright"));
		
		monitor.done();
		return Status.OK_STATUS;
	}
	
	@Override
	public String getDescription(){
		return "Importiere Medikamente von vidal.at";
	}
	
	@Override
	public String getTitle(){
		return "Medikamente (vidal v2)";
	}
	
}

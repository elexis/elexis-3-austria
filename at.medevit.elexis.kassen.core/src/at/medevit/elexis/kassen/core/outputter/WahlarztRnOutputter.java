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
package at.medevit.elexis.kassen.core.outputter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.statushandlers.StatusManager;
import org.w3c.dom.Document;

import at.medevit.elexis.formattedoutput.IFormattedOutput;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory.ObjectType;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory.OutputType;
import at.medevit.elexis.kassen.core.Activator;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.interfaces.IRnOutputter;
import ch.elexis.data.Fall;
import ch.elexis.data.Rechnung;
import ch.rgw.tools.Result;

public class WahlarztRnOutputter implements IRnOutputter {

	public static String OS;
	private static DocFlavor psInFormat = null;
	
	static {
		OS = System.getProperty("os.name").toLowerCase();
		if(isMac()) psInFormat = DocFlavor.BYTE_ARRAY.POSTSCRIPT;
		if(isWindows()) psInFormat = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		if(isUnix()) psInFormat = DocFlavor.BYTE_ARRAY.POSTSCRIPT; //TODO: Not checked
	}
	
	public static boolean isWindows(){ return (OS.indexOf( "win" ) >= 0); }
	public static boolean isMac(){ return (OS.indexOf( "mac" ) >= 0); }
	public static boolean isUnix(){ return (OS.indexOf( "nix") >=0 || OS.indexOf( "nux") >=0); }
	
	@Override
	public String getDescription() {
		return "Standard Wahlarzt Honorarnote auf A4 Drucker.";
	}

	@Override
	public Result<Rechnung> doOutput(TYPE type, Collection<Rechnung> rechnungen,
			Properties props) {
		Result<Rechnung> ret = new Result<Rechnung>();
		EdivkaConsumer edivka = new EdivkaConsumer();
		
		for (Rechnung rechnung : rechnungen) {
			try {
				Document xmlRechnung = edivka.getRechnungForElexisRechnung(rechnung);
				
//				printDocument(xmlRechnung, System.out);
				
				ByteArrayOutputStream psOutput = new ByteArrayOutputStream();
	
				URL xslt = WahlarztRnOutputter.class.getResource("/rsc/xslt/edivka2stdrechnung.xslt");
	
				IFormattedOutput output = FormattedOutputConsumer
						.getOutputImplementations(ObjectType.DOM, OutputType.PS);

				output.transform(xmlRechnung, xslt.openStream(), psOutput);
				
				doPrint(psOutput);
			} catch (IOException e) {
				ret.add(Result.SEVERITY.ERROR, -1, 
						e.getMessage(), rechnung, false);
				Status status =
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
						"Drucken der Rechnung Nummer (" + rechnung.getNr() + ") fehlgeschlagen.", e);
				StatusManager.getManager().handle(status, StatusManager.BLOCK);
			} catch (PrintException e) {
				ret.add(Result.SEVERITY.ERROR, -1, 
						e.getMessage(), rechnung, false);
				Status status =
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
							"Drucken der Rechnung Nummer (" + rechnung.getNr() + ") fehlgeschlagen.", e);
				StatusManager.getManager().handle(status, StatusManager.BLOCK);
			}
			ret.add(Result.SEVERITY.OK, 0,
					"Gedruckt", rechnung, false);
		}
		return ret;
	}

	private void doPrint(ByteArrayOutputStream psInput) throws PrintException {
		
		PrintService printer = getConfiguredService();
		
		Doc psDoc = new SimpleDoc(psInput.toByteArray(), psInFormat, null);
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		
		if(printer == null) {
			Status status =
				new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
					"No Printer found.");
			StatusManager.getManager().handle(status, StatusManager.BLOCK);
			return;
		}
	
		DocPrintJob job = printer.createPrintJob();
		PrintJobListenerImpl listener = new PrintJobListenerImpl();
		job.addPrintJobListener(listener);
		job.print(psDoc, aset);
	}
	
	private PrintService getConfiguredService() {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(psInFormat, null);
		String printerName = CoreHub.localCfg.get("Drucker/A4/Name", "");
		
		for(PrintService printService : printServices) {
			if(printService.getName().equalsIgnoreCase(printerName))
				return printService;
		}
		return PrintServiceLookup.lookupDefaultPrintService();
	}
	
	@Override
	public boolean canStorno(Rechnung rn) {
		return false;
	}

	@Override
	public boolean canBill(Fall fall) {
		return true;
	}

	public Control createSettingsControl(Object parent) {
		return null;
	}

	@Override
	public void saveComposite() {
	}
	
	private class PrintJobListenerImpl implements PrintJobListener {
		@Override
		public void printDataTransferCompleted(PrintJobEvent pje) {
		}
		@Override
		public void printJobCompleted(PrintJobEvent pje) {
		}
		@Override
		public void printJobFailed(PrintJobEvent pje) {
			Status status = new Status(IStatus.OK, Activator.PLUGIN_ID,
				"Druckauftrag fehlgeschlagen.");
			StatusManager.getManager().handle(status, StatusManager.BLOCK);
		}
		@Override
		public void printJobCanceled(PrintJobEvent pje) {
		}
		@Override
		public void printJobNoMoreEvents(PrintJobEvent pje) {
		}
		@Override
		public void printJobRequiresAttention(PrintJobEvent pje) {
		}
	}
	
	// print the xml for debugging
	@SuppressWarnings("unused")
	private static void printDocument(Document doc, OutputStream out) {
	    try {
			TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	
		    transformer.transform(new DOMSource(doc), 
		         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

}

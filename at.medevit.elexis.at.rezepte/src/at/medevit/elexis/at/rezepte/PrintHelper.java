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
package at.medevit.elexis.at.rezepte;

import java.io.IOException;
import java.util.LinkedList;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.medevit.elexis.at.rezepte.formattedoutputconsumer.DocumentCreator;
import at.medevit.elexis.at.rezepte.model.RezeptAT;
import at.medevit.elexis.at.rezepte.ui.RezeptausdruckPreferencePage;
import ch.elexis.core.data.activator.CoreHub;

public class PrintHelper {
	
	private static Logger log = LoggerFactory.getLogger(PrintHelper.class);
	
	public static int REZEPT_PRINTER = 1;
	public static int EINNAHMELISTE_PRINTER = 2;
	
	public static String OS;
	private static DocFlavor psInFormat = null;
	
	static {
		OS = System.getProperty("os.name").toLowerCase();
		if (isMac())
			psInFormat = DocFlavor.BYTE_ARRAY.POSTSCRIPT;
		if (isWindows())
			psInFormat = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		if (isUnix())
			psInFormat = DocFlavor.BYTE_ARRAY.POSTSCRIPT; //TODO: Not checked
	}
	
	public static boolean isWindows(){
		return (OS.indexOf("win") >= 0);
	}
	
	public static boolean isMac(){
		return (OS.indexOf("mac") >= 0);
	}
	
	public static boolean isUnix(){
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0);
	}
	
	/**
	 * @author MEDEVIT OG
	 * @return All system available print services.
	 */
	public static PrintService[] getPrintServices(){
		return PrintServiceLookup.lookupPrintServices(psInFormat, null);
	}
	
	/**
	 * 
	 * @return Array containing all available {@link PrintService} on the system.
	 */
	public static String[] getPrinterList(){
		if (psInFormat != null) {
			PrintService[] printServices = getPrintServices();
			LinkedList<String> printerList = new LinkedList<String>();
			for (PrintService printService : printServices) {
				printerList.add(printService.getName());
			}
			
			return printerList.toArray(new String[0]);
		}
		return new String[0];
	}
	
	/**
	 * @author MEDEVIT OG
	 * @return {@link PrintService} currently configured by name if found, else returns default
	 *         PrintService, may return null
	 */
	public static PrintService getConfiguredService(int printer){
		PrintService[] printServices = getPrintServices();
		String printerName = "undefined";
		if (printer == REZEPT_PRINTER) {
			printerName = CoreHub.localCfg.get(RezeptausdruckPreferencePage.LRP, "undefined");
			log.debug("Configured printerName for "+printer+": "+printerName);
		}
		
		PrintService ps = null;
		
		for (PrintService printService : printServices) {
			if (printerName.equalsIgnoreCase(printService.getName())) {
				ps = printService;
				continue;
			}
		}
		
		if (ps == null)
			PrintServiceLookup.lookupDefaultPrintService();
		if(ps!=null) {
			log.debug("Selected PrintService for " + printer + ": " + ps.getName());
		} else {
			log.error("No PrintService found!");
		}

		return ps;
	}
	
	/**
	 * Print a rezept to the selected Rezept Printer
	 * 
	 * @param template
	 * @param rezept
	 */
	public static void printRezeptToRezeptPrinterInPostscript(String template, RezeptAT rezept){
		try {
			PrintService printer = PrintHelper.getConfiguredService(REZEPT_PRINTER);
			DocFlavor psInFormat = DocFlavor.BYTE_ARRAY.POSTSCRIPT;
			Doc myDoc = new SimpleDoc(DocumentCreator.createPS(template, rezept).toByteArray(),
				psInFormat, null);
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
			
			if (printer != null) {
				DocPrintJob job = printer.createPrintJob();
				try {
					PrintListener pl = new PrintListener();
					job.addPrintJobListener(pl);
					job.addPrintJobAttributeListener(pl, job.getAttributes());
					job.print(myDoc, aset);
					job.removePrintJobAttributeListener(pl);
					job.removePrintJobAttributeListener(pl);
					// TODO: If printing handled correct add to new OutputLog(rp, this);
				} catch (PrintException pe) {
					Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID,
						pe.getLocalizedMessage(), pe);
					StatusManager.getManager().handle(status, StatusManager.SHOW);
				}
			} else {
				System.out.println("no printer services found");
			}
			
		} catch (IOException ex) {
			Status status =
				new Status(IStatus.WARNING, Activator.PLUGIN_ID, ex.getLocalizedMessage(), ex);
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}
	}
	
	public static void main(String[] args){
		System.out.println(getPrinterList()[0]);
	}
	
}

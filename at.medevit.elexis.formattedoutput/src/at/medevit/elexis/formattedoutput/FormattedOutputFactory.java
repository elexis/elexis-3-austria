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
package at.medevit.elexis.formattedoutput;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationUtil;
import org.apache.fop.Version;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.tools.fontlist.FontListGenerator;

import at.medevit.elexis.formattedoutput.config.ConfigFile;
import at.medevit.elexis.formattedoutput.domtopdf.DomToPdf;
import at.medevit.elexis.formattedoutput.domtopng.DomToPng;
import at.medevit.elexis.formattedoutput.domtops.DomToPs;
import at.medevit.elexis.formattedoutput.jaxbtopcl.JaxbToPcl;
import at.medevit.elexis.formattedoutput.jaxbtopdf.JaxbToPdf;
import at.medevit.elexis.formattedoutput.jaxbtopng.JaxbToPng;
import at.medevit.elexis.formattedoutput.jaxtops.JaxbToPs;

public class FormattedOutputFactory implements IFormattedOutputFactory {
	
	private static FopFactory fop = null;
	
	@Override
	public IFormattedOutput getFormattedOutputImplementation(ObjectType objectType,
		OutputType outputType){
		if (objectType == ObjectType.JAXB) {
			switch (outputType) {
			case PCL:
				return JaxbToPcl.getInstance();
			case PDF:
				return JaxbToPdf.getInstance();
			case PS:
				return JaxbToPs.getInstance();
			case PNG:
				return JaxbToPng.getInstance();
			default:
				break;
			}
		} else if (objectType == ObjectType.DOM) {
			switch (outputType) {
			case PDF:
				return DomToPdf.getInstance();
			case PS:
				return DomToPs.getInstance();
			case PNG:
				return DomToPng.getInstance();
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param mimeType
	 * @return The fonts available for FOP processing.
	 */
	public static String[] getRegisteredFonts(String mimeType){
		try {
			initialize();
			
			LinkedList<String> fontFamiliesList = new LinkedList<String>();
			
			FontListGenerator listGenerator = new FontListGenerator();
			SortedMap fontFamilies = listGenerator.listFonts(fop, mimeType, null);
			Iterator iter = fontFamilies.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				fontFamiliesList.add((String) entry.getKey());
			}
			return fontFamiliesList.toArray(new String[0]);
		} catch (FOPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String[] {
			""
		};
	}
	
	public static void initialize(){
		System.out.println("Initializing FOP" + Version.getVersion());
		try {
			fop = FopFactory.newInstance();
			Configuration cfg =
				ConfigurationUtil.toConfiguration(new ConfigFile().getRootElement());
			System.out.println("FOP Configuration: " + ConfigurationUtil.toString(cfg));
			fop.setUserConfig(cfg);
		} catch (FOPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
// public static void getRegisteredFontsa() {
// try {
// DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
// Configuration cfg = cfgBuilder.buildFromFile(new File("/Users/marco/myFOPcfg.xml"));
//
// FopFactory fop = FopFactory.newInstance();
// fop.setUserConfig(cfg);
//
//
// FontManager fopFont = fop.getFontManager();
// FontListGenerator listGenerator = new FontListGenerator();
// SortedMap fontFamilies = listGenerator.listFonts(fop, MimeConstants.MIME_PDF, null);
// System.out.println("FF: "+fontFamilies.size());
// try {
// writeToConsole(fontFamilies);
// } catch (TransformerConfigurationException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
//
// System.out.println(fop.getUserConfig());
//
//
// FontInfo fi = new FontInfo();
// // PDF Fonts
//
// FontSetup.setup(fi);
//
// Map fonts = fi.getFonts();
// System.out.println(fonts.size());
// System.out.println(fonts);
//
//
//
// } catch (FOPException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (ConfigurationException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (SAXException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// } catch (IOException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
// ;
// }
	
// private static void writeToConsole(SortedMap fontFamilies)
// throws TransformerConfigurationException, SAXException, IOException {
// Iterator iter = fontFamilies.entrySet().iterator();
// while (iter.hasNext()) {
// Map.Entry entry = (Map.Entry)iter.next();
// String firstFamilyName = (String)entry.getKey();
// System.out.println(firstFamilyName + ":");
// List list = (List)entry.getValue();
// Iterator fonts = list.iterator();
// while (fonts.hasNext()) {
// FontSpec f = (FontSpec)fonts.next();
// System.out.println("  " + f.getKey() + " " + f.getFamilyNames());
// Iterator triplets = f.getTriplets().iterator();
// while (triplets.hasNext()) {
// FontTriplet triplet = (FontTriplet)triplets.next();
// System.out.println("    " + triplet.toString());
// }
// }
// }
// }
	
}

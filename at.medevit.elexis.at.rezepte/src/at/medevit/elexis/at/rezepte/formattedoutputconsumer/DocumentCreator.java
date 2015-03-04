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
package at.medevit.elexis.at.rezepte.formattedoutputconsumer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import at.medevit.elexis.at.rezepte.Activator;
import at.medevit.elexis.at.rezepte.model.RezeptAT;
import at.medevit.elexis.formattedoutput.IFormattedOutput;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory.ObjectType;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory.OutputType;

public class DocumentCreator {
	/**
	 * @param template
	 *            XSLT Template file to create output
	 * @param backgroundImage
	 *            A background file to show on the rendered document
	 * @param rezeptOutput
	 *            The RezeptAT instance to use as input for the document creation
	 * @return {@link ByteArrayOutputStream} containing the <b>preview PDF file</b>
	 * @throws IOException
	 */
	public static ByteArrayOutputStream createPdfFile(String template, String backgroundImage,
		RezeptAT rezeptOutput) throws IOException{
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		URL xslt = null;
		
		// TODO: Does not throw IOException when wrong file is given
		xslt = bundle.getEntry(template);
		
		// Set background image
		URL image = null;
		image = bundle.getEntry("rsc/vorlagen/VGKKWA.jpg");
		if (image != null)
			rezeptOutput.setBgimg(FileLocator.toFileURL(image).toString());
		
		IFormattedOutput output =
			FOPConsumer.getOutputImplementations(ObjectType.JAXB, OutputType.PDF);
		output.transform(rezeptOutput, xslt.openStream(), out);
		
		return out;
	}
	
	/**
	 * @param template
	 *            XSLT Template file to create output
	 * @param rezeptOutput
	 *            The RezeptAT instance to use as input for the document creation
	 * @return {@link ByteArrayOutputStream} containing the <b>Postscript output file for the
	 *         printer</b>
	 * @throws IOException
	 */
	public static ByteArrayOutputStream createPS(String template, RezeptAT rezeptOutput)
		throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		URL xslt = null;
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		// TODO: Does not throw IOException when wrong file is given
		xslt = bundle.getEntry(template);
		
		try {
			JAXBContext.newInstance(RezeptAT.class).createMarshaller()
				.marshal(rezeptOutput, System.out);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Unset background image
		rezeptOutput.setBgimg("");
		
		IFormattedOutput output =
			FOPConsumer.getOutputImplementations(ObjectType.JAXB, OutputType.PS);
		output.transform(rezeptOutput, xslt.openStream(), out);
		
		return out;
	}
}

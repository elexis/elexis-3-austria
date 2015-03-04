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

import at.medevit.elexis.formattedoutput.IFormattedOutput;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory.ObjectType;
import at.medevit.elexis.formattedoutput.IFormattedOutputFactory.OutputType;

public class FOPConsumer {
private static IFormattedOutputFactory outputFactory;
	
	public static IFormattedOutput getOutputImplementations(ObjectType objectType, OutputType outputType) {
		return outputFactory.getFormattedOutputImplementation(objectType, outputType);
	}
	
	// Method will be used by DS to set the service
	public synchronized void setFormattedOutputServiceFactory(IFormattedOutputFactory factory) {
		outputFactory = factory;
		System.out.println(outputFactory.getClass().getName());
	}

	// Method will be used by DS to unset the service
	public synchronized void unsetFormattedOutputServiceFactory(IFormattedOutputFactory factory) {
		if (outputFactory == factory) {
			outputFactory = null;
		}
	}
}

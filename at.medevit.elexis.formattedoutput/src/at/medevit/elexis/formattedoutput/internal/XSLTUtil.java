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
package at.medevit.elexis.formattedoutput.internal;

import java.io.InputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.xml.utils.DefaultErrorHandler;

public class XSLTUtil {
	public static Transformer getTransformerForXSLT(InputStream xslt) throws TransformerConfigurationException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer ret = factory.newTransformer(new StreamSource(
				xslt));
		ret.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		ret.setErrorListener(new DefaultErrorHandler() {
			@Override
			public void error(TransformerException exception)
					throws TransformerException {
				super.error(exception);
				throw exception;
			}

			@Override
			public void fatalError(TransformerException exception)
					throws TransformerException {
				super.error(exception);
				throw exception;
			}
		});
		return ret;
	}
}

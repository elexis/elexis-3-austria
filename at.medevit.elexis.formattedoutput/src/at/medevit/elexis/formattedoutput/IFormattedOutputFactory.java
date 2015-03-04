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

/**
 * Factory for producing new {@link IFormattedOutput} implementations.
 * @author thomas
 *
 */
public interface IFormattedOutputFactory {
	/**
	 * Supported input Object types for the transformation.
	 * @author thomas
	 */
	enum ObjectType { JAXB, DOM }
	/**
	 * Supported output formats for the transformation.
	 * @author thomas
	 */
	enum OutputType { PDF, PCL, PS, PNG }
	
	/**
	 * Returns a {@link IFormattedOutput} implementation depending on the {@link ObjectType} and
	 * {@link OutputType} parameters
	 * @param objectType
	 * @param outputType
	 * @return implementation
	 */
	IFormattedOutput getFormattedOutputImplementation(ObjectType objectType, OutputType outputType);
}

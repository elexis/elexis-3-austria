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
package at.medevit.elexis.kassen.core.model.expressions.parser;

public class ExpressionDefinition {
	String name;
	String parameters;
	
	ExpressionDefinition[] children;

	public ExpressionDefinition(String name, String parameters) {
		this.name = name;
		this.parameters = parameters;
	}
	
	public String getParamters() {
		return parameters;
	}
	
	public String getName() {
		return name;
	}
	
	public ExpressionDefinition[] getChildren() {
		return children;
	}
}

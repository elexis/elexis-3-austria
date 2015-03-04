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
package at.medevit.elexis.kassen.core.model.expressions;

import java.text.ParseException;

import org.eclipse.core.expressions.Expression;

import at.medevit.elexis.kassen.core.model.KassenLeistung;

public interface IKassenExpressionFactory {
	public Expression getExpressionForString(String definition, Class<? extends KassenLeistung> clazz) throws ParseException;
	public int getWeightForString(String definition) throws ParseException;
}

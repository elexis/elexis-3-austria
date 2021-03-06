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
package at.medevit.elexis.kassen.core.model.expressions.internal;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.expressions.AbstractKassenExpression;

public class DummyExpression extends AbstractKassenExpression {

	EvaluationResult result;

	public DummyExpression(EvaluationResult result) throws CoreException {
		this.result = result;
	}

	@Override
	public EvaluationResult evaluate(IEvaluationContext context)
			throws CoreException {
		return result;
	}
}

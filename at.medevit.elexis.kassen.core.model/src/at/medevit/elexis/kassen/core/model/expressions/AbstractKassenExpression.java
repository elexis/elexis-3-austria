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

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;

public abstract class AbstractKassenExpression extends Expression {

	protected EvaluationResult lastResult = null;
	
	/**
	 * Computes the information for the given expression tree.
	 * <p>
	 * This is a convenience method for collecting the expression information
	 * using {@link AbstractKassenExpression#collectKassenExpressionInfo(KassenExpressionInfo)}.
	 * </p>
	 *
	 * @return the expression information
	 *
	 * @since 3.2
	 */
	public final KassenExpressionInfo computeKassenExpressionInfo() {
		KassenExpressionInfo result = new KassenExpressionInfo();
		collectKassenExpressionInfo(result);
		return result;
	}
	
	/**
	 * Collects information about the expression tree. For example
	 * the reason for the last negative result.
	 *
	 * @param info the expression information object used
	 *  to collect the information
	 *
	 * @since 3.2
	 */
	public void collectKassenExpressionInfo(KassenExpressionInfo info) {
		if(lastResult != null && 
				(lastResult == EvaluationResult.FALSE || lastResult == EvaluationResult.NOT_LOADED))
			info.addNegativeExpression(this);
	}
}

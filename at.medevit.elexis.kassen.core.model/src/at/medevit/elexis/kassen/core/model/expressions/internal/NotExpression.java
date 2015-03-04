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
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.expressions.AbstractKassenExpression;
import at.medevit.elexis.kassen.core.model.expressions.KassenExpressionInfo;

public class NotExpression extends AbstractKassenExpression {

	private AbstractKassenExpression fExpression;

	public NotExpression(AbstractKassenExpression expression) {
		Assert.isNotNull(expression);
		fExpression= expression;
	}

	public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
		EvaluationResult ret = fExpression.evaluate(context).not();
		if(ret == EvaluationResult.TRUE) {
			lastResult = ret;
			return ret;
		} else {
			lastResult = ret;
			return ret;
		}
	}

	@Override
	public void collectKassenExpressionInfo(KassenExpressionInfo info) {
		if (fExpression == null)
			return;
		if(lastResult != null &&
				(lastResult == EvaluationResult.FALSE || lastResult == EvaluationResult.NOT_LOADED)) {
			info.addNegativeExpression(this);
			// add reason directly afterwards
			info.addNegativeExpression(fExpression);
		}
	}
	
	public boolean equals(final Object object) {
		if (!(object instanceof NotExpression))
			return false;

		final NotExpression that= (NotExpression)object;
		return this.fExpression.equals(that.fExpression);
	}
}

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.expressions.AbstractKassenExpression;
import at.medevit.elexis.kassen.core.model.expressions.KassenExpressionInfo;

public abstract class CompositeExpression extends AbstractKassenExpression {

	private static final AbstractKassenExpression[] EMPTY_ARRAY = new AbstractKassenExpression[0];

	/**
	 * The seed for the hash code for all composite expressions.
	 */
	private static final int HASH_INITIAL= CompositeExpression.class.getName().hashCode();

	protected List<AbstractKassenExpression> fExpressions;

	public void add(AbstractKassenExpression expression) {
		if (fExpressions == null)
			fExpressions= new ArrayList<AbstractKassenExpression>(2);
		fExpressions.add(expression);
	}

	public AbstractKassenExpression[] getChildren() {
		if (fExpressions == null)
			return EMPTY_ARRAY;
		return (AbstractKassenExpression[])fExpressions.toArray(new AbstractKassenExpression[fExpressions.size()]);
	}

	protected EvaluationResult evaluateAnd(IEvaluationContext scope) throws CoreException {
		if (fExpressions == null)
			return EvaluationResult.TRUE;
		EvaluationResult result= EvaluationResult.TRUE;
		for (Iterator<AbstractKassenExpression> iter= fExpressions.iterator(); iter.hasNext();) {
			AbstractKassenExpression expression= iter.next();
			result= result.and(expression.evaluate(scope));
			// keep iterating even if we have a not loaded found. It can be
			// that we find a false which will result in a better result.
			if (result == EvaluationResult.FALSE)
				return result;
		}
		return result;
	}

	protected EvaluationResult evaluateOr(IEvaluationContext scope) throws CoreException {
		if (fExpressions == null)
			return EvaluationResult.TRUE;
		EvaluationResult result= EvaluationResult.FALSE;
		for (Iterator<AbstractKassenExpression> iter= fExpressions.iterator(); iter.hasNext();) {
			AbstractKassenExpression expression= iter.next();
			result= result.or(expression.evaluate(scope));
			if (result == EvaluationResult.TRUE)
				return result;
		}
		return result;
	}

	public void collectExpressionInfo(ExpressionInfo info) {
		if (fExpressions == null)
			return;
		for (Iterator<AbstractKassenExpression> iter= fExpressions.iterator(); iter.hasNext();) {
			AbstractKassenExpression expression= iter.next();
			expression.collectExpressionInfo(info);
		}
	}

	protected int computeHashCode() {
		return HASH_INITIAL * HASH_FACTOR + hashCode(fExpressions);
	}

	@Override
	public void collectKassenExpressionInfo(KassenExpressionInfo info) {
		if (fExpressions == null)
			return;
		for (Iterator<AbstractKassenExpression> iter= fExpressions.iterator(); iter.hasNext();) {
			AbstractKassenExpression expression = iter.next();
			expression.collectKassenExpressionInfo(info);
		}
	}
}

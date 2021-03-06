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

public class OrExpression extends CompositeExpression {
	public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
		return evaluateOr(context);
	}

	public boolean equals(final Object object) {
		if (!(object instanceof OrExpression))
			return false;

		final OrExpression that= (OrExpression)object;
		return equals(this.fExpressions, that.fExpressions);
	}
}

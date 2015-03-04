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

import static org.junit.Assert.assertTrue;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

public class AndExpressionTest {
	@Test
	public void testAnd() throws CoreException {		
		AndExpression exp = new AndExpression();
		exp.add(new DummyExpression(EvaluationResult.TRUE));
		exp.add(new DummyExpression(EvaluationResult.TRUE));
		
		EvaluationResult result = exp.evaluate(new EvaluationContext(null, ""));
		assertTrue(result == EvaluationResult.TRUE);

		exp.add(new DummyExpression(EvaluationResult.FALSE));
		result = exp.evaluate(new EvaluationContext(null, ""));
		assertTrue(result == EvaluationResult.FALSE);
	}
}

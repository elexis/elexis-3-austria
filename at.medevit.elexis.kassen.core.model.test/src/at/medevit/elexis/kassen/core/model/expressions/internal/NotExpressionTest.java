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

public class NotExpressionTest {
	@Test
	public void testNot() throws CoreException {
		NotExpression exp = new NotExpression(new DummyExpression(EvaluationResult.FALSE));
		
		EvaluationResult result = exp.evaluate(new EvaluationContext(null, ""));
		assertTrue(result == EvaluationResult.TRUE);
		// successful test
		exp = new NotExpression(new DummyExpression(EvaluationResult.TRUE));
		result = exp.evaluate(new EvaluationContext(null, ""));
		assertTrue(result == EvaluationResult.FALSE);
	}
}

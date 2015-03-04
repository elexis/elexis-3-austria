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
import org.eclipse.core.expressions.IVariableResolver;
import org.eclipse.core.runtime.CoreException;
import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.core.model.expressions.KonsultationResolver;
import at.medevit.elexis.kassen.core.model.expressions.test.TestData;
import at.medevit.elexis.kassen.core.model.expressions.test.TestData.TestSzenario;
import at.medevit.elexis.kassen.test.shared.SharedTestData;

public class KonsultationIncludesPositionExpressionTest {
	private static TestSzenario data;

	@BeforeClass
	public static void runBeforeClass() {
		data = TestData.getTestSzenarioInstance();
	}  
	
	@Test
	public void testKonsultationIncludesPosition() throws CoreException {
		SharedTestData.importTestLeistungen();
		KonsultationIncludesVerrechenbarExpression exp = new KonsultationIncludesVerrechenbarExpression("A1");
		
		IVariableResolver[] resolvers = {new KonsultationResolver(data.getKonsultationen().get(0))};
		EvaluationContext context = new EvaluationContext(null, "", resolvers);
		
		EvaluationResult result = exp.evaluate(context);
		assertTrue(result == EvaluationResult.TRUE);
	}
}

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

import java.text.ParseException;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.IVariableResolver;
import org.eclipse.core.runtime.CoreException;
import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.core.model.DateRange;
import at.medevit.elexis.kassen.core.model.PointsRange;
import at.medevit.elexis.kassen.core.model.expressions.MandantResolver;
import at.medevit.elexis.kassen.core.model.expressions.VerrechenbarResolver;
import at.medevit.elexis.kassen.core.model.expressions.test.TestData;
import at.medevit.elexis.kassen.core.model.expressions.test.TestData.TestSzenario;
import at.medevit.elexis.kassen.test.shared.KassenLeistungImpl;
import at.medevit.elexis.kassen.test.shared.SharedTestData;

public class InPointRangeExpressionTest {
	private static TestSzenario data;

	@BeforeClass
	public static void runBeforeClass() {
		data = TestData.getTestSzenarioInstance();
	}  
	
	@Test
	public void testInPointRange() throws CoreException, ParseException {
		SharedTestData.importTestLeistungen();
		PointsRange pr = new PointsRange("0", "10");
		DateRange dr = DateRange.getAbsolutRange("MONTH");
		InPointRangeExpression exp = new InPointRangeExpression(pr, dr);
		
		// setup the context for evaluation
		IVariableResolver[] resolvers = {
				new MandantResolver(data.getMandanten().get(0)),
				new VerrechenbarResolver(null, KassenLeistungImpl.class)};
		EvaluationContext context = new EvaluationContext(null, "", resolvers);
		
		EvaluationResult result = exp.evaluate(context);
		assertTrue(result == EvaluationResult.FALSE);

		pr = new PointsRange("11", "100");
		dr = DateRange.getAbsolutRange("QUARTER");
		exp = new InPointRangeExpression(pr, dr);

		result = exp.evaluate(context);
		assertTrue(result == EvaluationResult.TRUE);
	}
}

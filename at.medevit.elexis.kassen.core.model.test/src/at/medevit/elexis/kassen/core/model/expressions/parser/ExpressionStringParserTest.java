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
package at.medevit.elexis.kassen.core.model.expressions.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

import org.junit.Test;

import at.medevit.elexis.kassen.core.model.expressions.test.TestData;

public class ExpressionStringParserTest {
	@Test
	public void testGetExpressions() throws ParseException {
		ExpressionStringParser parser = new ExpressionStringParser(TestData.parserTestExpression);
		ExpressionDefinition[] expressions = parser.getExpressions();
		assertNotNull(expressions);
		assertEquals(1, expressions.length);
		
		parser = new ExpressionStringParser(expressions[0].getParamters());
		expressions = parser.getExpressions();
		assertNotNull(expressions);
		assertEquals(3, expressions.length);
		// now test the 3 expressions for further expressions
		parser = new ExpressionStringParser(expressions[0].getParamters());
		ExpressionDefinition[] subexpressions = parser.getExpressions();
		assertNull(subexpressions);
		
		parser = new ExpressionStringParser(expressions[1].getParamters());
		subexpressions = parser.getExpressions();
		assertNull(subexpressions);
	}
	
	/**
	 * Test parsing following string ...
	 * "expression0(expression1(parameter-parameter) and expression2(parameter)) and expression3(parameter,parameter) or expression4(parameter)"
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testGetExpressionTree() throws ParseException {
		ExpressionStringParser parser = new ExpressionStringParser(TestData.parserTestExpressionTree);
		ExpressionDefinition[] expressions = parser.getExpressionTree();
		assertNotNull(expressions);
		assertEquals(5, expressions.length);
		
		assertEquals(3, expressions[0].children.length);
		assertTrue(expressions[1].children == null);		
		assertTrue(expressions[2].children == null);
		assertTrue(expressions[3].children == null);
		assertTrue(expressions[4].children == null);
		assertTrue(expressions[0].children[0].children == null);
		assertTrue(expressions[0].children[1].children == null);
		assertTrue(expressions[0].children[2].children == null);
	}
	
	/**
	 * Test parsing following string ...
	 * "expression0(expression1(parameter-parameter) and expression2(parameter)) and (expression3(parameter,parameter) or expression4(parameter))"
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testGetExpressionTreeComposite() throws ParseException {
		ExpressionStringParser parser = new ExpressionStringParser(TestData.parserTestExpressionCompositeTree);
		ExpressionDefinition[] expressions = parser.getExpressionTree();
		assertNotNull(expressions);
		assertEquals(2, expressions.length);
		
		assertEquals(3, expressions[0].children.length);
		assertEquals(3, expressions[1].children.length);
		assertTrue(expressions[0].children[0].children == null);
		assertTrue(expressions[0].children[1].children == null);
		assertTrue(expressions[0].children[2].children == null);
		assertTrue(expressions[1].children[0].children == null);
		assertTrue(expressions[1].children[1].children == null);
		assertTrue(expressions[1].children[2].children == null);	}
}

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
package at.medevit.elexis.kassen.core.model.expressions.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import at.medevit.elexis.kassen.core.model.expressions.internal.AndExpressionTest;
import at.medevit.elexis.kassen.core.model.expressions.internal.EqualsExpressionTest;
import at.medevit.elexis.kassen.core.model.expressions.internal.NotExpressionTest;
import at.medevit.elexis.kassen.core.model.expressions.internal.OrExpressionTest;
import at.medevit.elexis.kassen.core.model.expressions.parser.ExpressionStringParserTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ExpressionStringParserTest.class,
	AndExpressionTest.class, OrExpressionTest.class,
	NotExpressionTest.class, EqualsExpressionTest.class})
public class AllTests {

}

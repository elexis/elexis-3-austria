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

import at.medevit.elexis.kassen.core.model.DateRangeTest;
import at.medevit.elexis.kassen.core.model.GroupPositionAreaTest;
import at.medevit.elexis.kassen.core.model.KassenLeistungGroupTest;
import at.medevit.elexis.kassen.core.model.KassenLeistungTest;
import at.medevit.elexis.kassen.core.model.PointsAreaFactoryTest;
import at.medevit.elexis.kassen.core.model.expressions.internal.InPointRangeExpressionTest;
import at.medevit.elexis.kassen.core.model.expressions.internal.KassenExpressionFactoryTest;
import at.medevit.elexis.kassen.core.model.expressions.internal.KonsultationIncludesPositionExpressionTest;
import at.medevit.elexis.kassen.core.model.expressions.internal.MandantSpecialityIsExpressionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({MandantSpecialityIsExpressionTest.class,
	KonsultationIncludesPositionExpressionTest.class,
	KassenExpressionFactoryTest.class, DateRangeTest.class,
	GroupPositionAreaTest.class, KassenLeistungGroupTest.class,
	KassenLeistungTest.class, PointsAreaFactoryTest.class,
	InPointRangeExpressionTest.class})
public class AllPluginTests {

}
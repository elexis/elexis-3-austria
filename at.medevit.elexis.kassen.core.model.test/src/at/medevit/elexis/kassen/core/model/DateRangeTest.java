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
package at.medevit.elexis.kassen.core.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import at.medevit.elexis.kassen.core.model.expressions.test.TestData;

public class DateRangeTest {
	
	@Test
	public void isIncludedTest() {
		DateRange test = new DateRange(TestData.getNow());
		assertTrue(test.isIncluded(TestData.getDayAfterTomorrow()));
		assertTrue(test.isIncluded(TestData.getNowPlusOneSecond()));
		
		test.setToDate(TestData.getDayAfterTomorrow());
		assertFalse(test.isIncluded(TestData.getDayBeforeYesterday()));
	}
	
	@Test
	public void equalsTest() {
		Date now = TestData.getNow();
		Date before = TestData.getDayBeforeYesterday();
		Date after = TestData.getDayAfterTomorrow();
		
		DateRange test = new DateRange(now);
		DateRange other = new DateRange(now);
		assertTrue(test.equals(other));
		assertFalse(test.equals(new Object()));
		
		test.setToDate(after);
		assertFalse(test.equals(other));
		other.setToDate(after);
		assertTrue(test.equals(other));

		test = new DateRange(now);
		other = new DateRange(before);
		assertFalse(test.equals(other));
	}
}

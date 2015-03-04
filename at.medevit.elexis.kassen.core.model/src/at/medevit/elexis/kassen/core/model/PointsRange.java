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

import java.text.NumberFormat;
import java.text.ParseException;

public class PointsRange {
	double from;
	double to;

	public static final NumberFormat numberFormat = NumberFormat.getInstance();
	
	public PointsRange(String from, String to) throws ParseException {
		this.from = numberFormat.parse(from).doubleValue();
		this.to = numberFormat.parse(to).doubleValue();
	}
	
	public PointsRange(double from, double to) {
		this.from = from;
		this.to = to;
	}
	
	public boolean isIncluded(double value) {
		return value >= from && value <= to;
	}

	@Override
	public String toString() {
		if(to != Double.POSITIVE_INFINITY)
			return from + "-" + to;
		else
			return Double.toString(from);
	}
}

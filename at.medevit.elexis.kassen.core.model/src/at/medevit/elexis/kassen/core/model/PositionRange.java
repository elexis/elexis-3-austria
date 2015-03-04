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

public class PositionRange implements IPositionArea {

	KassenLeistungGroup from;
	KassenLeistungGroup to;
	
	public PositionRange(KassenLeistung group) {
		from =  new KassenLeistungGroup(group);
		to = new KassenLeistungGroup(group);
	}
	
	public PositionRange(KassenLeistung from, KassenLeistung to) {
		this.from =  new KassenLeistungGroup(from);
		this.to =  new KassenLeistungGroup(to);
	}
	
	@Override
	public boolean includesPosition(KassenLeistung position) {
		KassenLeistungGroup pos = new KassenLeistungGroup(position);
		
		if(pos.isSubgroupOf(from) || pos.isSubgroupOf(to)) {
			return true;
		}else if(pos.isHigherThan(from) && pos.isLowerThan(to))
			return true;
		else if (pos.isEqual(from) || pos.isEqual(to))
			return true;
		
		return false;
	}

	@Override
	public String toString() {
		if(to.toString().equals(from.toString()))
			return "(" + from + ")";

		return "(" + from + "-" + to + ")";
	}
}

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
package at.medevit.elexis.kassen.core.importer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PointArea {
	@XmlElement(name = "area")
	public Area area;
	
	@XmlElement(name = "multiplier")
	public Multiplier multiplier;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(area.toString() + " " + multiplier.toString());
		return sb.toString();
	}
}


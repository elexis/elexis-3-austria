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
package at.medevit.elexis.kassen.svb;

import java.util.List;

import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.ui.KassenLeistungDetailDisplay;
import at.medevit.elexis.kassen.svb.model.SvbLeistung;

public class SvbDetailDisplay extends KassenLeistungDetailDisplay {
	
	public SvbDetailDisplay(){}
	
	public Class getElementClass(){
		return SvbLeistung.class;
	}
	
	public String getTitle(){
		return "SVA"; //$NON-NLS-1$
	}
	
	@Override
	protected KassenLeistung getGroupForActCode(){
		List<? extends KassenLeistung> groups =
			KassenLeistung.getCurrentLeistungenByIds(actCode.getPositionGroup(), null, null, null,
				SvbLeistung.class);
		if (groups.size() > 0)
			return groups.get(0);
		return null;
	}
	
}

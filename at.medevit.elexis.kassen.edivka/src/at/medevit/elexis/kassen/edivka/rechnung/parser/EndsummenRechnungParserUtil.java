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
package at.medevit.elexis.kassen.edivka.rechnung.parser;

import java.math.BigDecimal;
import java.math.RoundingMode;

import at.medevit.elexis.kassen.edivka.rechnung.model.PKVRechnung.EndsummenRechnung;
import ch.elexis.data.Rechnung;
import ch.rgw.tools.Money;

public class EndsummenRechnungParserUtil {
	
	public static EndsummenRechnung getEndsummenRechnung(Rechnung erechnung){
		EndsummenRechnung rechnung = new EndsummenRechnung();
		Money openAmount = erechnung.getOffenerBetrag();
		
		rechnung.setEndsummeBrutto(
			new BigDecimal(openAmount.doubleValue()).setScale(2, RoundingMode.HALF_UP));
			
		return rechnung;
	}
	
}

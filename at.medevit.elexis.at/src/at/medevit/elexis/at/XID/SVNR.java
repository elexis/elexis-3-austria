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
package at.medevit.elexis.at.XID;

import org.eclipse.ui.IStartup;

import ch.elexis.data.Xid;

public class SVNR implements IStartup {
	public static final String DOMAIN_AT_SVNR = "www.sozialversicherung.at/svnr";
	public static final String SIMPLENAME_AT_SVNR = "SVNR";

	@Override
	public void earlyStartup() {
		Xid.localRegisterXIDDomainIfNotExists(SVNR.DOMAIN_AT_SVNR,
				SVNR.SIMPLENAME_AT_SVNR, Xid.ASSIGNMENT_REGIONAL);
	}

	/**
	 * Validate the Versicherungsnummer
	 * 
	 * @param 10-digit-SVNumber (e.g. 2461260779)
	 * @return true if valid, false if invalid
	 */
	public static boolean validateSVNumber(String SVNumber) {
		if (SVNumber.length() != 10)
			return false;

		int[] faktoren = new int[] { 3, 7, 9, 5, 8, 4, 2, 1, 6 };
		int[] number = new int[9];

		int j;
		for (int i = 0; i < 9; i++) {
			j = 0;
			if (i < 3)
				j = i;
			if (i >= 3)
				j = i + 1;
			number[i] = Integer.parseInt(SVNumber.charAt(j) + "");
		}

		int sum = 0;
		for (int k = 0; k < 9; k++) {
			sum += faktoren[k] * number[k];
		}

		int p = sum % 11;
		if (p == Integer.parseInt(SVNumber.charAt(3) + ""))
			return true;

		return false;
	}

}

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
package at.medevit.elexis.befuem.ui.commands;

import org.eclipse.core.expressions.PropertyTester;

import at.medevit.elexis.befuem.contextservice.finding.AbstractFinding;

public class FindingPropertyTester extends PropertyTester {

	public FindingPropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof AbstractFinding) {
			AbstractFinding finding = (AbstractFinding) receiver;
			if ("visited".equals(property)) { //$NON-NLS-1$
				return finding.isVisited();
			} else if ("imported".equals(property)) { //$NON-NLS-1$
				return finding.isImported();
			} else if ("archived".equals(property)) { //$NON-NLS-1$
				return finding.isArchived();
			}
		}
		return false;
	}
}

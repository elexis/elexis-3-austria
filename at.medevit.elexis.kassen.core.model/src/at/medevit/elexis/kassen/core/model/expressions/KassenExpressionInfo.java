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
package at.medevit.elexis.kassen.core.model.expressions;

import java.util.ArrayList;
import java.util.List;

public class KassenExpressionInfo {
	
	private List<AbstractKassenExpression> negativeResultReasons = null;
	
	public void addNegativeExpression(AbstractKassenExpression abstractKassenExpression) {
		if(negativeResultReasons == null)
			negativeResultReasons = new ArrayList<AbstractKassenExpression>();
		negativeResultReasons.add(abstractKassenExpression);
	}
	
	public List<AbstractKassenExpression> getNegativeExpressions() {
		return negativeResultReasons;
	}
}

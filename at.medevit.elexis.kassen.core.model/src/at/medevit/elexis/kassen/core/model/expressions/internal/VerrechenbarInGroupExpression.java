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
package at.medevit.elexis.kassen.core.model.expressions.internal;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.PositionRange;
import at.medevit.elexis.kassen.core.model.expressions.AbstractKassenExpression;
import at.medevit.elexis.kassen.core.model.expressions.VerrechenbarResolver;

public class VerrechenbarInGroupExpression extends AbstractKassenExpression {

	PositionRange area;
	
	public VerrechenbarInGroupExpression(PositionRange area) {
		this.area = area;
	}
	
	@Override
	public EvaluationResult evaluate(IEvaluationContext context)
			throws CoreException {
		Object[] args = {VerrechenbarResolver.ResolveVariableId.IVERRECHENBARKASSENLEISTUNGOBJ};
		Object element = context.resolveVariable(VerrechenbarResolver.variableName, args);
		
		if(element != null) {
			
			if(element != null) {
				KassenLeistung position = (KassenLeistung)element;
				
				if(area.includesPosition(position)) {
					lastResult = EvaluationResult.TRUE;
					return EvaluationResult.TRUE;
				} else {
					lastResult = EvaluationResult.FALSE;
					return EvaluationResult.FALSE;
				}
			}
		}
		lastResult = EvaluationResult.NOT_LOADED;
		return EvaluationResult.NOT_LOADED;
	}

}

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

import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.expressions.AbstractKassenExpression;
import at.medevit.elexis.kassen.core.model.expressions.MandantResolver;

public class MandantSpecialityIsExpression extends AbstractKassenExpression {

	KassenCodes.SpecialityCode speciality;

	public MandantSpecialityIsExpression(KassenCodes.SpecialityCode parameter) {
		speciality = parameter;
	}

	@Override
	public EvaluationResult evaluate(IEvaluationContext context)
			throws CoreException {
		Object[] args = {MandantResolver.ResolveVariableId.SPECIALITYCODEOBJECT};
		Object element = context.resolveVariable(MandantResolver.variableName, args);

		if(element != null) {
			if(speciality.equals((KassenCodes.SpecialityCode)element)) {
				lastResult = EvaluationResult.TRUE;
				return EvaluationResult.TRUE;
			} else {
				lastResult = EvaluationResult.FALSE;
				return EvaluationResult.FALSE;
			}
		}
		lastResult = EvaluationResult.NOT_LOADED;
		return EvaluationResult.NOT_LOADED;
	}
}

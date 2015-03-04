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

import java.util.List;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.expressions.AbstractKassenExpression;
import at.medevit.elexis.kassen.core.model.expressions.KonsultationResolver;
import ch.elexis.core.data.interfaces.IVerrechenbar;
import ch.elexis.data.Verrechnet;

public class KonsultationIncludesVerrechenbarExpression extends AbstractKassenExpression {

	private String positionId;
	
	public KonsultationIncludesVerrechenbarExpression(String positionId) {
		this.positionId = positionId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EvaluationResult evaluate(IEvaluationContext context)
			throws CoreException {
		Object[] args = {KonsultationResolver.ResolveVariableId.LEISTUNGENLIST};
		Object element = context.resolveVariable(KonsultationResolver.variableName, args);
		
		if(element != null) {
			EvaluationResult ret = includesPositionIdTest((List<Verrechnet>)element);
			if(ret == EvaluationResult.TRUE) {
				lastResult = ret;
				return ret;
			} else {
				lastResult = ret;
				return ret;
			}
		}
		lastResult = EvaluationResult.NOT_LOADED;
		return EvaluationResult.NOT_LOADED;
	}

	private EvaluationResult includesPositionIdTest(List<Verrechnet> list) {
		for(Verrechnet verrechnet : list) {
			IVerrechenbar leistung = verrechnet.getVerrechenbar();
			if(leistung instanceof KassenLeistung) {
				if(((KassenLeistung)leistung).getPosition().equalsIgnoreCase(positionId))
					return EvaluationResult.TRUE;
			}
		}
		return EvaluationResult.FALSE;
	}
}

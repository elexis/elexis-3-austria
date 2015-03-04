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

import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.DateRange;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.PointsRange;
import at.medevit.elexis.kassen.core.model.expressions.AbstractKassenExpression;
import at.medevit.elexis.kassen.core.model.expressions.MandantResolver;
import at.medevit.elexis.kassen.core.model.expressions.VerrechenbarResolver;
import ch.elexis.core.data.interfaces.IVerrechenbar;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Mandant;
import ch.elexis.data.Query;
import ch.elexis.data.Verrechnet;

public class InPointRangeExpression extends AbstractKassenExpression {

	SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyyMMdd");
	
	private PointsRange pointrange;
	private DateRange daterange;
	
	public InPointRangeExpression(PointsRange pr, DateRange dr) {
		pointrange = pr;
		daterange = dr;
	}

	@Override
	public EvaluationResult evaluate(IEvaluationContext context)
			throws CoreException {
		Object[] mandantargs = {MandantResolver.ResolveVariableId.MANDANTOBJ};
		Mandant mandant = (Mandant)context.resolveVariable(MandantResolver.variableName, mandantargs);

		Object[] clazzargs = {VerrechenbarResolver.ResolveVariableId.CODESYSTEMCLASS};
		@SuppressWarnings("unchecked")
		Class<? extends KassenLeistung> clazz = (Class<? extends KassenLeistung>)context.resolveVariable(VerrechenbarResolver.variableName, clazzargs);

		if(mandant != null && clazz != null) {
			// Prepare DB query
			final Query<Konsultation> query = new Query<Konsultation>(Konsultation.class);
			query.add("Datum", ">=", databaseFormat.format(daterange.getFromDate()));
			query.add("Datum", "<=", databaseFormat.format(daterange.getToDate()));
			query.add("MandantID", "=", mandant.getId());
			// Get all Consultation which happened in the specified date range.
			final List<Konsultation> konsultationen = query.execute();
			
			int sumPoints = 0;
			
			for(Konsultation kons : konsultationen) {
				List<Verrechnet> leistungen = kons.getLeistungen();
				sumPoints += sumPoints(leistungen, clazz);
			}
			if(pointrange.isIncluded(sumPoints)) {
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

	int sumPoints(List<Verrechnet> leistungen, Class<? extends KassenLeistung> clazz) {
		int sum = 0;
		for(Verrechnet verrechnet : leistungen) {
			IVerrechenbar leistung = verrechnet.getVerrechenbar();
			if(leistung.getClass() == clazz) {
				sum += ((KassenLeistung)leistung).getPointValue();
			}
		}
		return sum;
	}
}

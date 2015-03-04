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
package at.medevit.elexis.kassen.core.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.IVariableResolver;
import org.eclipse.core.runtime.CoreException;

import at.medevit.elexis.kassen.core.model.expressions.AbstractKassenExpression;
import at.medevit.elexis.kassen.core.model.expressions.KassenExpressionFactory;
import at.medevit.elexis.kassen.core.model.expressions.KassenExpressionInfo;
import at.medevit.elexis.kassen.core.model.expressions.MandantResolver;
import at.medevit.elexis.kassen.core.model.expressions.VerrechenbarResolver;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.interfaces.IOptifier;
import ch.elexis.core.data.interfaces.IVerrechenbar;
import ch.elexis.data.Konsultation;
import ch.elexis.data.Verrechnet;
import ch.rgw.tools.Result;

public class KassenLeistungOptifier implements IOptifier {

	ArrayList<KassenLeistung> additional = null;
	AbstractKassenExpression logik = null;
	
	/**
	 * The additional positions are expected in the following format.
	 * <p>
	 * positionid,positionid,positionid
	 * </p>
	 *  
	 * @param definition
	 * @throws ParseException
	 */
	public void setAdditional(String definition, Class<? extends KassenLeistung> clazz) throws ParseException {
		if(definition != null && definition.length() > 0) {
			additional = KassenLeistung.getAdditionalFromString(definition, clazz);
		}
	}
	
	/**
	 * The logic is expected in a format that is defined in the package
	 * at.medevit.elexis.kassen.core.model.expressions.
	 * 
	 * @param definition
	 * @param clazz
	 * @throws ParseException
	 */
	public void setLogicExpression(String definition, Class<? extends KassenLeistung> clazz) throws ParseException {
		if(definition != null && definition.length() > 0)
			logik = KassenExpressionFactory.getInstance().getExpressionForString(definition, clazz);
	}
	
	@Override
	public Result<Object> optify(Konsultation kons) {
		return new Result<Object>(kons);
	}

	@Override
	public Result<IVerrechenbar> add(IVerrechenbar code, Konsultation kons) {
		KassenLeistung leistung = (KassenLeistung) code;
		
		// test logik if we can add this verrechenbar
		if(logik != null) {
			// setup the context for evaluation
			IVariableResolver[] resolvers = {
					new MandantResolver(CoreHub.actMandant),
					new VerrechenbarResolver(leistung, leistung.getClass())};
			EvaluationContext context = new EvaluationContext(null, "", resolvers);
			EvaluationResult result = EvaluationResult.FALSE;
			try {
				result = logik.evaluate(context);
			} catch (CoreException e) {
				// TODO inform user about the status
				e.printStackTrace();
			}
			
			if(result != EvaluationResult.TRUE) {
				KassenExpressionInfo info = logik.computeKassenExpressionInfo();
				
				return new Result<IVerrechenbar>(
						Result.SEVERITY.WARNING,
						0,
						info.toString(), null, false); //$NON-NLS-1$
			}
		}
		// test zusatz if we should add additional positions
		if(additional != null && additional.size() > 0) {
			for(KassenLeistung pos : additional) {
				add(pos, kons);
			}
		}
		// add the verrechenbar and return success
		addVerrechenbarToKonsultation(code, kons);
		return new Result<IVerrechenbar>(code);
	}

	@Override
	public Result<Verrechnet> remove(Verrechnet code, Konsultation kons) {
		List<Verrechnet> old = kons.getLeistungen();
		old.remove(code);
		code.delete();
		return new Result<Verrechnet>(null);
	}

	private void addVerrechenbarToKonsultation(IVerrechenbar code, Konsultation kons) {
		List<Verrechnet> old = kons.getLeistungen();
		// search for this verrechenbar in the konsultation
		Verrechnet foundVerrechnet = null;
		for (Verrechnet verrechnet : old) {
			if (verrechnet.getVerrechenbar().getId().equals(code.getId())) {
				if (verrechnet.getText().equals(code.getText())) {
					foundVerrechnet = verrechnet;
					break;
				}
			}
		}
		// increase number if found and add new verrechnet if not found 
		if (foundVerrechnet != null) {
			foundVerrechnet.changeAnzahl(foundVerrechnet.getZahl() + 1);
		} else {
			old.add(new Verrechnet(code, kons, 1));
		}
	}
	

}

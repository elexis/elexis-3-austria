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

import static org.junit.Assert.assertTrue;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.IVariableResolver;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.expressions.MandantResolver;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;

public class MandantSpecialityIsExpressionTest {
	
	private SettingsPreferenceStore store;
	private EvaluationContext context;
	
	@Before
	public void runBefore() {  
		store = new SettingsPreferenceStore(CoreHub.mandantCfg);
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET, Integer.toString(KassenCodes.SpecialityCode.HNO.getCode()));
		store.flush();

		IVariableResolver[] resolvers = {new MandantResolver(CoreHub.actMandant)};
		context = new EvaluationContext(null, "", resolvers);
	}  
	
	@Test
	public void testSpecialityIsFalse() throws CoreException {
		MandantSpecialityIsExpression exp = 
			new MandantSpecialityIsExpression(KassenCodes.SpecialityCode.INNERE);
		
		EvaluationResult result = exp.evaluate(context);
		assertTrue(result == EvaluationResult.FALSE);
	}
	
	@Test
	public void testSpecialityIsTrue() throws CoreException {
		MandantSpecialityIsExpression exp = 
			new MandantSpecialityIsExpression(KassenCodes.SpecialityCode.HNO);
		
		EvaluationResult result = exp.evaluate(context);
		assertTrue(result == EvaluationResult.TRUE);
	}
}

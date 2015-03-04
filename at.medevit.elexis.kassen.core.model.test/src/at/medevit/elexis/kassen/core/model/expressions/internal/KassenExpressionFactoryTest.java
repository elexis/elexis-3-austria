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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IVariableResolver;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenCodes;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.expressions.KassenExpressionFactory;
import at.medevit.elexis.kassen.core.model.expressions.MandantResolver;
import at.medevit.elexis.kassen.core.model.expressions.VerrechenbarResolver;
import at.medevit.elexis.kassen.core.model.expressions.test.TestData;
import at.medevit.elexis.kassen.test.shared.KassenLeistungImpl;
import at.medevit.elexis.kassen.test.shared.SharedTestData;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;


public class KassenExpressionFactoryTest {
	
	private SettingsPreferenceStore store;
	private EvaluationContext context;
	
	@BeforeClass
	public static void setup(){
		SharedTestData.importTestLeistungen();
	}
	
	@Before
	public void runBefore(){
		store = new SettingsPreferenceStore(CoreHub.mandantCfg);
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET, "");
		store.flush();
		
		IVariableResolver[] resolvers =
			{
				new MandantResolver(CoreHub.actMandant),
				new VerrechenbarResolver(null, KassenLeistungImpl.class)
			};
		context = new EvaluationContext(null, "", resolvers);
	}
	
	/**
	 * not(positioningroup(1.1-1.2) or positioningroup(2)) and mandantspecialityis(HNO)
	 * 
	 * @throws ParseException
	 * @throws CoreException
	 */
	@Test
	public void testGetExpressionForString() throws ParseException, CoreException{
		KassenExpressionFactory factory = new KassenExpressionFactory();
		Expression ex =
			factory
				.getExpressionForString(TestData.factoryTestExpression, KassenLeistungImpl.class);
		assertNotNull(ex);
		// setup new context and preference
		store.setValue(CorePreferenceConstants.MANDANT_FACHGEBIET,
			Integer.toString(KassenCodes.SpecialityCode.HNO.getCode()));
		store.flush();
		List<? extends KassenLeistung> leistung =
			KassenLeistung.getCurrentLeistungenByIds(null, "1.3", null, null,
				KassenLeistungImpl.class);
		IVariableResolver[] resolvers =
			{
				new MandantResolver(CoreHub.actMandant),
				new VerrechenbarResolver(leistung.get(0), KassenLeistungImpl.class)
			};
		context = new EvaluationContext(null, "", resolvers);
		
		assertTrue(ex.evaluate(context) == EvaluationResult.TRUE);
	}
}

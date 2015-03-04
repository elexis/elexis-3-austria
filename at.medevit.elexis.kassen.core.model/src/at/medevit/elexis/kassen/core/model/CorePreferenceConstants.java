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

public class CorePreferenceConstants {
	//@formatter:off
	public static final String CFG_KEY = "at.medevit.kassen";
	public static final String CFG_ADDITIONAL_KEY = "at.medevit.kassen.additional";
	
	// mandant settings
	public static final String MANDANT_HVNUMBER = "at.medevit.kassen.mandant/HVNumber";	
	public static final String MANDANT_DVRNUMBER = "at.medevit.kassen.mandant/DVRNumber";
	public static final String MANDANT_BUNDESLAND = "at.medevit.kassen.mandant/Bundesland";
	public static final String MANDANT_FACHGEBIET = "at.medevit.kassen.mandant/Fachgebiet";
	public static final String KASSEN_USENEWPOSID = "at.medevit.kassen/UseNewPosId";
	
	// rechnungssteller settings
	public static final String RECHNUNGSSTELLER_ACCOUNTNR = "at.medevit.kassen.rechnungssteller/AccountNumber";	
	public static final String RECHNUNGSSTELLER_BANKCODE = "at.medevit.kassen.rechnungssteller/BankCode";
	public static final String RECHNUNGSSTELLER_BANKCONTACT = "at.medevit.kassen.rechnungssteller/BankContact";
	public static final String RECHNUNGSSTELLER_SPECIALITIES = "at.medevit.kassen.rechnungssteller/Fachgebiete";
	public static final String RECHNUNGSSTELLER_HVNUMBER = "at.medevit.kassen.rechnungssteller/HVNumber";	
	
	// settings per Leistungscode (CorePreferenceConstants.CFG_KEY + "/CodeName")
	public static final String KASSE_HVCODE = "/HVCode";	
	public static final String KASSE_CONTACT = "/Contact";
	public static final String KASSE_BILLINGINTERVAL = "/BillingInterval";
	public static final String KASSE_STDINSURANCECATEGORIE = "/StdInsuranceCategory";
	public static final String KASSE_ISPRIVATE = "/IsPrivateInsurance";
	public static final String KASSE_ISREGION = "/IsRegionInsurance";
	public static final String KASSE_CATALOGVERSION = "/CatalogVersion";
	public static final String KASSE_POINTSVERSION = "/PointsVersion";
	public static final String KASSE_FOREIGNCATALOG = "/ForeignCatalog";
	public static final String KASSE_USEFOREIGNPOINTVALUES = "/UseForeignPointValues";
	public static final String KASSE_CLASSNAME = "/ClassName";
	//@formatter:on
}

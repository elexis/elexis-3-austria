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
package at.medevit.elexis.kassen.privat;

import java.util.List;

import at.medevit.elexis.at.model.PatientKassenData;
import at.medevit.elexis.kassen.privat.model.AdditionalInsurance;
import at.medevit.elexis.kassen.privat.model.PreferenceConstants;
import at.medevit.elexis.kassen.privat.model.PrivatKasse;
import ch.elexis.core.data.interfaces.IVerrechnetAdjuster;
import ch.elexis.data.Verrechnet;
import ch.rgw.tools.Money;

public class PrivatKassenAdjuster implements IVerrechnetAdjuster {
	
	public PrivatKassenAdjuster(){
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void adjust(Verrechnet verrechnet){
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void adjustGetNettoPreis(Verrechnet verrechnet, Money price){
		PatientKassenData data = null;
		List<PatientKassenData> datas =
			PatientKassenData.getByPatient(verrechnet.getKons().getFall().getPatient());
		if (datas.size() > 0)
			data = datas.get(0);
		
		// adjust for private insurance
		if (verrechnet.getKons().getFall().getAbrechnungsSystem()
			.equals(PreferenceConstants.PRIVAT_ABRECHNUNGSSYSNAME)) {
			
			PrivatKasse kasse = null;
			
			if (data != null) {
				String sysOneStr = data.get(PatientKassenData.FLD_SYSTEMONE);
				String sysTwoStr = data.get(PatientKassenData.FLD_SYSTEMTWO);
				kasse = PrivatKasse.getByName(sysOneStr);
				if (kasse == null)
					kasse = PrivatKasse.getByName(sysTwoStr);
			}
			
			if (kasse != null) {
				if (kasse.includesCatalog(verrechnet.getVerrechenbar().getCodeSystemName())) {
					double value = kasse.getValue();
					if (kasse.getValueType() == PreferenceConstants.ValueType.ABSOLUT)
						price.addAmount(value);
					else if (kasse.getValueType() == PreferenceConstants.ValueType.PERCENT) {
						double scale = 1.0 + (value / 100.0);
						price.multiply(scale);
					}
				}
			}
		}
		
		// adjust for additional insurance
		if (data != null) {
			AdditionalInsurance additional = null;
			String additionalStr = data.get(PatientKassenData.FLD_ADDITIONALINSURANCE);
			additional = AdditionalInsurance.getByName(additionalStr);
			
			if (additional != null) {
				double value = additional.getValue();
				if (additional.getValueType() == PreferenceConstants.ValueType.ABSOLUT)
					price.addAmount(value);
				else if (additional.getValueType() == PreferenceConstants.ValueType.PERCENT) {
					double scale = 1.0 + (value / 100.0);
					price.multiply(scale);
				}
			}
		}
	}
}

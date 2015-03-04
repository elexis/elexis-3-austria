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
package at.medevit.elexis.kassen.core.importer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import at.medevit.elexis.kassen.core.model.DateRange;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.LeistungBean;

public class KassenLeistungImporter {
	@SuppressWarnings("rawtypes")
	private Constructor cons = null;
	
	public void closeKassenLeistungen(Date deadline, Class<? extends KassenLeistung> clazz){
		List<KassenLeistung> leistungen = KassenLeistung.getAllCurrentLeistungen(clazz);
		for (KassenLeistung leistung : leistungen) {
			DateRange range = leistung.getValidRange();
			if (range.getToDate() == null && range.getFromDate().before(deadline)) {
				range.setToDate(deadline);
				leistung.setValidRange(range);
			}
		}
	}
	
	public void updateKassenLeistungen(List<LeistungBean> leistungen,
		Class<? extends KassenLeistung> clazz){
		for (LeistungBean bean : leistungen) {
			// check if position / group is already in db
			List<? extends KassenLeistung> existing;
			if (bean.getGruppeId() != null && bean.getGruppeId().length() > 0)
				existing =
					KassenLeistung.getCurrentLeistungenByIds(bean.getGruppeId(), null, null, null,
						clazz);
			else {
				existing =
					KassenLeistung.getCurrentLeistungenByIds(null, bean.getPositionGruppenId(),
						bean.getPositionId(), null, clazz);
			}
			// create new if not there else update
			if (existing.size() == 0) {
				createKassenLeistung(bean, clazz);
			} else {
				updateKassenLeistung(existing, bean, clazz);
			}
		}
	}
	
	private void updateKassenLeistung(List<? extends KassenLeistung> existing, LeistungBean bean,
		Class<? extends KassenLeistung> clazz){
		boolean created = false;
		
		for (KassenLeistung exists : existing) {
			DateRange existsValid = exists.getValidRange();
			Date beanValidFrom;
			try {
				beanValidFrom = KassenLeistung.getDateForString(bean.getValidFromDate());
			} catch (ParseException e) {
				throw new IllegalArgumentException("No valid from date (" + bean.getValidFromDate()
					+ ")" + " on import of grpId: " + bean.getGruppeId() + " posId: "
					+ bean.getPositionId() + " " + clazz, e);
			}
			// overwrite existing data from bean
			// if existing is not closed and valid from matches
			if (existsValid.getFromDate().equals(beanValidFrom) && existsValid.getToDate() == null) {
				exists.setLeistungFromBean(bean);
			}
			// close currently valid and create new from bean
			if (existsValid.getToDate() == null && existsValid.getFromDate().before(beanValidFrom)) {
				
				existsValid.setToDate(beanValidFrom);
				exists.setValidRange(existsValid);
				if (created == false) {
					createKassenLeistung(bean, clazz);
					created = true;
				}
			}
		}
	}
	
	public static String getKassenLeistungSystemName(Class<? extends KassenLeistung> clazz){
		Constructor defCons = getKassenLeistungDefaultConstructor(clazz);
		try {
			KassenLeistung kassenObj = (KassenLeistung) defCons.newInstance();
			return kassenObj.getCodeSystemName();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Could not create KassenLeistung", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Could not create KassenLeistung", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Could not create KassenLeistung", e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static Constructor getKassenLeistungDefaultConstructor(
		Class<? extends KassenLeistung> clazz){
		Class consParameter[] = new Class[0];
		try {
			return clazz.getConstructor(consParameter);
		} catch (SecurityException e) {
			throw new IllegalStateException("Could not get Constructor for " + clazz, e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Could not get Constructor for " + clazz, e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Could not get Constructor for " + clazz, e);
		}
	}
	
	private KassenLeistung createKassenLeistung(LeistungBean leistung,
		Class<? extends KassenLeistung> clazz){
		if (cons == null)
			cons = getKassenLeistungConstructor(clazz);
		try {
			return (KassenLeistung) cons.newInstance(leistung);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Could not create KassenLeistung", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Could not create KassenLeistung", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Could not create KassenLeistung", e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private Constructor getKassenLeistungConstructor(Class<? extends KassenLeistung> clazz){
		Class consParameter[] = new Class[1];
		consParameter[0] = LeistungBean.class;
		try {
			return clazz.getConstructor(consParameter);
		} catch (SecurityException e) {
			throw new IllegalStateException("Could not get Constructor for " + clazz, e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Could not get Constructor for " + clazz, e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Could not get Constructor for " + clazz, e);
		}
	}
}

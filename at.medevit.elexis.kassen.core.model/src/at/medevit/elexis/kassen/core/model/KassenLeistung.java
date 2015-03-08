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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;



import at.medevit.elexis.kassen.core.model.KassenCodes.SpecialityCode;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.interfaces.IOptifier;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Query;
import ch.elexis.data.VerrechenbarAdapter;


/**
 * 
 * @author thomas
 * 
 */
public abstract class KassenLeistung extends VerrechenbarAdapter {
	
	public static final String FLD_GRUPPEID = "gruppeid"; //$NON-NLS-1$
	public static final String FLD_POSITIONGRUPPENID = "positiongruppenid"; //$NON-NLS-1$
	public static final String FLD_POSITIONID = "positionid"; //$NON-NLS-1$
	public static final String FLD_POSITIONNEUID = "positionneuid"; //$NON-NLS-1$
	public static final String FLD_VALIDFROMDATE = "validfromdate"; //$NON-NLS-1$
	public static final String FLD_VALIDTODATE = "validtodate"; //$NON-NLS-1$	
	public static final String FLD_POSITIONTITLE = "positiontitle"; //$NON-NLS-1$
	public static final String FLD_POSITIONHINWEIS = "positionhinweis"; //$NON-NLS-1$
	public static final String FLD_POSITIONAUSFACH = "positionausfach"; //$NON-NLS-1$
	public static final String FLD_POSITIONFACHGEBIETE = "positionfachgebiete"; //$NON-NLS-1$
	public static final String FLD_POSITIONPUNKTWERT = "positionpunktwert"; //$NON-NLS-1$
	public static final String FLD_POSITIONGELDWERT = "positiongeldwert"; //$NON-NLS-1$
	public static final String FLD_POSITIONZUSATZ = "positionzusatz"; //$NON-NLS-1$
	public static final String FLD_POSITIONLOGIK = "positionlogik"; //$NON-NLS-1$
	
	// definition of date format for the database
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
	
	private static final NumberFormat numberFormat = NumberFormat.getInstance();
	
	private static boolean useNewPosId = true;
	
	protected KassenLeistungOptifier kassenOptifier = null;
	
	static {
		numberFormat.setMaximumFractionDigits(4);
	}
	
	static final Comparator<KassenLeistung> GROUP_ORDER = new Comparator<KassenLeistung>() {
		private KassenLeistungGroup group1 = new KassenLeistungGroup();
		private KassenLeistungGroup group2 = new KassenLeistungGroup();
		
		public int compare(KassenLeistung e1, KassenLeistung e2){
			group1.setGroup(e1);
			group2.setGroup(e2);
			if (group1.isLowerThan(group2))
				return -1;
			else if (group1.isHigherThan(group2))
				return 1;
			else
				return 0;
		}
	};
	
	public static void setUseNewPosId(boolean value){
		useNewPosId = value;
	}
	
	public KassenLeistung(){
		
	}
	
	protected KassenLeistung(final String id){
		super(id);
	}
	
	@Override
	public boolean isDragOK(){
		return true;
	}
	
	@Override
	public IOptifier getOptifier(){
		if (kassenOptifier == null) {
			try {
				kassenOptifier = new KassenLeistungOptifier();
				kassenOptifier.setAdditional(get(FLD_POSITIONZUSATZ), this.getClass());
				kassenOptifier.setLogicExpression(get(FLD_POSITIONLOGIK), this.getClass());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return kassenOptifier;
	}
	
	@Override
	public String getCode(){
		return getPosition();
	}
	
	@Override
	public String getText(){
		return getTitle();
	}
	
	@Override
	public String getLabel(){
		if (isGroup())
			return getGroup() + ". " + getTitle();
		else
			return getPosition() + " " + getTitle();
	}
	
	public String getTitle(){
		return get(FLD_POSITIONTITLE);
	}
	
	public String getPosition(){
		if (useNewPosId) {
			String newPosId = get(FLD_POSITIONNEUID);
			if (newPosId != null && newPosId.trim().length() > 0)
				return newPosId;
			else
				return get(FLD_POSITIONID);
		} else {
			return get(FLD_POSITIONID);
		}
	}
	
	public String getAdviceText(){
		return get(FLD_POSITIONHINWEIS);
	}
	
	public String getPositionGroup(){
		return get(FLD_POSITIONGRUPPENID);
	}
	
	public String getGroup(){
		return get(FLD_GRUPPEID);
	}
	
	/**
	 * Check if this Leistung represents a group of Leistungen.
	 * 
	 * @return
	 */
	public boolean isGroup(){
		String grpId = get(FLD_GRUPPEID);
		if (grpId != null && grpId.length() > 0)
			return true;
		return false;
	}
	
	/**
	 * Get the value of this Leistung in points.
	 * 
	 * @return
	 */
	public double getPointValue(){
		String value = get(FLD_POSITIONPUNKTWERT);
		if (value != null && value.length() > 0) {
			try {
				return getDoubleForString(value);
			} catch (ParseException e) {
				return Double.NaN;
			}
		}
		return 0;
	}
	
	/**
	 * Get the value of this Leistung in money.
	 * 
	 * 
	 */
	public double getMoneyValue(){
		String value = get(FLD_POSITIONGELDWERT);
		if (value != null && value.length() > 0) {
			try {
				return getDoubleForString(value);
			} catch (ParseException e) {
				return Double.NaN;
			}
		}
		return 0;
	}
	
	/**
	 * Convert the string to double, according to the format specified for KassenLeistung.
	 * 
	 * @param value
	 * @return double
	 * @throws ParseException
	 */
	public static double getDoubleForString(String value) throws ParseException{
		return numberFormat.parse(value).doubleValue();
	}
	
	/**
	 * Convert the double to string, according to the format specified for KassenLeistung.
	 * 
	 * @param value
	 * @return double
	 */
	public static String getStringForDouble(double value){
		return numberFormat.format(value);
	}
	
	public DateRange getValidRange(){
		String from = get(FLD_VALIDFROMDATE).trim();
		String to = get(FLD_VALIDTODATE).trim();
		
		DateRange ret = new DateRange(getDateForStringWithDefault(from));
		if (to != null && to.length() > 0) {
			ret.setToDate(getDateForStringWithDefault(to));
		}
		return ret;
	}
	
	public List<KassenCodes.SpecialityCode> getSpecialities(){
		ArrayList<KassenCodes.SpecialityCode> ret = new ArrayList<KassenCodes.SpecialityCode>();
		String specialities = get(FLD_POSITIONFACHGEBIETE);
		try {
			if (specialities != null)
				ret.addAll(KassenLeistung.getSpecialitiesForString(specialities));
		} catch (ParseException pe) {
			// ignore and return the empty list
		}
		return ret;
	}
	
	public String getSpecialitiesAsString(){
		StringBuilder sb = new StringBuilder();
		String specialities = get(FLD_POSITIONFACHGEBIETE);
		try {
			if (specialities != null) {
				List<SpecialityCode> specList =
					KassenLeistung.getSpecialitiesForString(specialities);
				for (SpecialityCode sc : specList) {
					if (sb.length() == 0)
						sb.append(sc.getName());
					else
						sb.append(", " + sc.getName());
				}
			}
		} catch (ParseException pe) {
			// ignore and return an empty string
		}
		return sb.toString();
	}
	
	public static String[] getAllKassenCodeSystemNames(){
		return CoreHub.globalCfg.nodes(CorePreferenceConstants.CFG_KEY);
	}
	
	public static String[] getAllRegionKassenCodeSystemNames(){
		SettingsPreferenceStore globalStore = new SettingsPreferenceStore(CoreHub.globalCfg);
		
		String[] kassen = CoreHub.globalCfg.nodes(CorePreferenceConstants.CFG_KEY);
		
		ArrayList<String> ret = new ArrayList<String>();
		for (int i = 0; i < kassen.length; i++) {
			String isRegion =
				globalStore.getString(CorePreferenceConstants.CFG_KEY + "/" + kassen[i]
					+ CorePreferenceConstants.KASSE_ISREGION);
			if (isRegion.equalsIgnoreCase("true")) {
				ret.add(kassen[i]);
			}
		}
		return ret.toArray(new String[0]);
	}
	
	public static String[] getAdditionalInsuranceNames(){
		return CoreHub.globalCfg.nodes(CorePreferenceConstants.CFG_ADDITIONAL_KEY);
	}
	
	/**
	 * Convert the string to date, according to the format specified for KassenLeistung. If
	 * conversion fails now is returned as default.
	 * 
	 * @param value
	 * @return date
	 */
	public static Date getDateForStringWithDefault(String string){
		try {
			return dateformat.parse(string);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	/**
	 * Convert the string to date, according to the format specified for KassenLeistung.
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringForDate(Date date){
		return dateformat.format(date);
	}
	
	/**
	 * Convert the string to date, according to the format specified for KassenLeistung. If
	 * conversion fails ParseException will be thrown.
	 * 
	 * @param value
	 * @return date
	 */
	public static Date getDateForString(String string) throws ParseException{
		return dateformat.parse(string);
	}
	
	/**
	 * Get specialities for a string. The format of the string is expected as comma separated values
	 * representing codes in KassenCodes.SpecialityCode.
	 * <p>
	 * 4, 11,14
	 * </p>
	 * 
	 * @param definition
	 * @return
	 * @throws ParseException
	 */
	public static List<KassenCodes.SpecialityCode> getSpecialitiesForString(String definition)
		throws ParseException{
		ArrayList<KassenCodes.SpecialityCode> ret = new ArrayList<KassenCodes.SpecialityCode>();
		String[] parts = definition.split(",");
		for (int i = 0; i < parts.length; i++) {
			String def = parts[i].trim();
			try {
				SpecialityCode speciality =
					KassenCodes.SpecialityCode.getByCode(Integer.parseInt(def));
				ret.add(speciality);
			} catch (NumberFormatException fx) {
				throw new ParseException("Could not find speciality " + def, 0);
			} catch (IllegalArgumentException iax) {
				throw new ParseException("Could not find speciality " + def, 0);
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @param fachgebiete
	 */
	public static String getStringForSpecialities(List<KassenCodes.SpecialityCode> fachgebiete){
		StringBuilder sb = new StringBuilder();
		
		for (KassenCodes.SpecialityCode element : fachgebiete) {
			if (sb.length() == 0)
				sb.append(element.getCode());
			else
				sb.append("," + element.getCode());
		}
		
		return sb.toString();
	}
	
	/**
	 * Get the additional positions for a string. The format of the string is expected as comma
	 * separated positionids.
	 * <p>
	 * positionid, positionid,positionid
	 * </p>
	 * 
	 * @param definition
	 * @return
	 * @throws ParseException
	 */
	public static ArrayList<KassenLeistung> getAdditionalFromString(String definition,
		Class<? extends KassenLeistung> clazz) throws ParseException{
		ArrayList<KassenLeistung> ret = new ArrayList<KassenLeistung>();
		String[] parts = definition.split(",");
		for (int i = 0; i < parts.length; i++) {
			String positionid = parts[i].trim();
			List<? extends KassenLeistung> additional =
				KassenLeistung.getCurrentLeistungenByIds(null, null, positionid, null, clazz);
			if (additional.size() == 0) {
				// look for a matching new id ...
				additional =
					KassenLeistung.getCurrentLeistungenByIds(null, null, null, positionid, clazz);
			}
			if (additional.size() != 1)
				throw new ParseException("Could not find position " + positionid + " in "
					+ clazz.getName(), 0);
			ret.add(additional.get(0));
		}
		return ret;
	}
	
	/**
	 * Method for determining the table name of this KassenLeistung
	 * 
	 * @param validRange
	 */
	public String getKassenLeistungTableName(){
		return getTableName();
	}
	
	public void setValidRange(DateRange validRange){
		set(new String[] {
			FLD_VALIDFROMDATE, FLD_VALIDTODATE
		}, dateformat.format(validRange.getFromDate()), (validRange.getToDate() == null) ? ""
				: dateformat.format(validRange.getToDate()));
	}
	
	public LeistungBean getBeanForLeistung(){
		LeistungBean ret = new LeistungBean();
		
		ret.setGruppeId(get(FLD_GRUPPEID));
		ret.setPositionGruppenId(get(FLD_POSITIONGRUPPENID));
		ret.setPositionId(get(FLD_POSITIONID));
		ret.setPositionNeuId(get(FLD_POSITIONNEUID));
		ret.setValidFromDate(get(FLD_VALIDFROMDATE));
		ret.setValidToDate(get(FLD_VALIDTODATE));
		ret.setPositionTitle(get(FLD_POSITIONTITLE));
		ret.setPositionHinweis(get(FLD_POSITIONHINWEIS));
		ret.setPositionAusFach(get(FLD_POSITIONAUSFACH));
		ret.setPositionFachgebiete(get(FLD_POSITIONFACHGEBIETE));
		ret.setPositionPunktWert(get(FLD_POSITIONPUNKTWERT));
		ret.setPositionGeldWert(get(FLD_POSITIONGELDWERT));
		ret.setPositionZusatz(get(FLD_POSITIONZUSATZ));
		ret.setPositionLogik(get(FLD_POSITIONLOGIK));
		
		return ret;
	}
	
	public void setLeistungFromBean(LeistungBean bean){
		set(new String[] {
			FLD_GRUPPEID, FLD_POSITIONGRUPPENID, FLD_POSITIONID, FLD_POSITIONNEUID,
			FLD_VALIDFROMDATE, FLD_VALIDTODATE, FLD_POSITIONTITLE, FLD_POSITIONHINWEIS,
			FLD_POSITIONAUSFACH, FLD_POSITIONFACHGEBIETE, FLD_POSITIONPUNKTWERT,
			FLD_POSITIONGELDWERT, FLD_POSITIONZUSATZ, FLD_POSITIONLOGIK
		}, bean.getGruppeId(), bean.getPositionGruppenId(), bean.getPositionId(),
			bean.getPositionNeuId(), bean.getValidFromDate(), bean.getValidToDate(),
			bean.getPositionTitle(), bean.getPositionHinweis(), bean.getPositionAusFach(),
			bean.getPositionFachgebiete(), bean.getPositionPunktWert(), bean.getPositionGeldWert(),
			bean.getPositionZusatz(), bean.getPositionLogik());
	}
	
	/**
	 * Get Leistungen incl. subgroups contained by this Leistung which represents a group.
	 * 
	 * Depends on the field FLD_POSITIONGRUPPENID and FLD_GRUPPEID
	 * 
	 */
	public List<? extends KassenLeistung> getChildren(){
		ArrayList<KassenLeistung> ret = new ArrayList<KassenLeistung>();
		if (isGroup()) {
			ret.addAll(getCurrentPositions(getClass()));
			ret.addAll(getCurrentSubGroups(getClass()));
		}
		return ret;
	}
	
	private List<? extends KassenLeistung> getCurrentPositions(Class<? extends KassenLeistung> clazz){
		AccessType accessType = clazz.getAnnotation(AccessType.class);
		
		if (accessType == null || accessType.accessType() == AccessType.AccessTypes.DIRECT) {
			Query<KassenLeistung> qbe = new Query<KassenLeistung>(clazz);
			qbe.add(FLD_POSITIONGRUPPENID, "LIKE", get(FLD_GRUPPEID)); //$NON-NLS-1$ //$NON-NLS-2$
//			qbe.addToken(FLD_VALIDTODATE + " = " + "''" + " OR " + FLD_VALIDTODATE + " is null");
			return qbe.execute();
		} else {
			Query<ForeignKassenLeistung> qbe = new Query<ForeignKassenLeistung>(clazz);
			qbe.add(ForeignKassenLeistung.FLD_FOREIGNPOSGROUPID,
				"LIKE", get(ForeignKassenLeistung.FLD_FOREIGNGROUPID)); //$NON-NLS-1$ //$NON-NLS-2$
			return qbe.execute();
		}
	}
	
	private List<? extends KassenLeistung> getCurrentSubGroups(Class<? extends KassenLeistung> clazz){
		AccessType accessType = clazz.getAnnotation(AccessType.class);
		
		if (accessType == null || accessType.accessType() == AccessType.AccessTypes.DIRECT) {
			Query<KassenLeistung> qbe = new Query<KassenLeistung>(clazz);
			qbe.add(FLD_GRUPPEID, "LIKE", get(FLD_GRUPPEID) + ".%"); //$NON-NLS-1$ //$NON-NLS-2$
			qbe.addToken(FLD_VALIDTODATE + " = " + "''" + " OR " + FLD_VALIDTODATE + " = null");
			return qbe.execute();
		} else {
			Query<ForeignKassenLeistung> qbe = new Query<ForeignKassenLeistung>(clazz);
			qbe.add(ForeignKassenLeistung.FLD_FOREIGNGROUPID,
				"LIKE", get(ForeignKassenLeistung.FLD_FOREIGNGROUPID) + ".%"); //$NON-NLS-1$ //$NON-NLS-2$
			return qbe.execute();
		}
	}
	
	/**
	 * Get the Leistungen which are the root for all other Leistungen.
	 * 
	 * Depends on the field FLD_GRUPPEID
	 * 
	 * @param clazz
	 * @return List of root KassenLeistung objects
	 */
// public static List<KassenLeistung>getRootLeistungen(Class<? extends KassenLeistung> clazz) {
// Query<KassenLeistung> qbe = new Query<KassenLeistung>(clazz);
//		qbe.add(FLD_GRUPPEID, "LIKE", "_"); //$NON-NLS-1$ //$NON-NLS-2$
// return qbe.execute();
// }
	
	/**
	 * Get the current Leistungen which are the root for all other Leistungen.
	 * 
	 * Depends on the field FLD_GRUPPEID
	 * 
	 * @param clazz
	 * @return List of root KassenLeistung objects
	 */
	public static List<? extends KassenLeistung> getCurrentRootLeistungen(
		Class<? extends KassenLeistung> clazz){
		
		AccessType accessType = clazz.getAnnotation(AccessType.class);
		
		if (accessType == null || accessType.accessType() == AccessType.AccessTypes.DIRECT) {
			Query<KassenLeistung> qbe = new Query<KassenLeistung>(clazz);
			qbe.add(FLD_GRUPPEID, "LIKE", "_"); //$NON-NLS-1$ //$NON-NLS-2$
			qbe.addToken(FLD_VALIDTODATE + " = " + "''" + " OR " + FLD_VALIDTODATE + " = null");
			return qbe.execute();
		} else {
			ForeignKassenLeistung foreign = (ForeignKassenLeistung) getDefaultInstance(clazz);
			if (foreign == null)
				return new ArrayList<KassenLeistung>();
			Class<? extends KassenLeistung> foreignClazz = foreign.getConfiguredForeignClazz();
			if (foreignClazz == null)
				return new ArrayList<KassenLeistung>();
			
			Query<ForeignKassenLeistung> qbe = new Query<ForeignKassenLeistung>(clazz);
			qbe.add(ForeignKassenLeistung.FLD_FOREIGNGROUPID, "LIKE", "_"); //$NON-NLS-1$ //$NON-NLS-2$
			qbe.add(ForeignKassenLeistung.FLD_FOREIGNCLASSNAME, "=", foreignClazz.getName()); //$NON-NLS-1$
			qbe.addToken(ForeignKassenLeistung.FLD_FOREIGNVALIDTODATE + " = " + "''" + " OR "
				+ ForeignKassenLeistung.FLD_FOREIGNVALIDTODATE + " = null");
			return qbe.execute();
		}
	}
	
	/**
	 * Get all Leistungen which are groups of other Leistungen.
	 * 
	 * Depends on the field FLD_GRUPPEID
	 * 
	 * @param clazz
	 * @return List of root KassenLeistung objects
	 */
// public static List<KassenLeistung>getAllGroupLeistungen(Class<? extends KassenLeistung> clazz) {
// Query<KassenLeistung> qbe = new Query<KassenLeistung>(clazz);
//		qbe.add(FLD_GRUPPEID, "!=", ""); //$NON-NLS-1$ //$NON-NLS-2$
// List<KassenLeistung> groups = qbe.execute();
// Collections.sort(groups, GROUP_ORDER);
// return groups;
// }
	
	/**
	 * Get all current Leistungen which are groups of other Leistungen.
	 * 
	 * Depends on the field FLD_GRUPPEID
	 * 
	 * @param clazz
	 * @return List of root KassenLeistung objects
	 */
	public static List<? extends KassenLeistung> getAllCurrentGroupLeistungen(
		Class<? extends KassenLeistung> clazz){
		AccessType accessType = clazz.getAnnotation(AccessType.class);
		
		if (accessType == null || accessType.accessType() == AccessType.AccessTypes.DIRECT) {
			Query<KassenLeistung> qbe = new Query<KassenLeistung>(clazz);
			qbe.add(FLD_GRUPPEID, "!=", ""); //$NON-NLS-1$ //$NON-NLS-2$
			qbe.addToken(FLD_VALIDTODATE + " = " + "''" + " OR " + FLD_VALIDTODATE + " = null");
			List<KassenLeistung> groups = qbe.execute();
			Collections.sort(groups, GROUP_ORDER);
			return groups;
		} else {
			ForeignKassenLeistung foreign = (ForeignKassenLeistung) getDefaultInstance(clazz);
			if (foreign == null)
				return new ArrayList<KassenLeistung>();
			Class<? extends KassenLeistung> foreignClazz = foreign.getConfiguredForeignClazz();
			
			Query<ForeignKassenLeistung> qbe = new Query<ForeignKassenLeistung>(clazz);
			qbe.add(ForeignKassenLeistung.FLD_FOREIGNGROUPID, "!=", ""); //$NON-NLS-1$ //$NON-NLS-2$
			qbe.add(ForeignKassenLeistung.FLD_FOREIGNCLASSNAME, "=", foreignClazz.getName()); //$NON-NLS-1$
			qbe.addToken(ForeignKassenLeistung.FLD_FOREIGNVALIDTODATE + " = " + "''" + " OR "
				+ ForeignKassenLeistung.FLD_FOREIGNVALIDTODATE + " = null");
			List<ForeignKassenLeistung> groups = qbe.execute();
			Collections.sort(groups, GROUP_ORDER);
			return groups;
		}
	}
	
	/**
	 * Get all Leistungen with matching ids.
	 * 
	 * @param clazz
	 * @return List of root KassenLeistung objects
	 */
// public static List<KassenLeistung> getLeistungenByIds(String gruppeId, String positionGruppeId,
// String positionId, String positionNeuId, Class<? extends KassenLeistung> clazz) {
// Query<KassenLeistung> qbe = new Query<KassenLeistung>(clazz);
//
// if(gruppeId != null && gruppeId.length() > 0)
//			qbe.add(FLD_GRUPPEID, "=", gruppeId); //$NON-NLS-1$
// if(positionGruppeId != null && positionGruppeId.length() > 0)
//			qbe.add(FLD_POSITIONGRUPPENID, "=", positionGruppeId); //$NON-NLS-1$
// if(positionId != null && positionId.length() > 0)
//			qbe.add(FLD_POSITIONID, "=", positionId); //$NON-NLS-1$
// if(positionNeuId != null && positionNeuId.length() > 0)
//			qbe.add(FLD_POSITIONNEUID, "=", positionNeuId); //$NON-NLS-1$
//
// return qbe.execute();
// }
	
	/**
	 * Get all current Leistungen with matching ids.
	 * 
	 * @param clazz
	 * @return List of root KassenLeistung objects
	 */
	public static List<? extends KassenLeistung> getCurrentLeistungenByIds(String gruppeId,
		String positionGruppeId, String positionId, String positionNeuId,
		Class<? extends KassenLeistung> clazz){
		AccessType accessType = clazz.getAnnotation(AccessType.class);
		
		if (accessType == null || accessType.accessType() == AccessType.AccessTypes.DIRECT) {
			Query<KassenLeistung> qbe = new Query<KassenLeistung>(clazz);
			if (gruppeId != null && gruppeId.length() > 0)
				qbe.add(FLD_GRUPPEID, "=", gruppeId); //$NON-NLS-1$
			if (positionGruppeId != null && positionGruppeId.length() > 0)
				qbe.add(FLD_POSITIONGRUPPENID, "=", positionGruppeId); //$NON-NLS-1$
			if (positionId != null && positionId.length() > 0)
				qbe.add(FLD_POSITIONID, "=", positionId); //$NON-NLS-1$
			if (positionNeuId != null && positionNeuId.length() > 0)
				qbe.add(FLD_POSITIONNEUID, "=", positionNeuId); //$NON-NLS-1$
				
			qbe.addToken(FLD_VALIDTODATE + " = " + "''" + " OR " + FLD_VALIDTODATE + " = null");
			return qbe.execute();
		} else {
			ForeignKassenLeistung foreign = (ForeignKassenLeistung) getDefaultInstance(clazz);
			if (foreign == null)
				return new ArrayList<KassenLeistung>();
			Class<? extends KassenLeistung> foreignClazz = foreign.getConfiguredForeignClazz();
			
			Query<ForeignKassenLeistung> qbe = new Query<ForeignKassenLeistung>(clazz);
			if (gruppeId != null && gruppeId.length() > 0)
				qbe.add(ForeignKassenLeistung.FLD_FOREIGNGROUPID, "=", gruppeId); //$NON-NLS-1$
			if (positionGruppeId != null && positionGruppeId.length() > 0)
				qbe.add(ForeignKassenLeistung.FLD_FOREIGNPOSGROUPID, "=", positionGruppeId); //$NON-NLS-1$
			if (positionId != null && positionId.length() > 0)
				qbe.add(ForeignKassenLeistung.FLD_FOREIGNPOSID, "=", positionId); //$NON-NLS-1$
				
			qbe.add(ForeignKassenLeistung.FLD_FOREIGNCLASSNAME, "=", foreignClazz.getName()); //$NON-NLS-1$
			qbe.addToken(ForeignKassenLeistung.FLD_FOREIGNVALIDTODATE + " = " + "''" + " OR "
				+ ForeignKassenLeistung.FLD_FOREIGNVALIDTODATE + " = null");
			return qbe.execute();
		}
	}
	
	public static List<KassenLeistung> getAllCurrentLeistungen(Class<? extends KassenLeistung> clazz){
		ArrayList<KassenLeistung> ret = new ArrayList<KassenLeistung>();
		List<? extends KassenLeistung> groups = getAllCurrentGroupLeistungen(clazz);
		for (KassenLeistung group : groups) {
			ret.add(group);
			ret.addAll(getCurrentLeistungenByIds(null, group.getGroup(), null, null, clazz));
		}
		return ret;
	}
	
	private static KassenLeistung getDefaultInstance(Class<? extends KassenLeistung> clazz){
		KassenLeistung ret = null;
		try {
			ret = (KassenLeistung) clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}

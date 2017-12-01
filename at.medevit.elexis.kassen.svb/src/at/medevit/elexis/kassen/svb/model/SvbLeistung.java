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
package at.medevit.elexis.kassen.svb.model;

import java.util.List;

import at.medevit.elexis.kassen.core.model.AccessType;
import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.ForeignKassenLeistung;
import at.medevit.elexis.kassen.core.model.IPointsArea;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.PointsAreaFactory;
import at.medevit.elexis.kassen.svb.ui.SvbPreferenceInitializer;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.interfaces.IFall;
import ch.elexis.core.data.interfaces.IVerrechenbar;
import ch.elexis.core.data.util.Extensions;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.PersistentObjectFactory;
import ch.rgw.tools.TimeTool;
import ch.rgw.tools.VersionInfo;

@AccessType(accessType = AccessType.AccessTypes.FOREIGN)
public class SvbLeistung extends ForeignKassenLeistung implements IVerrechenbar {
	
	protected static final String TABLENAME = "at_medevit_elexis_kassen_SVB_leistungen";
	protected static final String VERSION = "0.1.0";
	
	//@formatter:off
	protected static final String createDB = "CREATE TABLE "
		+ TABLENAME
		+ "("
		+ "ID					VARCHAR(25) PRIMARY KEY," // Elexis internal
		+ "lastupdate 			BIGINT," // Elexis internal
		+ "deleted				CHAR(1) default '0'," // Elexis internal
		+ "foreignposid  		VARCHAR(8)," 
		+ "foreignposgroupid  	VARCHAR(8)," 
		+ "foreigngroupid  		VARCHAR(8),"
		+ "foreignvalidfromdate CHAR(8),"
		+ "foreignvalidtodate 	CHAR(8),"
		+ "foreignid			VARCHAR(25),"
		+ "foreignclassname 	VARCHAR(80)" + ");"
		+ "CREATE INDEX SVBIDX1 ON " + TABLENAME + " (foreignid);"
		+ "INSERT INTO " + TABLENAME
		+ " (ID," + FLD_FOREIGNCLASSNAME + ") VALUES ('VERSION','" + VERSION + "');";
	//@formatter:on
	
	static {
		addMapping(TABLENAME, FLD_FOREIGNPOSID, FLD_FOREIGNPOSGROUPID, FLD_FOREIGNGROUPID,
			FLD_FOREIGNVALIDFROMDATE, FLD_FOREIGNVALIDTODATE, FLD_FOREIGNID, FLD_FOREIGNCLASSNAME);
		
		if (!PersistentObject.tableExists(TABLENAME)) {
			createOrModifyTable(createDB);
		} else {
			SvbLeistung version = load("VERSION");
			VersionInfo vi = new VersionInfo(version.get(FLD_FOREIGNCLASSNAME));
			// if (vi.isOlder(VERSION)) {
			// createOrModifyTable(update010to011);
			// version.set(FLD_POSITIONPUNKTWERT, VERSION);
			// }
		}
	}
	
	public SvbLeistung(){
		
	}
	
	@SuppressWarnings({
		"rawtypes", "unchecked"
	})
	protected SvbLeistung(final String id){
		super(id);
	}
	
	public static SvbLeistung load(final String id){
		return new SvbLeistung(id);
	}
	
	public SvbLeistung(KassenLeistung foreignCode){
		create(null);
		if (!foreignCode.isGroup()) {
			set(FLD_FOREIGNPOSID, foreignCode.get(FLD_POSITIONID));
			set(FLD_FOREIGNPOSGROUPID, foreignCode.get(FLD_POSITIONGRUPPENID));
		} else {
			set(FLD_FOREIGNGROUPID, foreignCode.get(FLD_GRUPPEID));
		}
		set(FLD_FOREIGNVALIDFROMDATE, foreignCode.get(FLD_VALIDFROMDATE));
		set(FLD_FOREIGNVALIDTODATE, foreignCode.get(FLD_VALIDTODATE));
		set(FLD_FOREIGNID, foreignCode.getId());
		set(FLD_FOREIGNCLASSNAME, foreignCode.getClass().getName());
	}
	
	@Override
	public String getXidDomain(){
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String getTableName(){
		return TABLENAME;
	}
	
	@Override
	public String getCodeSystemName(){
		return "SVB";
	}
	
	@Override
	public int getTP(TimeTool date, IFall fall){
		double money = getMoneyValue();
		double points = getPointValue();
		
		if (money == 0 && points > 0) {
			// return points and getFactor is responsible for locating
			// the right multiplier for the system state
			return (int) Math.round((points) * 100.0);
		} else if (points == 0 && money > 0) {
			// return money and getFactor will return 1.0
			return (int) Math.round((money) * 100.0);
		} else {
			// it is possible that a Leistung has money and point value
			// so do the calculation for the points here and handle
			// like money value only
			List<IPointsArea> areas =
				PointsAreaFactory.getInstance().getEnabledPointsAreasForClass(getClass());
			double mv = Double.NaN;
			for (IPointsArea area : areas) {
				if (area.includesPosition(this, date.getTime()))
					mv = area.getMoneyValue();
			}
			
			return (int) Math.round((points * mv) * 100.0) + (int) Math.round((money) * 100.0);
		}
	}
	
	@Override
	public double getFactor(TimeTool date, IFall fall){
		double money = getMoneyValue();
		double points = getPointValue();
		
		// if caller does not care about the time set it to current time
		if (date == null)
			date = new TimeTool();
		
		if (money == 0 && points > 0) {
			SettingsPreferenceStore globalStore = new SettingsPreferenceStore(CoreHub.globalCfg);
			
			String useForeignPointValues = globalStore.getString(SvbPreferenceInitializer.SVBPREF
				+ CorePreferenceConstants.KASSE_USEFOREIGNPOINTVALUES);
			if (useForeignPointValues.equalsIgnoreCase("true")) {
				return foreignLeistung.getFactor(date, fall);
			} else {
				List<IPointsArea> areas =
					PointsAreaFactory.getInstance().getEnabledPointsAreasForClass(getClass());
				double currentValue = Double.NaN;
				int currentWeight = -1;
				for (IPointsArea area : areas) {
					if (area.includesPosition(this, date.getTime())) {
						if (currentWeight < area.getWeight()) {
							currentValue = area.getMoneyValue();
							currentWeight = area.getWeight();
						}
					}
				}
				return currentValue;
			}
		} else if (points == 0 && money > 0) {
			// return money and getFactor will return 1.0
			return 1.0;
		} else {
			// handle like money value only
			return 1.0;
		}
	}
	
	@Override
	public Class<? extends KassenLeistung> getConfiguredForeignClazz(){
		Class<? extends KassenLeistung> ret = null;
		// get class name
		SettingsPreferenceStore globalStore = new SettingsPreferenceStore(CoreHub.globalCfg);
		
		String foreignSystem = globalStore.getString(
			SvbPreferenceInitializer.SVBPREF + CorePreferenceConstants.KASSE_FOREIGNCATALOG);
		String foreignPreferencePrefix = CorePreferenceConstants.CFG_KEY + "/" + foreignSystem;
		String foreignClassName = globalStore
			.getString(foreignPreferencePrefix + CorePreferenceConstants.KASSE_CLASSNAME);
		// get class object for name
		if (foreignClassName != null && foreignClassName.length() > 0) {
			List<PersistentObjectFactory> exts =
				Extensions.getClasses("ch.elexis.PersistentReference", "Class");
			for (PersistentObjectFactory po : exts) {
				ret = po.getClassforName(foreignClassName);
				if (ret != null)
					break;
			}
		}
		return ret;
	}
	
	@Override
	public List<Object> getActions(Object context){
		// TODO Auto-generated method stub
		return null;
	}
}

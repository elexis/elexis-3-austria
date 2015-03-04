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
package at.medevit.elexis.kassen.vaeb.model;

import java.util.List;

import at.medevit.elexis.kassen.core.model.IPointsArea;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.LeistungBean;
import at.medevit.elexis.kassen.core.model.PointsAreaFactory;
import ch.elexis.data.Fall;
import ch.elexis.data.PersistentObject;
import ch.rgw.tools.TimeTool;
import ch.rgw.tools.VersionInfo;

public class VaebLeistung extends KassenLeistung {

	protected static final String TABLENAME = "at_medevit_elexis_kassen_VAEB_leistungen";
	protected static final String VERSION = "0.1.0";
	
	protected static final String createDB =
		"CREATE TABLE " + TABLENAME + "("
			+ "ID					VARCHAR(25) PRIMARY KEY,"	// Elexis internal
			+ "lastupdate 			BIGINT,"					// Elexis internal
			+ "deleted				CHAR(1) default '0',"		// Elexis internal
			+ "gruppeid  			VARCHAR(8),"
			+ "positiongruppenid	VARCHAR(8),"
			+ "positionid			VARCHAR(8),"
			+ "positionneuid		VARCHAR(8),"
			+ "validfromdate 		CHAR(8),"
			+ "validtodate 			CHAR(8),"
			+ "positiontitle		TEXT,"
			+ "positionhinweis      TEXT,"
			+ "positionausfach      CHAR(1) default '0',"
			+ "positionfachgebiete  VARCHAR(64),"
			+ "positionpunktwert	VARCHAR(16),"
			+ "positiongeldwert		VARCHAR(16),"
			+ "positionzusatz		TEXT,"
			+ "positionlogik		TEXT"
			+ ");" 		  
			+ "CREATE INDEX VAEBIDX1 ON " + TABLENAME + " (positionid);"
			+ "INSERT INTO " + TABLENAME
			+ " (ID,positionpunktwert) VALUES ('VERSION','" + VERSION + "');";

	static {
		addMapping(TABLENAME, FLD_GRUPPEID, FLD_POSITIONGRUPPENID, FLD_POSITIONID, FLD_POSITIONNEUID,
				FLD_VALIDFROMDATE, FLD_VALIDTODATE,
				FLD_POSITIONTITLE, FLD_POSITIONHINWEIS, FLD_POSITIONAUSFACH, FLD_POSITIONFACHGEBIETE, FLD_POSITIONPUNKTWERT,
				FLD_POSITIONGELDWERT, FLD_POSITIONZUSATZ, FLD_POSITIONLOGIK);

		VaebLeistung version = load("VERSION");
		if (version.state() < PersistentObject.DELETED) {
			createOrModifyTable(createDB);
		} else {
			VersionInfo vi = new VersionInfo(version.get(FLD_POSITIONPUNKTWERT));
//			if (vi.isOlder(VERSION)) {
//				createOrModifyTable(update010to011);
//				version.set(FLD_POSITIONPUNKTWERT, VERSION);
//			}
		}
	}

	public VaebLeistung() {
		
	}
	
	protected VaebLeistung(final String id){
		super(id);
	}
	
	public VaebLeistung(LeistungBean leistung) {
		 create(null);
		 setLeistungFromBean(leistung);
	}

	public static VaebLeistung load(final String id){
		return new VaebLeistung(id);
	}
	
	@Override
	public int getTP(TimeTool date, Fall fall) {
		double money = getMoneyValue();
		double points = getPointValue();
		
		if(money == 0 && points > 0) {
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
			List<IPointsArea> areas = PointsAreaFactory.getInstance().getEnabledPointsAreasForClass(getClass());
			double mv = Double.NaN;
			for(IPointsArea area : areas) {
				if(area.includesPosition(this, date.getTime()))
					mv = area.getMoneyValue();
			}
			
			return (int) Math.round((points * mv) * 100.0) + (int) Math.round((money) * 100.0);
		}
	}

	@Override
	public double getFactor(TimeTool date, Fall fall) {
		double money = getMoneyValue();
		double points = getPointValue();
		
		// if caller does not care about the time set it to current time
		if(date == null)
			date = new TimeTool();
		
		if(money == 0 && points > 0) {
			List<IPointsArea> areas = PointsAreaFactory.getInstance().getEnabledPointsAreasForClass(getClass());
			double currentValue = Double.NaN;
			int currentWeight = -1;
			for(IPointsArea area : areas) {
				if(area.includesPosition(this, date.getTime())) {
					if(currentWeight < area.getWeight()) {
						currentValue = area.getMoneyValue();
						currentWeight = area.getWeight();
					}
				}
			}
			return currentValue;
		} else if (points == 0 && money > 0) {
			// return money and getFactor will return 1.0
			return 1.0;
		} else {
			// handle like money value only
			return 1.0;
		}
	}

	@Override
	public String getXidDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableName() {
		return TABLENAME;
	}

	@Override
	public String getCodeSystemName() {
		return "VAEB";
	}

	@Override
	public List<Object> getActions(Object context) {
		// TODO Auto-generated method stub
		return null;
	}
}

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
package at.medevit.elexis.kassen.test.shared;

import java.util.List;

import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.LeistungBean;
import ch.elexis.data.Fall;
import ch.elexis.data.PersistentObject;
import ch.rgw.tools.TimeTool;
import ch.rgw.tools.VersionInfo;

public class KassenLeistungImpl extends KassenLeistung {
	
	protected static final String TABLENAME = "AT_KASSEN_TEST_LEISTUNGEN";
	protected static final String VERSION = "0.1.0";
	
	public static String PREFERENCE_PREFIX = CorePreferenceConstants.CFG_KEY + "/TESTIMPL";
	
	//@formatter:off
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
			+ "positionzusatz		VARCHAR(255),"
			+ "positionlogik		VARCHAR(255)"
			+ ");" 		  
			+ "CREATE INDEX IDX1 ON " + TABLENAME + " (positionid);"
			+ "INSERT INTO " + TABLENAME
			+ " (ID," + FLD_POSITIONPUNKTWERT + ") VALUES ('VERSION','" + VERSION + "');";
	//@formatter:on
	
	static {
		addMapping(TABLENAME, FLD_GRUPPEID, FLD_POSITIONGRUPPENID, FLD_POSITIONID,
			FLD_POSITIONNEUID, FLD_VALIDFROMDATE, FLD_VALIDTODATE, FLD_POSITIONTITLE,
			FLD_POSITIONHINWEIS, FLD_POSITIONAUSFACH, FLD_POSITIONFACHGEBIETE,
			FLD_POSITIONPUNKTWERT, FLD_POSITIONGELDWERT, FLD_POSITIONZUSATZ, FLD_POSITIONLOGIK);
		
		KassenLeistungImpl version = load("VERSION");
		if (version.state() < PersistentObject.DELETED) {
			createOrModifyTable(createDB);
		} else {
			VersionInfo vi = new VersionInfo(version.get(FLD_POSITIONPUNKTWERT));
			if (vi.isOlder(VERSION)) {
				// we should update eg. with createOrModifyTable(update.sql);
				// And then set the new version
				version.set(FLD_POSITIONPUNKTWERT, VERSION);
			}
		}
	}
	
	public KassenLeistungImpl(){
		
	}
	
	protected KassenLeistungImpl(final String id){
		super(id);
	}
	
	public KassenLeistungImpl(LeistungBean leistung){
		create(null);
		setLeistungFromBean(leistung);
	}
	
	public static KassenLeistungImpl load(final String id){
		return new KassenLeistungImpl(id);
	}
	
	@Override
	public int getTP(TimeTool date, Fall fall){
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double getFactor(TimeTool date, Fall fall){
		// TODO Auto-generated method stub
		return 0;
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
	public List<Object> getActions(Object context) {
		// TODO Auto-generated method stub
		return null;
	}
}

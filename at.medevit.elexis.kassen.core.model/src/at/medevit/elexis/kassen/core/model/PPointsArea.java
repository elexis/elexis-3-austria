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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.VersionInfo;

public class PPointsArea extends PersistentObject {

	public static final String FLD_CODESYSTEMCLASS = "CodeSystemClass"; //$NON-NLS-1$
	public static final String FLD_ISENABLED = "IsEnabled"; //$NON-NLS-1$
	public static final String FLD_ISUSERDEFINED = "IsUserDefined"; //$NON-NLS-1$
	public static final String FLD_VALIDFROMDATE = "ValidFromDate"; //$NON-NLS-1$
	public static final String FLD_VALIDTODATE = "ValidToDate"; //$NON-NLS-1$
	public static final String FLD_AREADEFINITION = "AreaDefinition"; //$NON-NLS-1$
	public static final String FLD_VALUE = "Value"; //$NON-NLS-1$
	static final String VERSION = "0.1.0"; //$NON-NLS-1$

	// definition of date format for the database
	private static final SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
	
	static final String TABLENAME = "at_medevit_elexis_kassen_core_pointarea"; //$NON-NLS-1$

	/** Definition of the database table */
	static final String createDB = "CREATE TABLE "
			+ TABLENAME
			+ "("
			+ "ID VARCHAR(25) primary key," // This field must always be present
			+ "lastupdate BIGINT," // This field must always be present
			+ "deleted CHAR(1) default '0'," // This field must always be present
			
			+ "CodeSystemClass VARCHAR(255),"
			+ "IsEnabled CHAR(1) default '0',"
			+ "IsUserDefined CHAR(1) default '0',"
			+ "ValidFromDate CHAR(8),"
			+ "ValidToDate CHAR(8),"
			+ "AreaDefinition VARCHAR(255),"
			+ "Value VARCHAR(16));"
			+ "CREATE INDEX "
			+ TABLENAME // Create index as needed
			+ "idx1 on " + TABLENAME + " (CodeSystemClass);" + "INSERT INTO "
			+ TABLENAME + " (ID,CodeSystemClass) VALUES ('VERSION',"
			+ JdbcLink.wrap(VERSION) + ");";
	
	static {
		addMapping(TABLENAME,
				FLD_CODESYSTEMCLASS, FLD_ISENABLED, FLD_ISUSERDEFINED, FLD_VALIDFROMDATE, FLD_VALIDTODATE, FLD_AREADEFINITION, FLD_VALUE);

		PPointsArea version = load("VERSION");
		if (version.state() < PersistentObject.DELETED) {
			createOrModifyTable(createDB);
		} else {
			VersionInfo vi = new VersionInfo(version.get(FLD_CODESYSTEMCLASS));
			if (vi.isOlder(VERSION)) {
				// we should update eg. with createOrModifyTable(update.sql);
				// And then set the new version
				version.set(FLD_CODESYSTEMCLASS, VERSION);
			}
		}
	}
	
	public PPointsArea() {}
	
	public PPointsArea(IPointsArea area, Class<? extends KassenLeistung> clazz) {
		create(null);
		set(new String[] { 
				FLD_CODESYSTEMCLASS,
				FLD_ISENABLED,
				FLD_ISUSERDEFINED,
				FLD_VALIDFROMDATE,
				FLD_VALIDTODATE,
				FLD_AREADEFINITION,
				FLD_VALUE},
				clazz.getName(),
				area.isEnabled() ? "1" : "0",
				area.isUserDefined() ? "1" : "0",
				dateformat.format(area.getValidFromDate()),
				(area.getValidToDate() == null) ? "" : dateformat.format(area.getValidToDate()),
				area.getAreaDefinition(),
				area.getValue());
	}
	
	protected PPointsArea(final String id){
		super(id);
	}
	
	public static PPointsArea load(final String id){
		return new PPointsArea(id);
	}
	
	public String getAreaDefinition() {
		return get(FLD_AREADEFINITION);
	}
	
	public void setAreaDefinition(String definition) {
		set(FLD_AREADEFINITION, definition);
	}
	
	public String getCodeSystemClass() {
		return get(FLD_CODESYSTEMCLASS);
	}
	
	public String getValue() {
		return get(FLD_VALUE);
	}
	
	public boolean getIsUserDefined() {
		return get(FLD_ISUSERDEFINED).equalsIgnoreCase("1") ? true : false;
	}
	
	public void setIsUserDefined(boolean value) {
		set(FLD_ISUSERDEFINED, value ? "1" : "0");
	}
	
	public void setIsEnabled(boolean value) {
		set(FLD_ISENABLED, value ? "1" : "0");
	}
	
	public boolean getIsEnabled() {
		return get(FLD_ISENABLED).equalsIgnoreCase("1") ? true : false;
	}
	
	public double getValueAsDouble() {
		try {
			return KassenLeistung.getDoubleForString(getValue());
		} catch (ParseException e) {
			return(Double.NaN);
		}
	}
	
	public void setValueAsDouble(double value) {
		set(FLD_VALUE, KassenLeistung.getStringForDouble(value));
	}
	
	public DateRange getValidRange() {
		String from = get(FLD_VALIDFROMDATE).trim();
		String to = get(FLD_VALIDTODATE).trim();
		
		DateRange ret = new DateRange(getDateForString(from));
		if(to != null && to.length() > 0) {
			ret.setToDate(getDateForString(to));
		}
		return ret;
	}
	
	private Date getDateForString(String string) {
		try {
			return dateformat.parse(string);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	public void setValidRange(DateRange validRange) {
		set(new String[] { 
				FLD_VALIDFROMDATE,
				FLD_VALIDTODATE},
				dateformat.format(validRange.getFromDate()),
				(validRange.getToDate() == null) ? "" : dateformat.format(validRange.getToDate()));
	}
	
	@Override
	public String getLabel() {
		return getAreaDefinition() + " " + getValue();
	}

	@Override
	protected String getTableName() {
		return TABLENAME;
	}
	
	public static List<PPointsArea> getPersistentPointsAreasForClass(Class<? extends KassenLeistung> clazz) {
		Query<PPointsArea> qbe = new Query<PPointsArea>(PPointsArea.class);
		qbe.add(FLD_CODESYSTEMCLASS, "=", clazz.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		return qbe.execute();
	}
}

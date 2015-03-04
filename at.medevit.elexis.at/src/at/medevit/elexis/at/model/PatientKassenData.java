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
package at.medevit.elexis.at.model;

import java.util.List;

import ch.elexis.data.Patient;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.VersionInfo;

public class PatientKassenData extends PersistentObject {
	
	public static final String VERSION = "1.0.0";
	public static final String TABLENAME = "at_medevit_elexis_at_patientkassendata";
	
	public static final String FLD_PATIENTID = "patientid"; //$NON-NLS-1$
	public static final String FLD_SYSTEMONE = "systemone"; //$NON-NLS-1$
	public static final String FLD_SYSTEMTWO = "systemtwo"; //$NON-NLS-1$
	public static final String FLD_INSURANCECATEGORY = "insurancecategory"; //$NON-NLS-1$
	public static final String FLD_ADDITIONALINSURANCE = "additionalinsurance"; //$NON-NLS-1$
	
	static final String create = "CREATE TABLE " + TABLENAME + " (" + //$NON-NLS-1$
		"ID VARCHAR(25) primary key, "
		+ //$NON-NLS-1$
		"lastupdate BIGINT," + "deleted CHAR(1) default '0',"
		+ //$NON-NLS-1$
		"patientid VARCHAR(25),"
		+ //$NON-NLS-1$
		"systemone VARCHAR,"
		+ //$NON-NLS-1$
		"systemtwo VARCHAR,"
		+ //$NON-NLS-1$
		"insurancecategory VARCHAR,"
		+ //$NON-NLS-1$
		"additionalinsurance VARCHAR"
		+ //$NON-NLS-1$
		");"
		+ //$NON-NLS-1$
		"CREATE INDEX patkassaidx1 ON " + TABLENAME + " (" + FLD_PATIENTID + ");" + //$NON-NLS-1$
		"INSERT INTO " + TABLENAME + " (ID," + FLD_PATIENTID + ") VALUES ('VERSION',"
		+ JdbcLink.wrap(VERSION) + ");";
	
	static {
		addMapping(TABLENAME, FLD_PATIENTID, FLD_SYSTEMONE, FLD_SYSTEMTWO, FLD_INSURANCECATEGORY,
			FLD_ADDITIONALINSURANCE);
		
		if (!tableExists(TABLENAME)) {
			createOrModifyTable(create);
		} else {
			PatientKassenData version = load("VERSION");
			VersionInfo vi = new VersionInfo(version.get(FLD_PATIENTID));
			if (vi.isOlder(VERSION)) {
				// we should update eg. with createOrModifyTable(update.sql);
				// And then set the new version
				version.set(FLD_PATIENTID, VERSION);
			}
		}
	}
	
	public static PatientKassenData load(final String id){
		return new PatientKassenData(id);
	}
	
	protected PatientKassenData(final String id){
		super(id);
	}
	
	public PatientKassenData(){
		
	}
	
	@Override
	public String getLabel(){
		return null;
	}
	
	@Override
	protected String getTableName(){
		return TABLENAME;
	}
	
	public PatientKassenData(Patient patient){
		create(null);
		set(FLD_PATIENTID, patient.getId());
	}
	
	public static List<PatientKassenData> getByPatient(Patient patient){
		Query<PatientKassenData> qbe = new Query<PatientKassenData>(PatientKassenData.class);
		qbe.add("ID", "!=", "VERSION");
		qbe.add(FLD_PATIENTID, "=", patient.getId());
		return qbe.execute();
	}
}

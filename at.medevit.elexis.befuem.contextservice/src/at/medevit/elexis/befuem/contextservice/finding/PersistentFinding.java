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
package at.medevit.elexis.befuem.contextservice.finding;

import java.util.List;

import ch.elexis.data.Patient;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.VersionInfo;

public class PersistentFinding extends PersistentObject {

	public static final int STATUS_NONE = 0x00;
	public static final int STATUS_IMPORTED = 0x01;
	public static final int STATUS_ARCHIVED = 0x02;
	
	public static final String FLD_ID = "ID";
	public static final String FLD_NAME = "Name"; //$NON-NLS-1$	
	public static final String FLD_STATUS = "Status"; //$NON-NLS-1$
	public static final String FLD_DIGEST = "Digest"; //$NON-NLS-1$
	public static final String FLD_PATIENT_ID = "PatientID"; //$NON-NLS-1$
	static final String VERSION = "0.1.0"; //$NON-NLS-1$

	static final String TABLENAME = "at_medevit_elexis_befuem_finding"; //$NON-NLS-1$

	/** Definition of the database table */
	static final String createDB = "CREATE TABLE "
			+ TABLENAME
			+ "("
			+ "ID VARCHAR(25) primary key," // This field must always be present
			+ "lastupdate BIGINT," // This field must always be present
			+ "deleted CHAR(1) default '0'," // This field must always be present
			+ "Name VARCHAR(256),"
			+ "Status VARCHAR(8),"
			+ "Digest VARCHAR(128),"
			+ "PatientID VARCHAR(25));"
			+ "CREATE INDEX "
			+ TABLENAME // Create index as needed
			+ "idx1 on " + TABLENAME + " (Digest);"
			+ "INSERT INTO " + TABLENAME + " (ID,Name) VALUES ('VERSION',"
			+ JdbcLink.wrap(VERSION) + ");";
	
	/**
	 * In the static initializer we construct the table mappings, then we try to
	 * load the Version of the table. If no version is found, we assume the
	 * table has to be created. If we find a version we check if it matches our
	 * version and update the table as needed.
	 */
	static {
		addMapping(TABLENAME, FLD_NAME, FLD_STATUS, FLD_DIGEST, FLD_PATIENT_ID); //$NON-NLS-1$
		PersistentFinding version = load("VERSION"); //$NON-NLS-1$
		if (!version.exists()) {
			createOrModifyTable(createDB);
		} else {
			VersionInfo vi = new VersionInfo(version.get(FLD_NAME));

			if (vi.isOlder(VERSION)) {
				// we should update eg. with createOrModifyTable(update.sql);
				// And then set the new version
				version.set(FLD_NAME, VERSION);
			}
		}
	}
	
	/**
	 * The default constructor must be present but is only called by the
	 * framework
	 */
	PersistentFinding() {}
	
	/**
	 * The constructor with a String parameter must be present for PersistentObject
	 * 
	 * @param id
	 */
	protected PersistentFinding(String id) {
		super(id);
	}
	
	/**
	 * This static method should always be defined. We need this to retrieve
	 * PersistentObjects from the Database
	 * 
	 * @param id
	 * @return
	 */
	public static PersistentFinding load(String id) {
		return new PersistentFinding(id);
	}
	
	/**
	 * Create a PersistentFinding
	 * 
	 */
	public PersistentFinding(String name, String digest) {
		create(null); 
		set(FLD_NAME, name);
		set(FLD_DIGEST, digest);
		set(FLD_STATUS, "0");
	}
	
	/**
	 * Get the PersistentFinding for the digest or null
	 * 
	 * @param digest
	 * @return PersistentFinding or null
	 */
	public static PersistentFinding getWithDigest(String digest) {
		Query<PersistentFinding> nwq = new Query<PersistentFinding>(PersistentFinding.class);
		nwq.add(FLD_DIGEST, Query.EQUALS, digest);
		List<PersistentFinding> result = nwq.execute();
		if(result.size() > 0)
			return result.get(0);
		else
			return null;
	}

	public void setPatient(Patient pat) {
		set(FLD_PATIENT_ID, pat.getId());
	}
	
	public boolean isImported() {
		String status = get(FLD_STATUS);
		int statusInt = Integer.parseInt(status);
		return (statusInt & STATUS_IMPORTED) != 0;
	}
	
	public void setImported(boolean value) {
		String status = get(FLD_STATUS);
		int statusInt = Integer.parseInt(status);
		if(value) {
			statusInt |= STATUS_IMPORTED;
			set(FLD_STATUS, Integer.toString(statusInt));
		} else {
			statusInt &= ~STATUS_IMPORTED;
			set(FLD_STATUS, Integer.toString(statusInt));
		}
	}
	
	public boolean isArchived() {
		String status = get(FLD_STATUS);
		if(status != null) {
			int statusInt = Integer.parseInt(status);
			return (statusInt & STATUS_ARCHIVED) != 0;
		} else {
			return false;
		}
	}
	
	public void setArchived(boolean value) {
		String status = get(FLD_STATUS);
		int statusInt = Integer.parseInt(status);
		if(value) {
			statusInt |= STATUS_ARCHIVED;
			set(FLD_STATUS, Integer.toString(statusInt));
		} else {
			statusInt &= ~STATUS_ARCHIVED;
			set(FLD_STATUS, Integer.toString(statusInt));
		}
	}
	
	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableName() {
		return PersistentFinding.TABLENAME;
	}

}

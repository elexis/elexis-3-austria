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
package at.medevit.elexis.diag.eigene.model;

import java.util.List;

import ch.elexis.core.model.ICodeElement;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.JdbcLink.Stm;
import ch.rgw.tools.VersionInfo;

public class Diagnose extends PersistentObject implements ICodeElement {
	
	public static final String VERSION = "1.0.0";
	public static final String TABLENAME = "at_medevit_elexis_diag_eigene_diagnose";
	
	public static final String FLD_CODE = "code"; //$NON-NLS-1$
	public static final String FLD_TEXT = "text"; //$NON-NLS-1$
	
	static final String create = "CREATE TABLE " + TABLENAME + " (" + //$NON-NLS-1$
		"ID VARCHAR(25) primary key, "
		+ //$NON-NLS-1$
		"lastupdate BIGINT," + "deleted CHAR(1) default '0'," + //$NON-NLS-1$
		
		"code VARCHAR(128),"
		+ //$NON-NLS-1$
		"text TEXT"
		+ //$NON-NLS-1$
		
		");"
		+ //$NON-NLS-1$
		"CREATE INDEX eigene1 ON " + TABLENAME + " (" + FLD_CODE + ");" + //$NON-NLS-1$
		"INSERT INTO " + TABLENAME + " (ID," + FLD_CODE + ") VALUES ('VERSION',"
		+ JdbcLink.wrap(VERSION) + ");";
	
	static {
		addMapping(TABLENAME, FLD_CODE, FLD_TEXT);
		
		if (!tableExists(TABLENAME)) {
			createOrModifyTable(create);
		} else {
			Diagnose version = load("VERSION");
			VersionInfo vi = new VersionInfo(version.get(FLD_CODE));
			if (vi.isOlder(VERSION)) {
				// we should update eg. with createOrModifyTable(update.sql);
				// And then set the new version
				version.set(FLD_CODE, VERSION);
			}
		}
	}
	
	public static Diagnose load(final String id){
		if (id != null) {
			List<Diagnose> diag = Diagnose.getByCode(id);
			if (diag.size() > 0)
				return diag.get(0);
		}
		
		return new Diagnose(id);
	}
	
	protected Diagnose(final String id){
		super(id);
	}
	
	public Diagnose(){
		
	}
	
	public Diagnose(String code, String text){
		create(null);
		set(FLD_CODE, code);
		set(FLD_TEXT, text);
	}
	
	public String getCodeSystemName(){
		return "Eigene";
	}
	
	public String getCodeSystemCode(){
		return "999"; //$NON-NLS-1$
	}
	
	public String getCode(){
		return get(FLD_CODE);
	}
	
	public void setCode(String code){
		JdbcLink conn = getConnection();
		Stm statemnet = conn.getStatement();
		
		statemnet.exec("UPDATE diagnosen SET dg_code=" + JdbcLink.wrap(code) + " WHERE klasse="
			+ JdbcLink.wrap(this.getClass().getName()) + " AND dg_code="
			+ JdbcLink.wrap(get(FLD_CODE)));
		
		conn.releaseStatement(statemnet);
		
		set(FLD_CODE, code);
	}
	
	public String getText(){
		return get(FLD_TEXT);
	}
	
	@Override
	public String getLabel(){
		String text = getText();
		if (text != null && text.trim().length() > 0)
			return getCode() + " - " + text.trim();
		else
			return getCode();
	}
	
	@Override
	protected String getTableName(){
		return TABLENAME;
	}
	
	@Override
	public boolean isDragOK(){
		return true;
	}
	
	public static List<Diagnose> getAll(){
		Query<Diagnose> qbe = new Query<Diagnose>(Diagnose.class);
		qbe.add("ID", "!=", "VERSION");
		return qbe.execute();
	}
	
	public static List<Diagnose> getByCode(String code){
		Query<Diagnose> qbe = new Query<Diagnose>(Diagnose.class);
		qbe.add("ID", "!=", "VERSION");
		qbe.add(FLD_CODE, "=", code);
		return qbe.execute();
	}

	@Override
	public List<Object> getActions(Object context){
		// TODO Auto-generated method stub
		return null;
	}
}

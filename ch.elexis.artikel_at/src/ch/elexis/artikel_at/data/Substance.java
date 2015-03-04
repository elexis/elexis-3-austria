/*******************************************************************************
 * Copyright (c) 2006-2009, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    
 *******************************************************************************/

package ch.elexis.artikel_at.data;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.elexis.core.ui.util.SWTHelper;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.rgw.tools.StringTool;
import ch.rgw.tools.VersionInfo;

public class Substance extends PersistentObject {
	static final String TABLENAME = "CH_ELEXIS_AUSTRIAMEDI_SUBSTANCE";
	public static final String FLD_NAME = "name";
	
	static final String VERSION = "0.3.0";
	static final String createDB = "CREATE TABLE "
		+ TABLENAME
		+ "("
		+ "ID		VARCHAR(25) primary key," //
		+ "lastupdate BIGINT,"
		+ "deleted	CHAR(1) default '0',"
		+ "salt		VARCHAR(30)," // Bezeichnung des Salzes (OPTIONAL)
		+ "name		VARCHAR(254)" // Bezeichnung des Wirkstoffs
		+ ");" + "CREATE INDEX CAUSTRIAS1 ON " + TABLENAME + " (index);"
		+ "CREATE INDEX CAUSTRIAS2 ON " + TABLENAME + " (name);" + "INSERT INTO " + TABLENAME
		+ " (ID,name) VALUES ('VERSION','" + VERSION + "');";
	
	static {
		// WHAT IS THIS? Marco D. / Herzpraxis
		// addMapping(TABLENAME, "name", "index", "medis=JOINT:product:substance:"
		// + Medikament.JOINTTABLE, "interactions=JOINT:Subst1:Subst2:" + Interaction.TABLENAME);
		// Changed to:
		addMapping(TABLENAME, FLD_NAME, "index", "salt");
		Substance v = load("VERSION");
		if (v.state() < PersistentObject.DELETED) {
			createOrModifyTable(createDB);
		} else {
			VersionInfo vi = new VersionInfo(v.get("name"));
			if (vi.isOlder(VERSION)) {
				SWTHelper.showError("Datenbank Fehler", "Tabelle Substance ist zu alt");
			}
		}
	}
	
	@Override
	public String getLabel(){
		return get("name");
	}
	
	public Substance(final String substID, final String name, final String salt){
		create(substID);
		set(new String[] {
			"name", "salt"
		}, StringTool.limitLength(name, 250), StringTool.limitLength(salt, 25));
	}
	
	public SortedSet<Medikament> findMedis(SortedSet<Medikament> list){
		if (list == null) {
			list = new TreeSet<Medikament>();
		}
		List<String[]> lMedis = getList("medis", new String[0]);
		for (String[] r : lMedis) {
			Medikament bm = Medikament.load(r[0]);
			list.add(bm);
		}
		return list;
	}
	
	public List<Substance> sameGroup(){
		return allFromGroup(get("index"));
	}
	
	public static Substance find(final String name){
		String id = new Query<Substance>(Substance.class).findSingle("name", "=", name);
		if (id != null) {
			return load(id);
		}
		return null;
	}
	
	public static List<Substance> allFromGroup(final String group){
		return new Query<Substance>(Substance.class, "index", group).execute();
		
	}
	
	public List<Interaction> getInteractions(){
		return Interaction.getInteractionsFor(this);
	}
	
	public Collection<Interaction> getInteractionsWith(Substance other, SortedSet<Interaction> old){
		if (old == null) {
			old = new TreeSet<Interaction>();
		}
		Query<Interaction> qbe = new Query<Interaction>(Interaction.class);
		qbe.startGroup();
		qbe.add("Subst1", "=", getId());
		qbe.add("Subst2", "=", other.getId());
		qbe.endGroup();
		qbe.or();
		qbe.startGroup();
		qbe.add("Subst1", "=", other.getId());
		qbe.and();
		qbe.add("Subst2", "=", getId());
		qbe.endGroup();
		return qbe.execute(old);
	}
	
	@Override
	protected String getTableName(){
		return TABLENAME;
	}
	
	public static Substance load(final String id){
		return new Substance(id);
	}
	
	protected Substance(){}
	
	protected Substance(final String id){
		super(id);
	}
	
}

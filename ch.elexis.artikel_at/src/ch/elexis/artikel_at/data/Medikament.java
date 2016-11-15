/*******************************************************************************
 * Copyright (c) 2006-2011, G. Weirich and Elexis
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    G. Weirich - initial implementation
 *    M. Descher - Some changes to adapt to new format
 *******************************************************************************/
package ch.elexis.artikel_at.data;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import ch.elexis.data.Artikel;
import ch.elexis.data.Query;
import ch.rgw.tools.ExHandler;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.JdbcLink.Stm;
import ch.rgw.tools.StringTool;

public class Medikament extends Artikel {
	private static final String VERSION = "0.4.0";
	
	public static final String CODESYSTEMNAME = "Medikamente AT";
	static final String JOINTTABLE = "CH_ELEXIS_AUSTRIAMEDI_JOINT";
	static final String EXTTABLE = "CH_ELEXIS_AUSTRIAMEDI_EXT";
	static final String ATCTABLE = "CH_ELEXIS_AUSTRIAMEDI_ATC";
	
	public static final String TYPNAME = "Vidal2";
	
	// Rezeptzeichen und Lagerungshinweise
	public static final String[] RSIGNS = {
		"P1", "P5", "R1", "R2", "SG", "S1", "S5", "W1", "W2", "W6", "W7", "W8", "W9", "W10", "W11",
		"W12", "W13", "W14", "W15", "W16"
	};
	
	// Kassenzeichen und Texte der Sozialversicherung
	public static final String[] SSIGNS = { // "CH14", "KF14", "KF2", "NE", "PS" // Not existant
			// anymore
			"Box", "A", "AU", "B", "D", "DS", "F", "F14", "F2J", "GF", "F6J", "IND", "K", "L",
			"L3", "L6", "L9", "L12", "N", "P", "R", "RE1", "RE2", "U"
		};
	
	static final String extDB = "CREATE TABLE " + EXTTABLE + "(" + "ID VARCHAR(25) primary key,"
		+ "deleted CHAR(2)," + "ATCCODE  VARCHAR(10)," + "NOTES VARCHAR(80),"
		+ "MANUFACTURER VARCHAR(80)," + "DESCRIPTION BLOB" + ");";
	
	static final String atcDB = "CREATE TABLE " + ATCTABLE + "(" + "IDMEDI			VARCHAR(25),"
		+ "IDATC			VARCHAR(25));" + "CREATE INDEX " + ATCTABLE + "idx1" + " ON " + ATCTABLE
		+ " (IDMEDI);" + "CREATE INDEX " + ATCTABLE + "idx2" + " ON " + ATCTABLE + " (IDATC);";
	
	static final String jointDB = "CREATE TABLE " + JOINTTABLE + "("
		+ "ID				VARCHAR(25) primary key," + "product			VARCHAR(25),"
		+ "substance         VARCHAR(25)" + ");" + "CREATE INDEX CHEAUSTRIAMJ1 ON " + JOINTTABLE
		+ " (product);" + "CREATE INDEX CHAUSTRIAMJ2 ON " + JOINTTABLE + " (substance);"
		+ "INSERT INTO " + JOINTTABLE + " (ID,substance) VALUES('VERSION','" + VERSION + "');";
	
	static {
		addMapping(Artikel.TABLENAME, "Gruppe=ExtId", "Generikum=Codeclass",
			"inhalt=JOINT:substance:product:" + JOINTTABLE, "keywords=EXT:" + EXTTABLE + ":notes",
			"description=EXT:" + EXTTABLE + ":description", "KompendiumText=EXT:" + EXTTABLE
				+ ":KompendiumText", "ATC=JOINT:IDATC:IDMEDI:" + ATCTABLE);
		
		transferAllStockInformationToNew32StockModel(new Query<Medikament>(Medikament.class),
			Medikament.class);
	}
	
	/**
	 * 
	 * @param name
	 * @param typ
	 * @param subid
	 *            Die Pharmazentralnummer PhZNr
	 */
	public Medikament(String name, String typ, String subid){
		super(name, typ, subid);
		set(Artikel.FLD_KLASSE, getClass().getName());
	}
	
	@Override
	protected String getConstraint(){
		return new StringBuilder(Artikel.FLD_TYP).append(Query.EQUALS)
			.append(JdbcLink.wrap(TYPNAME)).toString();
	}
	
	protected void setConstraint(){
		set(Artikel.FLD_TYP, TYPNAME);
	}
	
	@Override
	public String getCodeSystemName(){
		return CODESYSTEMNAME;
	}
	
	@Override
	public String getText(){
		return getLabel();
	}
	
	@Override
	public String getLabel(){
		String ret = getInternalName();
		if (StringTool.isNothing(ret)) {
			StringBuilder sb = new StringBuilder();
			sb.append(getInternalName()).append(" (").append(getExt("Quantity")).append(")");
			ret = sb.toString();
		}
		return ret;
	}
	
	/**
	 * Liefert die freie Verschreibbarkeit der Arzneispezialität.
	 * 
	 * 0 = Arzneimittel ist nicht auf Rechnung der Krankenversicherungsträger zugelassen. 1 = Als
	 * frei verschreibbar gilt nur eine Packungsgröße. 2..9 = Als frei verschreibbar gilt das
	 * Doppelte, Dreifache, .. der angegebenen Menge. Die angegebene Menge entspricht dem Inhalt
	 * einer Originalpackung.
	 * 
	 * @return String im Bereich [0..9]
	 */
	public String getRemb(){
		String r = getExt("Remb");
		if (StringTool.isNothing(r)) {
			r = (String) ((Hashtable) getMap(Medikament.FLD_EXTINFO).get("SSigns")).get("Remb");
			setExt("Remb", r);
		}
		return StringTool.isNothing(r) ? "1" : r;
	}
	
	public static List<Medikament> getWithATC(String atcCode){
		Stm stm = getConnection().getStatement();
		String sql = "SELECT IDMEDI FROM " + ATCTABLE + " WHERE IDATC=" + JdbcLink.wrap(atcCode);
		LinkedList<Medikament> ret = new LinkedList<Medikament>();
		try {
			ResultSet res = stm.query(getConnection().translateFlavor(sql));
			while (res != null && res.next()) {
				ret.add(Medikament.load(res.getString(1)));
			}
			return ret;
		} catch (Exception ex) {
			ExHandler.handle(ex);
			return null;
		} finally {
			getConnection().releaseStatement(stm);
		}
	}
	
	@Override
	public String getCode(){
		return getPharmaCode();
	}
	
	public static Medikament load(String id){
		return new Medikament(id);
	}
	
	protected Medikament(){}
	
	protected Medikament(String id){
		super(id);
	}
	
	@Override
	public boolean isDragOK(){
		return true;
	}
	
	public String getBox(){
		return this.get(Medikament.FLD_CODECLASS);
	}
	
}
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
package at.medevit.elexis.befuem.contextservice.networkparticipant;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import ch.elexis.data.Kontakt;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;
import ch.rgw.tools.JdbcLink;
import ch.rgw.tools.VersionInfo;


/**
 * Dieser Datentyp beinhaltet sämtliche importierten Gesundheitsnetzteilnehmer, da in Österreich jeder
 * Arzt eine HV-Nummer erhält, ist diese List allgemeingültig, unabhängig vom realisierten Netzzugang.
 * 
 * @author Marco Descher <descher@medevit.at>
 *
 */
public class NetworkParticipant extends PersistentObject {
	public String HVNummer;
	
	public static final String[] addressbookViewerProperties = new String[] { "HVNummer",
		"nachname",
		"vorname",
		"ortschaft",
		"befundEmail",
		"kontakt"};
	
	public static final String FLD_ID = "ID";
	public static final String FLD_HVNUMMER = "HVNummer"; //$NON-NLS-1$
	public static final String FLD_NACHNAME = "Nachname"; //$NON-NLS-1$
	public static final String FLD_VORNAME = "Vorname"; //$NON-NLS-1$
	public static final String FLD_ORTSCHAFT = "Ort"; //$NON-NLS-1$
	public static final String FLD_BEFUND_EMAIL = "BefundEmail"; //$NON-NLS-1$
	public static final String FLD_TEILNEHMER_SEIT = "TeilnehmerSeit"; //$NON-NLS-1$
	public static final String FLD_KONTAKT_ID = "Kontakt_Id"; //$NON-NLS-1$	
	static final String VERSION = "0.1.1"; //$NON-NLS-1$

	static final String TABLENAME = "at_medevit_elexis_befuem_networkparticipants"; //$NON-NLS-1$

	/** Definition of the database table */
	static final String createDB = "CREATE TABLE "
			+ TABLENAME
			+ "("
			+ "ID VARCHAR(25) primary key," // This field must always be present
			+ "lastupdate BIGINT," // This field must always be present
			+ "deleted CHAR(1) default '0'," // This field must always be present
			+ "HVNummer VARCHAR(10),"
			+ "Nachname VARCHAR(50)," // Use VARCHAR, CHAR, TEXT and BLOB
			+ "Vorname VARCHAR(50)," // No numeric fields
			+ "Ort VARCHAR(25)," // VARCHARS can be read as integrals
			+ "BefundEmail VARCHAR(50)," // use always this for dates
			+ "TeilnehmerSeit CHAR(8),"
			+ "Kontakt_Id VARCHAR(25));"
			+ "CREATE INDEX "
			+ TABLENAME // Create index as needed
			+ "idx1 on " + TABLENAME + " (HVNummer);"
			+ "INSERT INTO " + TABLENAME + " (ID,HVNummer) VALUES ('VERSION',"
			+ JdbcLink.wrap(VERSION) + ");";
	
	static final String update010to011 = "ALTER TABLE " + TABLENAME + " ADD Kontakt_Id VARCHAR(25);";
	
	/**
	 * In the static initializer we construct the table mappings, then we try to
	 * load the Version of the table. If no version is found, we assume the
	 * table has to be created. If we find a version we check if it matches our
	 * version and update the table as needed.
	 */
	static {
		addMapping(TABLENAME, FLD_HVNUMMER, FLD_NACHNAME, FLD_VORNAME, //$NON-NLS-1$ //$NON-NLS-2$
				FLD_ORTSCHAFT, FLD_BEFUND_EMAIL, "FLD_TEILNEHMER_SEIT=S:D:Date", FLD_KONTAKT_ID); //$NON-NLS-1$ //$NON-NLS-2$
		NetworkParticipant version = load("VERSION"); //$NON-NLS-1$
		if (!version.exists()) {
			createOrModifyTable(createDB);
		} else {
			VersionInfo vi = new VersionInfo(version.get(FLD_HVNUMMER));

			if (vi.isOlder("0.1.1")) {
				createOrModifyTable(update010to011);
				// we should update eg. with createOrModifyTable(update.sql);
				// And then set the new version
				version.set(FLD_HVNUMMER, VERSION);
			}
		}
	}
	
	/**
	 * Erstelle einen neuen Netzwerkteilnehmer.
	 * 
	 * @param hvNummer: Hauptverbandsnummer im Netz, eg: ME328300)
	 * @param nachname: Nachname des Teilnehmers
	 * @param vorname:  Vorname des Teilnehmers
	 * @param ort: 		Ansiedlungsort des Teilnehmers
	 */
	public NetworkParticipant(String hvNummer, String nachname, String vorname, String ort, String befundemail) {
		create(null); 
		set(new String[] { FLD_HVNUMMER, FLD_NACHNAME, FLD_VORNAME, FLD_ORTSCHAFT, FLD_BEFUND_EMAIL}, 
				hvNummer, nachname, vorname, ort, befundemail);
	}
	
	/**
	 * This static method should always be defined. We need this to retrieve
	 * PersistentObjects from the Database
	 * 
	 * @param id
	 * @return
	 */
	public static NetworkParticipant load(String id) {
		return new NetworkParticipant(id);
	}
	
	/**
	 * The constructor with a String parameter must be present
	 * 
	 * @param id
	 */
	protected NetworkParticipant(String id) {
		super(id);
	}

	/**
	 * Einen Netzwerkteilnummer aufgrund seiner HVNummer laden
	 * 
	 * @param HVNummer
	 * @return
	 */
	public static NetworkParticipant getWithHVNummer(String HVNummer) {
		Query<NetworkParticipant> nwq = new Query<NetworkParticipant>(NetworkParticipant.class);
		nwq.clear();
		nwq.add(FLD_HVNUMMER, "=", HVNummer);
		List<NetworkParticipant> result = nwq.execute();
		if(result.size() > 0)
			return result.get(0);
		else
			return null;
	}
	
	/**
	 * Get all NetworkParticipants from the db
	 * 
	 * @return List of all NetworkParicipants
	 */
	public static List<NetworkParticipant> getAll() {
		Query<NetworkParticipant> partQuery = new Query<NetworkParticipant>(NetworkParticipant.class);
		partQuery.add(NetworkParticipant.FLD_ID, Query.NOT_EQUAL, "VERSION"); //$NON-NLS-1$
		return partQuery.execute();
	}
	
	/**
	 * The default constructor must be present but is only called by the
	 * framework
	 */
	NetworkParticipant() {}
	
	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableName() {
		return NetworkParticipant.TABLENAME;
	}
	
	
	// IMPLEMENT BEAN CAPABILITY
	public String getHVNummer() {
		return get(NetworkParticipant.FLD_HVNUMMER);
	}
	public String getNachname() {
		return get(NetworkParticipant.FLD_NACHNAME);
	}
	public String getVorname() {
		return get(NetworkParticipant.FLD_VORNAME);
	}
	public String getOrtschaft() {
		return get(NetworkParticipant.FLD_ORTSCHAFT);
	}
	public String getBefundEmail() {
		return get(NetworkParticipant.FLD_BEFUND_EMAIL);
	}

	public Kontakt getKontakt() {
		String kontaktId = get(NetworkParticipant.FLD_KONTAKT_ID);
		if(kontaktId != null && kontaktId.length() > 0)
			return Kontakt.load(kontaktId);
		return null;
	}
	
	public void setKontakt(Kontakt kontakt) {
		set(NetworkParticipant.FLD_KONTAKT_ID, kontakt.getId());
	}

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}
	
}

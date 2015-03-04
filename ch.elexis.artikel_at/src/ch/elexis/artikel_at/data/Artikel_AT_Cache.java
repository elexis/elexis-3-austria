/*******************************************************************************
 * Artikel AT static cache classe, persisting to NamedBlob2
 *
 * Contributors:
 *    M. Descher - Modifications due to performance problems on selector (WiP)
 *    
 *******************************************************************************/
package ch.elexis.artikel_at.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.artikel_at.PreferenceConstants;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.data.NamedBlob2;
import ch.elexis.data.Query;
import ch.rgw.tools.TimeTool;

public class Artikel_AT_Cache {
	private static Logger log = LoggerFactory.getLogger(Artikel_AT_Cache.class);
	
	public static final int MEDIKAMENT_AT_CACHE_ELEMENT_BOX = 0;
	public static final int MEDIKAMENT_AT_CACHE_ELEMENT_LABEL = 1;
	
	private static boolean initialized = false;
	private static String MEDIKAMENT_AT_CACHE_STORE_NAME_NAMEDBLOB2 = "MedikamentATCache";
	private static NamedBlob2 cacheStorage = null;
	private static HashMap<String, HashMap<Integer, String>> artikelATcache = null;
	private static HashMap<Integer, String> resultSet = null;
	
	private static void initialize(){
		if (!initialized) {
			cacheStorage = NamedBlob2.create(MEDIKAMENT_AT_CACHE_STORE_NAME_NAMEDBLOB2, false);
			if (artikelATcache == null) {
				artikelATcache = new HashMap<String, HashMap<Integer, String>>();
				if (resultSet == null)
					resultSet = new HashMap<Integer, String>();
				loadCache();
			}
			initialized = true;
		}
	}
	
	public static String get(String id, int element){
		if (!initialized)
			initialize();
		if (artikelATcache.containsKey(id)) {
			resultSet = artikelATcache.get(id);
			// System.out.println("CACHE::ID: "+id+" / BOX "+resultSet.get(0)+" / Label "+resultSet.get(1));
		} else {
			Medikament medi = Medikament.load(id);
			resultSet.put(MEDIKAMENT_AT_CACHE_ELEMENT_BOX, medi.getBox());
			StringBuffer sb = new StringBuffer();
			sb.append(medi.getLabel()).append("/").append(medi.getRemb());
			resultSet.put(MEDIKAMENT_AT_CACHE_ELEMENT_LABEL, sb.toString());
		}
		return resultSet.get(element);
	}
	
	private static void loadCache(){
		try {
			ByteArrayInputStream ba = new ByteArrayInputStream(cacheStorage.getBytes());
			ObjectInputStream oba;
			oba = new ObjectInputStream(ba);
			artikelATcache = (HashMap<String, HashMap<Integer, String>>) oba.readObject();
			System.out.println("Loaded HashMap with " + artikelATcache.size() + " mappings");
		} catch (IOException e) {
			log.warn("", e);
		} catch (ClassNotFoundException e) {
			log.warn("", e);
		}
	}
	
	public static void updateCache(){
		if (!initialized) {
			if (artikelATcache == null)
				artikelATcache = new HashMap<String, HashMap<Integer, String>>();
			initialized = true;
		}
		artikelATcache.clear();
		Query<Medikament> qMedi = new Query<Medikament>(Medikament.class);
		qMedi.clear();
		qMedi.add("Name", "LIKE", "%");
		qMedi.orderBy(false, "Name");
		List<Medikament> list = qMedi.execute();
		for (Medikament medikament : list) {
			resultSet = new HashMap<Integer, String>();
			resultSet.put(MEDIKAMENT_AT_CACHE_ELEMENT_BOX, medikament.getBox());
			StringBuffer sb = new StringBuffer();
			sb.append(medikament.getLabel()).append("/").append(medikament.getRemb());
			resultSet.put(MEDIKAMENT_AT_CACHE_ELEMENT_LABEL, sb.toString());
			artikelATcache.put(medikament.getId(), resultSet);
		}
		
		cacheStorage = NamedBlob2.create(MEDIKAMENT_AT_CACHE_STORE_NAME_NAMEDBLOB2, false);
		try {
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			ObjectOutputStream oba = new ObjectOutputStream(ba);
			oba.writeObject(artikelATcache);
			oba.close();
			cacheStorage.putBytes(ba.toByteArray());
			System.out.println("Stored HashMap with " + artikelATcache.size() + " mappings");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		TimeTool ts = new TimeTool();
		CoreHub.globalCfg.set(PreferenceConstants.ARTIKEL_AT_CACHEUPDATE_TIME,
			ts.toString(TimeTool.FULL_GER));
	}
}

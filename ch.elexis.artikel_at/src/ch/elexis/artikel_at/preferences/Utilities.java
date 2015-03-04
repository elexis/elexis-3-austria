package ch.elexis.artikel_at.preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.elexis.admin.AccessControlDefaults;
import ch.elexis.artikel_at.data.Medikament;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.ui.util.SWTHelper;
import ch.elexis.data.Artikel;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Prescription;
import ch.elexis.data.Query;
import ch.elexis.data.Rezept;

public class Utilities {
	private static Logger log = LoggerFactory.getLogger(Utilities.class);

	/**
	 * Dieses Skript reinigt die patient_artikel_joint Tabelle (in Elexis
	 * Prescription). Datensätze die weder einen existenten Patient, einen
	 * existenten Artikel noch ein existentes Rezept vorweisen werden gelöscht.
	 * 
	 * Benötigt DELETE_MEDICATION Rechte!
	 * 
	 * @author Marco Descher / Herzpraxis Dr. Thomas Wolber
	 */
	public static void cleanPrescriptionTable() {
		if (!CoreHub.acl.request(AccessControlDefaults.DELETE_MEDICATION)) {
			return;
		}

		Query<Prescription> qPres = new Query<Prescription>(Prescription.class);
		List<Prescription> presList = qPres.execute();
		int invalidRecipes = 0;
		for (Iterator<Prescription> iterator = presList.iterator(); iterator.hasNext();) {
			Prescription prescription = (Prescription) iterator.next();
			boolean validPerson = true;
			boolean validArticle = true;
			boolean validRecipe = true;

			String refPerID = prescription.get(Prescription.PATIENT_ID);
			if (refPerID == null || refPerID.equals(""))
				validPerson = false;
			String refArtID = prescription.get(Prescription.ARTICLE);
			if (refArtID == null || refArtID.equals(""))
				validArticle = false;
			String refRezID = prescription.get(Prescription.REZEPT_ID);
			if (refRezID == null || refRezID.equals(""))
				validRecipe = false;

			Kontakt refPer = Kontakt.load(refPerID);
			if (refPer.state() == Kontakt.INEXISTENT)
				validPerson = false;
			Artikel refArt = Artikel.load(refArtID);
			if (refArt.state() == Artikel.INEXISTENT)
				validArticle = false;
			Rezept refRez = Rezept.load(refRezID);
			if (refRez.state() == Rezept.INEXISTENT)
				validRecipe = false;

			if (!validPerson && !validArticle && !validRecipe) {
				invalidRecipes++;
				log.trace("Lösche Veschreibung " + prescription.getId()
						+ ". Weder gültiger Kontakt, Artikel noch Rezept gefunden.");
				// Werden nicht REAL aus der DB entfernt, wuerde hier aber Sinn
				// machen!
				prescription.remove();
			}
		}
		SWTHelper.showInfo(invalidRecipes + " Rezepte gelöscht.", invalidRecipes
				+ " Rezepte wurden als ungültig gelöscht.");
	}

	/**
	 * Dieses Skript such von jeder aktuellen Verordnung die zugehörige
	 * Pharma-Zentralnummer, anschliessend wird unter Artikel das aktuellste
	 * Medikament mit identischer Pharma-Zentralnummer ausgewählt und die
	 * Verknüpfung upgedatet.
	 * 
	 * FOR ALL m: artikelid IN patient_artikel_joint { String PhZNr =
	 * m.artikelid->subid; Medikament[] medis = artikel.getsubid(PhZNr);
	 * Medikament current = medis.getNewest(); // Last updated m.artikelid =
	 * current.id; medis.remove(!=current && current.PhZNr == medis[i].PhZNr); }
	 * 
	 * Benötigt DELETE_MEDICATION Rechte!
	 * 
	 * @author Marco Descher / Herzpraxis Dr. Thomas Wolber
	 */
	public static void updateMediReferences() {
		if (!CoreHub.acl.request(AccessControlDefaults.DELETE_MEDICATION)) {
			return;
		}
		// Query<Artikel> qbe = new Query<Artikel>(Artikel.class);
		// List<Artikel> artikelList = qbe.execute(); // does not return
		// elements marked as deleted
		Query<Prescription> qPres = new Query<Prescription>(Prescription.class);
		List<Prescription> presList = qPres.execute();
		Artikel currArtikel;

		File outfile = new File(System.getProperty("user.home") + File.separator + "elexis" + File.separator
				+ "ArtikelATupdateReferences.log");
		try {
			PrintWriter pen = new PrintWriter(outfile);
			int noOfPrescriptions = 0;
			int noOfUpdates = 0;

			// FOR ALL prescription: patient_artikel_joint
			for (Iterator<Prescription> iterator = presList.iterator(); iterator.hasNext();) {
				Prescription prescription = (Prescription) iterator.next();
				noOfPrescriptions++;
				currArtikel = prescription.getArtikel();
				//
				String PhZNr = currArtikel.get(Artikel.FLD_SUB_ID);

				if (PhZNr != "" && PhZNr != null) {
					Query<Artikel> qArt = new Query<Artikel>(Artikel.class);
					qArt.clear();
					qArt.add(Artikel.FLD_SUB_ID, "=", PhZNr);
					List<Artikel> artList = qArt.execute();
					// List<Artikel> artList = qArt.executeWithDeleted();

					try {
						Artikel newest = artList.get(0);
						long newestint = 0;
						for (Artikel artikel : artList) {
							String updateTime = artikel.get(Artikel.FLD_LASTUPDATE);
							if (updateTime.equalsIgnoreCase(""))
								continue;
							long time = Long.parseLong(updateTime);
							// pen.println(PhZNr+" found with date"+time);
							if (time > newestint) {
								newestint = time;
								newest = artikel;
							}
						}

						if (newest.equals(currArtikel)) {
							pen.println(prescription.getId() + ":[OK]:" + PhZNr + " No update necessary.");
							continue;
						}
						prescription.set(Prescription.ARTICLE, newest.storeToString());
						noOfUpdates++;
						pen.println(prescription.getId() + ":[OK]:" + PhZNr + " Update to " + newest.getName()
								+ " from " + newest.get(Artikel.FLD_LASTUPDATE));

					} catch (IndexOutOfBoundsException e) {
						pen.println(prescription.getId() + ":[ERR]:" + PhZNr
								+ " No Change - Kein passendes Medikament gefunden. artList.size():" + artList.size());
					}
				} else {
					pen.println(prescription.getId() + ":[ERR]:"
							+ "No Change - Referenzierter Artikel hat keine Pharma-ZentralNr.");
				}
			}
			pen.println("Anzahl Verschreibungen: " + noOfPrescriptions);
			pen.println("Anzahl Updates:" + noOfUpdates);
			pen.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Dieses Skript sucht nach Medikamenten mit der gleichen
	 * Pharma-Zentralnummer. Von den gegebenen Einträgen wird der neueste
	 * gewählt, dieser wird nicht gelöscht. Von den älteren Einträgen wird
	 * überprüft ob ein Link auf diese vorhanden ist, falls nein, werden sie
	 * gelöscht.
	 * 
	 * TODO: BlackBox Medikamente können sofern keine Verschreibungen mehr
	 * darauf referenzieren auch gelöscht werden!
	 */
	public static void cleanMedikamente() {
		File outfile = new File(System.getProperty("user.home") + File.separator + "elexis" + File.separator
				+ "ArtikelATcleanMedikamente.log");
		try {
			PrintWriter pen = new PrintWriter(outfile);
			pen.println("starting");
			pen.flush();

			int deleted = 0;

			Query<Prescription> presList = new Query<Prescription>(Prescription.class);
			List<Prescription> result = presList.execute();
			LinkedList<Medikament> resultIDMap = new LinkedList<Medikament>();

			for (Prescription p : result) {
				if (p.getArtikel() instanceof Medikament) {
					resultIDMap.add((Medikament) p.getArtikel());
				}
			}

			Query<Medikament> qMedi = new Query<Medikament>(Medikament.class);
			List<Medikament> mediList = qMedi.execute();
			for (Iterator<Medikament> iterator = mediList.iterator(); iterator.hasNext();) {
				Medikament medikament = (Medikament) iterator.next();
				String PhZNr = medikament.get(Artikel.FLD_SUB_ID);

				if (PhZNr != "" && PhZNr != null) {
					Query<Medikament> qMediN = new Query<Medikament>(Medikament.class);
					qMediN.clear();
					qMediN.add(Artikel.FLD_SUB_ID, "LIKE", PhZNr);
					List<Medikament> mediListN = qMediN.execute();
					if (mediListN.size() == 1) {
						continue;
					} else {

						pen.println("PhZNr " + PhZNr + " occurences " + mediListN.size());
						pen.flush();
						long newestint = 0;
						Medikament newest = null;
						for (Medikament medi : mediListN) {
							String updateTime = medi.get(Artikel.FLD_LASTUPDATE);
							if (updateTime.equalsIgnoreCase(""))
								continue;
							long time = Long.parseLong(updateTime);
							if (time > newestint) {
								newestint = time;
								newest = medi;
							}
						}
						pen.println("Keeping " + newest.getLabel() + " from " + newest.getLastUpdate());
						pen.flush();
						mediListN.remove(newest);

						for (Medikament medidel : mediListN) {
							if (!resultIDMap.contains(medidel)) {
								pen.println("Deleting " + medidel.getLabel() + " from " + medidel.getLastUpdate());
								deleted++;
								medidel.delete();
							} else {
								pen.println("Cant delete " + medidel.getLabel() + " from " + medidel.getLastUpdate()
										+ "reference found!");
							}
						}

					}
				}
			}

			// Lösche alle Artikel vom Typ Vidal, die nicht mehr einer
			// Verschreibung zugeordnet sind
			// (Alter Datensatz)
			boolean deleteOldVidal = true;
			if (deleteOldVidal) {
				Query<Artikel> artQuery = new Query<Artikel>(Artikel.class);
				artQuery.clear();
				artQuery.add(Artikel.FLD_TYP, "LIKE", "Vidal");
				List<Artikel> artikelList = artQuery.execute();
				for (Artikel art : artikelList) {
					if (!resultIDMap.contains(art)) {
						pen.println("Deleting " + art.getId() + " as its " + art.get(Artikel.FLD_TYP));
						art.delete();
					} else {
						pen.println("Cant delete " + art.getId() + " as its still referenced in a prescription.");
					}
				}
			}

			boolean deleteNonReferencedBlackBoxes = true;
			if (deleteNonReferencedBlackBoxes) {
				Query<Medikament> blackQuery = new Query<Medikament>(Medikament.class);
				blackQuery.clear();
				blackQuery.add(Medikament.FLD_CODECLASS, "LIKE", "B%");
				List<Medikament> blackMedis = blackQuery.execute();
				pen.println("Found blackmedis: " + blackMedis.size());
				for (Medikament medi : blackMedis) {
					if (!resultIDMap.contains(medi)) {
						pen.println("Deleting black box " + medi.getId() + " as no more references..");
						medi.delete();
					} else {
						pen.println("Cant delete blackbox medi " + medi.getId() + " as still referenced.");
					}
				}
			}

			pen.println("Deleted: " + deleted + " Medikamente");
			pen.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

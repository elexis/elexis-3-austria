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
package at.medevit.elexis.kassen.core.importer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;

import at.medevit.elexis.kassen.core.Activator;
import at.medevit.elexis.kassen.core.importer.model.PointAreas;
import at.medevit.elexis.kassen.core.model.CorePreferenceConstants;
import at.medevit.elexis.kassen.core.model.ForeignKassenLeistung;
import at.medevit.elexis.kassen.core.model.KassenLeistung;
import at.medevit.elexis.kassen.core.model.LeistungBean;
import ch.elexis.core.data.activator.CoreHub;
import ch.elexis.core.data.status.ElexisStatus;
import ch.elexis.core.ui.preferences.SettingsPreferenceStore;
import ch.elexis.data.Query;

public class KassenResourceImporter {
	private static SettingsPreferenceStore globalPrefStore = new SettingsPreferenceStore(
		CoreHub.globalCfg, Activator.PLUGIN_ID);
	
	private List<KassenFile> catalogs;
	private List<KassenFile> points;
	private String preferencePrefix;
	private Class<? extends KassenLeistung> clazz;
	
	protected List<KassenFile> getCatalogs(){
		return catalogs;
	}
	
	public void setCatalogs(Enumeration<URL> entries){
		catalogs = new ArrayList<KassenFile>();
		while (entries.hasMoreElements()) {
			catalogs.add(new KassenFile(entries.nextElement()));
		}
		Collections.sort(catalogs);
	}
	
	protected List<KassenFile> getPoints(){
		return points;
	}
	
	public void setPoints(Enumeration<URL> entries){
		points = new ArrayList<KassenFile>();
		while (entries.hasMoreElements()) {
			points.add(new KassenFile(entries.nextElement()));
		}
		Collections.sort(points);
	}
	
	public void setPreferencePrefix(String prefix){
		preferencePrefix = prefix;
	}
	
	public void setClazz(Class<? extends KassenLeistung> clazz){
		this.clazz = clazz;
	}
	
	public void update(){
		// Update the catalogs if needed
		if (catalogNeedsUpdate()) {
			// which catalog files need to be imported
			List<KassenFile> updateFiles = getUpdateCatalogs();
			for (KassenFile file : updateFiles) {
				runWithUI(new UpdateCatalog(file));
				globalPrefStore.setValue(preferencePrefix
					+ CorePreferenceConstants.KASSE_CATALOGVERSION, file.getVersionString());
			}
		}
		// Update the points if needed
		if (pointsNeedsUpdate()) {
			// which points files need to be imported
			List<KassenFile> updateFiles = getUpdatePoints();
			for (KassenFile file : updateFiles) {
				runWithUI(new UpdatePoints(file));
				globalPrefStore.setValue(preferencePrefix
					+ CorePreferenceConstants.KASSE_POINTSVERSION, file.getVersionString());
			}
		}
	}
	
	public void updateForeign(){
		// Update the catalogs if needed
		if (foreignCatalogNeedsUpdate()) {
			// update from the foreign catalog
			runWithUI(new UpdateForeignCatalog());
			globalPrefStore.setValue(preferencePrefix
				+ CorePreferenceConstants.KASSE_CATALOGVERSION, getForeignCatalogVersion());
		}
	}
	
	private class UpdatePoints implements IRunnableWithProgress {
		KassenFile file;
		
		UpdatePoints(KassenFile file){
			this.file = file;
		}
		
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException{
			monitor.beginTask(
				"Updating Points " + KassenLeistungImporter.getKassenLeistungSystemName(clazz),
				IProgressMonitor.UNKNOWN);
			
			InputStream inStream = null;
			try {
				inStream = file.getURL().openStream();
				if (inStream != null) {
					ElexisStatus status =
						new ElexisStatus(ElexisStatus.INFO, Activator.PLUGIN_ID,
							ElexisStatus.CODE_NONE, "POINTS UPDATE " + file, ElexisStatus.LOG_INFOS);
					StatusManager.getManager().handle(status);
					PointAreas areas = XmlPointAreasImporter.getPointAreas(inStream);
					XmlPointAreasImporter.initializePointAreas(areas, clazz);
				}
			} catch (IOException e) {
				throw new IllegalStateException(e);
			} finally {
				monitor.done();
				try {
					if (inStream != null)
						inStream.close();
				} catch (IOException e) {}
			}
		}
	}
	
	private class UpdateCatalog implements IRunnableWithProgress {
		KassenFile file;
		
		UpdateCatalog(KassenFile file){
			this.file = file;
		}
		
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException{
			monitor.beginTask(
				"Updating Catalog " + KassenLeistungImporter.getKassenLeistungSystemName(clazz),
				IProgressMonitor.UNKNOWN);
			
			InputStream inStream = null;
			try {
				inStream = file.getURL().openStream();
				if (inStream != null) {
					ElexisStatus status =
						new ElexisStatus(ElexisStatus.INFO, Activator.PLUGIN_ID,
							ElexisStatus.CODE_NONE, "CATALOG UPDATE " + file,
							ElexisStatus.LOG_INFOS);
					StatusManager.getManager().handle(status);
					List<LeistungBean> leistungen =
						CsvLeistungsImporter.getLeistungenFromCsvStream(new InputStreamReader(
							inStream, "UTF-8"));
					KassenLeistungImporter importer = new KassenLeistungImporter();
					importer.updateKassenLeistungen(leistungen, clazz);
					importer.closeKassenLeistungen(file.getVersionDate(), clazz);
				}
			} catch (IOException e) {
				throw new IllegalStateException(e);
			} finally {
				monitor.done();
				try {
					if (inStream != null)
						inStream.close();
				} catch (IOException e) {}
			}
		}
	}
	
	private class UpdateForeignCatalog implements IRunnableWithProgress {
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException{
			monitor.beginTask(
				"Updating Catalog " + KassenLeistungImporter.getKassenLeistungSystemName(clazz),
				IProgressMonitor.UNKNOWN);
			
			ElexisStatus status =
				new ElexisStatus(ElexisStatus.INFO, Activator.PLUGIN_ID, ElexisStatus.CODE_NONE,
					"FOREIGN CATALOG UPDATE "
						+ KassenLeistungImporter.getKassenLeistungSystemName(clazz),
					ElexisStatus.LOG_INFOS);
			StatusManager.getManager().handle(status);
			
			ForeignKassenLeistung defaultLeistung =
				(ForeignKassenLeistung) getDefaultInstance(clazz);
			if (defaultLeistung == null)
				throw new IllegalStateException("Could not instantiate with default constructor "
					+ clazz.getName());
			Class<? extends KassenLeistung> foreignClazz =
				defaultLeistung.getConfiguredForeignClazz();
			
			List<KassenLeistung> foreignCodes =
				KassenLeistung.getAllCurrentLeistungen(foreignClazz);
			for (KassenLeistung code : foreignCodes) {
				if (code.getGroup().trim().length() > 0 || code.getPosition().trim().length() > 0) {
					Query<ForeignKassenLeistung> qbe = new Query<ForeignKassenLeistung>(clazz);
					qbe.add(ForeignKassenLeistung.FLD_FOREIGNID, "=", code.getId()); //$NON-NLS-1$ //$NON-NLS-2$
					qbe.add(ForeignKassenLeistung.FLD_FOREIGNCLASSNAME, "=", foreignClazz.getName()); //$NON-NLS-1$ //$NON-NLS-2$
					List<ForeignKassenLeistung> existing = qbe.execute();
					if (existing.size() == 0)
						createInstanceWithCode(clazz, code);
				}
			}
			monitor.done();
		}
	}
	
	private void runWithUI(final IRunnableWithProgress runnable){
		// run the update with progress in the UI Thread if a Display is available ...
		if (isDisplayAvailable()) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run(){
					Shell parent = null;
					try {
						parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					} catch (IllegalStateException e) {
						// the workbench has not been created yet ... create a dummy Shell on the
						// display
						parent = new Shell(Display.getDefault());
					} catch (NullPointerException e) {
						// the workbench has not been created yet ... create a dummy Shell on the
						// display
						parent = new Shell(Display.getDefault());
					}
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(parent);
					try {
						dialog.run(true, false, runnable);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	private boolean catalogNeedsUpdate(){
		String currentVersion =
			globalPrefStore.getString(preferencePrefix
				+ CorePreferenceConstants.KASSE_CATALOGVERSION);
		if (currentVersion.equals(catalogs.get(catalogs.size() - 1).getVersionString()))
			return false;
		return true;
	}
	
	private boolean foreignCatalogNeedsUpdate(){
		String currentVersion =
			globalPrefStore.getString(preferencePrefix
				+ CorePreferenceConstants.KASSE_CATALOGVERSION);
		if (currentVersion.equals(getForeignCatalogVersion()))
			return false;
		return true;
	}
	
	private String getForeignCatalogVersion(){
		String foreignSystem =
			globalPrefStore.getString(preferencePrefix
				+ CorePreferenceConstants.KASSE_FOREIGNCATALOG);
		String foreignPreferencePrefix = CorePreferenceConstants.CFG_KEY + "/" + foreignSystem;
		String foreignVersion =
			globalPrefStore.getString(foreignPreferencePrefix
				+ CorePreferenceConstants.KASSE_CATALOGVERSION);
		return foreignVersion;
	}
	
	private List<KassenFile> getUpdateCatalogs(){
		String currentVersion =
			globalPrefStore.getString(preferencePrefix
				+ CorePreferenceConstants.KASSE_CATALOGVERSION);
		if (currentVersion.equals("")) {
			// return all catalogs if there is no catalog version set
			return catalogs;
		} else {
			// return only newer newer catalogs
			return getUpdateList(catalogs, currentVersion);
		}
	}
	
	private boolean pointsNeedsUpdate(){
		String currentVersion =
			globalPrefStore.getString(preferencePrefix
				+ CorePreferenceConstants.KASSE_POINTSVERSION);
		if (currentVersion.equals(points.get(points.size() - 1).getVersionString()))
			return false;
		return true;
	}
	
	private List<KassenFile> getUpdatePoints(){
		String currentVersion =
			globalPrefStore.getString(preferencePrefix
				+ CorePreferenceConstants.KASSE_POINTSVERSION);
		if (currentVersion.equals("")) {
			// return all points
			return points;
		} else {
			// return only newer points
			return getUpdateList(points, currentVersion);
		}
	}
	
	private List<KassenFile> getUpdateList(List<KassenFile> from, String fromVersion){
		ArrayList<KassenFile> ret = new ArrayList<KassenFile>();
		// only newer newer catalogs
		Date fromVerisonDate = KassenFile.getVersionDate(fromVersion);
		int fromVerisonNumber = KassenFile.getVersionNumber(fromVersion);
		
		for (KassenFile file : from) {
			if (file.getVersionDate().after(fromVerisonDate)
				|| (file.getVersionDate().equals(fromVerisonDate) && file.getVersionNumber() > fromVerisonNumber)) {
				ret.add(file);
			}
		}
		return ret;
	}
	
	protected String getFilesAsString(){
		StringBuilder sb = new StringBuilder();
		
		for (KassenFile file : catalogs) {
			sb.append("CATALOG: " + file + "\n");
		}
		
		for (KassenFile file : points) {
			sb.append("POINTS: " + file + "\n");
		}
		return sb.toString();
	}
	
	protected boolean isDisplayAvailable(){
		try {
			Class.forName("org.eclipse.swt.widgets.Display");
		} catch (ClassNotFoundException e) {
			return false;
		} catch (NoClassDefFoundError e) {
			return false;
		}
		if (Display.getDefault() == null)
			return false;
		else
			return true;
	}
	
	private static KassenLeistung getDefaultInstance(Class<? extends KassenLeistung> clazz){
		KassenLeistung ret = null;
		try {
			ret = (KassenLeistung) clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	private static KassenLeistung createInstanceWithCode(Class<? extends KassenLeistung> clazz,
		KassenLeistung code){
		KassenLeistung ret = null;
		try {
			Constructor constructor = clazz.getConstructor(KassenLeistung.class);
			ret = (KassenLeistung) constructor.newInstance(code);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
}
